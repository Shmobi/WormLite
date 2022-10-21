package com.litts.wormlite.config;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public enum SQLiteType {

    INTEGER(boolean.class, Boolean.class, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class),
    REAL(float.class, Float.class, double.class, Double.class),
    BLOB(byte[].class, Byte[].class, Serializable.class),
    TEXT;

    private final Class<?>[] assignableTypes;

    SQLiteType(Class<?>... assignableTypes) {
        this.assignableTypes = assignableTypes;
    }

    @NotNull
    public static SQLiteType fromJavaType(@NotNull Class<?> javaType) {
        for (SQLiteType sqLiteType : values()) {
            for (Class<?> assignableType : sqLiteType.assignableTypes) {
                if (assignableType.isAssignableFrom(javaType)) {
                    return sqLiteType;
                }
            }
        }
        return TEXT;
    }

}
