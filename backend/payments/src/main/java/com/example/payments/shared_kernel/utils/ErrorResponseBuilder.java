package com.example.payments.shared_kernel.utils;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponseBuilder {
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";

    public static Map<String, String> buildErrorResponse(Enum<?> errorType, String message) {
        Map<String, String> response = new HashMap<>();

        if (errorType != null) {
            response.put(ERROR, errorType.name());
        }

        response.put(MESSAGE, message);

        return response;
    }
}
