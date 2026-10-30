package com.example.electricwaste;

import java.time.Instant;

public record Reading(
        long id,
        long applianceId,
        Instant timestamp,
        String appliance,
        int quantity,
        int unitsOn,
        int expectedUnits,
        double nominalWatts,
        double dailyHours,
        double voltage,
        double current,
        double powerWatts,
        boolean applianceOn,
        boolean expectedOn,
        String status,
        String message
) {}
