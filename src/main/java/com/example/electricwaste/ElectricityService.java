package com.example.electricwaste;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ElectricityService {
    private static final double VOLTAGE = 230.0;
    private static final double SAMPLE_SECONDS = 3.0;
    private static final int MAX_READINGS = 500;

    private final AtomicLong sequence = new AtomicLong();
    private final AtomicLong applianceSequence = new AtomicLong();
    private final Random random = new Random();
    private final List<Reading> readings = new CopyOnWriteArrayList<>();
    private final List<ApplianceConfig> appliances = new ArrayList<>();
    private double simulatedEnergyKwh = 0.0;

    public synchronized List<ApplianceConfig> getAppliances() {
        return List.copyOf(appliances);
    }

    public synchronized List<ApplianceConfig> saveAppliances(List<ApplianceConfig> requested) {
        if (requested == null) {
            throw new IllegalArgumentException("Appliance configuration is required.");
        }
        if (requested.size() > 100) {
            throw new IllegalArgumentException("Please configure no more than 100 appliance groups.");
        }

        List<ApplianceConfig> validated = new ArrayList<>();
        for (ApplianceConfig item : requested) {
            if (item == null || item.name() == null || item.name().isBlank()) {
                throw new IllegalArgumentException("Every appliance needs a name.");
            }
            if (item.quantity() < 1 || item.quantity() > 500) {
                throw new IllegalArgumentException("Quantity must be between 1 and 500 for " + item.name() + ".");
            }
            if (!Double.isFinite(item.powerWatts()) || item.powerWatts() <= 0 || item.powerWatts() > 100_000) {
                throw new IllegalArgumentException("Power rating must be between 1 and 100000 W for " + item.name() + ".");
            }
            if (item.unitsOn() < 0 || item.unitsOn() > item.quantity()) {
                throw new IllegalArgumentException("Units currently ON must be between 0 and quantity for " + item.name() + ".");
            }
            if (item.expectedUnits() < 0 || item.expectedUnits() > item.quantity()) {
                throw new IllegalArgumentException("Expected units must be between 0 and quantity for " + item.name() + ".");
            }
            if (!Double.isFinite(item.dailyHours()) || item.dailyHours() < 0 || item.dailyHours() > 24) {
                throw new IllegalArgumentException("Daily usage must be between 0 and 24 hours for " + item.name() + ".");
            }
            validated.add(new ApplianceConfig(applianceSequence.incrementAndGet(), item.name().trim(),
                    item.quantity(), item.powerWatts(), item.unitsOn(), item.expectedUnits(), item.dailyHours()));
        }

        appliances.clear();
        appliances.addAll(validated);
        readings.clear();
        simulatedEnergyKwh = 0.0;
        if (!appliances.isEmpty()) {
            generateCycle();
        }
        return List.copyOf(appliances);
    }

    @Scheduled(fixedRate = 3000)
    public synchronized void generateCycle() {
        for (ApplianceConfig appliance : appliances) {
            readings.add(createReading(appliance));
        }
        while (readings.size() > MAX_READINGS) {
            readings.remove(0);
        }
    }

    private Reading createReading(ApplianceConfig appliance) {
        int onUnits = appliance.unitsOn();
        boolean isOn = onUnits > 0;
        boolean expectedOn = onUnits <= appliance.expectedUnits();
        double nominalGroupPower = onUnits * appliance.powerWatts();
        double power = 0.0;
        if (isOn) {
            power = nominalGroupPower * (0.94 + random.nextDouble() * 0.12);
            if (random.nextDouble() < 0.04) {
                power = nominalGroupPower * 1.20;
            }
        }

        String status;
        String message;
        if (isOn && !expectedOn) {
            status = "WASTE";
            message = (onUnits - appliance.expectedUnits()) + " more unit(s) are ON than marked as needed.";
        } else if (isOn && power > nominalGroupPower * 1.15) {
            status = "HIGH_USAGE";
            message = "Simulated power draw is above the expected rating.";
        } else {
            status = "NORMAL";
            message = isOn ? "Usage is within the configured expectation." : "All units in this group are switched off.";
        }

        simulatedEnergyKwh += power * SAMPLE_SECONDS / 3_600_000.0;
        return new Reading(
                sequence.incrementAndGet(), appliance.id(), Instant.now(), appliance.name(), appliance.quantity(), onUnits,
                appliance.expectedUnits(), appliance.powerWatts(), appliance.dailyHours(),
                round(VOLTAGE + random.nextDouble() * 10.0 - 5.0), round(power / VOLTAGE), round(power),
                isOn, expectedOn, status, message
        );
    }

    public synchronized List<Reading> getRecentReadings() {
        return readings.stream().sorted(Comparator.comparingLong(Reading::id).reversed()).limit(50).toList();
    }

    public synchronized Map<String, Object> getSummary() {
        double connectedLoad = appliances.stream().mapToDouble(a -> a.quantity() * a.powerWatts()).sum();
        double configuredCurrentLoad = appliances.stream().mapToDouble(a -> a.unitsOn() * a.powerWatts()).sum();
        double estimatedDailyEnergy = appliances.stream()
                .mapToDouble(a -> a.unitsOn() * a.powerWatts() * a.dailyHours() / 1000.0).sum();

        Map<Long, Reading> latestByAppliance = readings.stream().collect(Collectors.toMap(
                Reading::applianceId, reading -> reading,
                (first, second) -> first.id() > second.id() ? first : second));
        double activePower = latestByAppliance.values().stream().filter(Reading::applianceOn)
                .mapToDouble(Reading::powerWatts).sum();
        long alerts = latestByAppliance.values().stream()
                .filter(r -> r.status().equals("WASTE") || r.status().equals("HIGH_USAGE")).count();
        int unitCount = appliances.stream().mapToInt(ApplianceConfig::quantity).sum();

        return Map.of(
                "applianceCount", unitCount,
                "applianceGroups", appliances.size(),
                "connectedLoadWatts", round(connectedLoad),
                "configuredCurrentLoadWatts", round(configuredCurrentLoad),
                "activePowerWatts", round(activePower),
                "estimatedEnergyKwh", round(estimatedDailyEnergy),
                "simulatedEnergyKwh", round(simulatedEnergyKwh),
                "activeAlerts", alerts,
                "totalReadings", sequence.get()
        );
    }

    public synchronized List<Reading> generateManualCycle() {
        generateCycle();
        return new ArrayList<>(getRecentReadings());
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
