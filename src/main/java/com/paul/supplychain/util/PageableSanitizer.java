package com.paul.supplychain.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public final class PageableSanitizer {

    private PageableSanitizer() {
    }

    public static Pageable sanitize(Pageable pageable) {
        if (pageable == null || pageable.getSort().isUnsorted()) {
            return pageable;
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            String property = sanitizeProperty(order.getProperty());
            if (!property.isBlank()) {
                orders.add(order.withProperty(property));
            }
        }

        Sort sort = orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    private static String sanitizeProperty(String property) {
        if (property == null) {
            return "";
        }

        String sanitized = property.trim();
        while (sanitized.startsWith("[") || sanitized.startsWith("\"") || sanitized.startsWith("'")) {
            sanitized = sanitized.substring(1).trim();
        }
        while (sanitized.endsWith("]") || sanitized.endsWith("\"") || sanitized.endsWith("'")) {
            sanitized = sanitized.substring(0, sanitized.length() - 1).trim();
        }

        return sanitized;
    }
}
