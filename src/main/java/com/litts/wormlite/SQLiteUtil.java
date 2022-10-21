package com.litts.wormlite;

import org.jetbrains.annotations.NotNull;

public class SQLiteUtil {

    private final static String WHERE_FIELD_MATCHES_VALUE = "%s=?";

    @NotNull
    public static String whereFieldMatchesValue(@NotNull String fieldName){
        return String.format(WHERE_FIELD_MATCHES_VALUE, fieldName);
    }

}
