package com.planagency.mapping;

import java.util.HashMap;
import java.util.Map;


public class ValidationMapper {

    private static final Map<String, String> TYPE_MAPPING = new HashMap<>();
    private static final Map<String, String> VALIDATION_MAPPING = new HashMap<>();

    static {

        TYPE_MAPPING.put("string", "String");
        TYPE_MAPPING.put("long", "Long");
        TYPE_MAPPING.put("int", "Integer");
        TYPE_MAPPING.put("double", "Double");
        TYPE_MAPPING.put("boolean", "Boolean");
        TYPE_MAPPING.put("date", "LocalDateTime");


        VALIDATION_MAPPING.put("email", "@Email");
        VALIDATION_MAPPING.put("firstName", "@NotBlank(message = \"First name is required\")");
        VALIDATION_MAPPING.put("lastName", "@NotBlank(message = \"Last name is required\")");
        VALIDATION_MAPPING.put("phone", "@Pattern(regexp = \"^[0-9]{10}$\")");
        VALIDATION_MAPPING.put("age", "@Min(0) @Max(150)");
    }

    public static String mapType(String userType) {
        return TYPE_MAPPING.getOrDefault(userType.toLowerCase(), "String");
    }

    public static String mapValidation(String fieldName) {
        return VALIDATION_MAPPING.getOrDefault(fieldName, "");
    }
}

