package com.homenetworkmonitor.models.enums;

public enum WifiBand {
    GHZ_2_4,
    GHZ_5,
    UNKNOWN;

    public static WifiBand fromRouterLabel(String label) {
        if (label == null) {
            return UNKNOWN;
        }
        return switch (label.trim().toUpperCase()) {
            case "2.4G" -> GHZ_2_4;
            case "5G" -> GHZ_5;
            default -> UNKNOWN;
        };
    }
}
