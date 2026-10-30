package com.example.electricwaste;

public record ApplianceConfig(
        long id,
        String name,
        int quantity,
        double powerWatts,
        int unitsOn,
        int expectedUnits,
        double dailyHours
) {}
