package com.ducle.chat_service.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationHelper {
    private PaginationHelper() {
    }

    public static <E extends Enum<E>> Pageable generatePageable(
            int page, int size,
            String sortBy, String sortDir,
            Class<E> enumClass) {
        String[] fields = sortBy.split(",");
        String[] directions = sortDir.split(",");

        if (fields.length != directions.length) {
            throw new IllegalArgumentException("sortBy and sortDir must have the same number of elements.");
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            E sortField;
            try {
                sortField = Enum.valueOf(enumClass, fields[i]);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid sortBy value: " + fields[i]);
            }

            Sort.Direction direction;
            if (directions[i].equalsIgnoreCase("asc")) {
                direction = Sort.Direction.ASC;
            } else if (directions[i].equalsIgnoreCase("desc")) {
                direction = Sort.Direction.DESC;
            } else {
                throw new IllegalArgumentException(
                        "Invalid sortDir value: " + directions[i] + ". Only 'asc' or 'desc' allowed.");
            }

            orders.add(new Sort.Order(direction, sortField.name()));
        }

        return PageRequest.of(page, size, Sort.by(orders));
    }

}
