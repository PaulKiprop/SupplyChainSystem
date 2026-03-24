package com.paul.supplychain.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageableSanitizerTest {

    @Test
    void shouldSanitizeSwaggerStyleSortProperty() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Order.asc("[\"createdAt\"]")));

        Pageable sanitized = PageableSanitizer.sanitize(pageable);

        assertEquals("createdAt", sanitized.getSort().iterator().next().getProperty());
    }

    @Test
    void shouldKeepNormalSortPropertyUnchanged() {
        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Order.desc("updatedAt")));

        Pageable sanitized = PageableSanitizer.sanitize(pageable);

        assertEquals("updatedAt", sanitized.getSort().iterator().next().getProperty());
        assertEquals(1, sanitized.getPageNumber());
        assertEquals(10, sanitized.getPageSize());
    }
}
