package com.project.image_service.utils;

import java.util.Set;

public class FormatHelper {
    private static final Set<String> SUPPORTED_FORMATS = Set.of("png", "jpg", "jpeg");

    public static String normalizeFormat(String requestedFormat) {
        if (requestedFormat == null) return "png";

        String format = requestedFormat.toLowerCase();

        if (!SUPPORTED_FORMATS.contains(format)) {
            return "png";
        }

        return format.equals("jpg") ? "jpeg" : format;
    }
}
