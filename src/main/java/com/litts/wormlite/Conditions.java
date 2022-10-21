package com.litts.wormlite;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Conditions {

    private String where;
    private Object[] whereArgs;

    private long resultLimit;
    private long resultOffset;

    public Conditions() {
    }

    public Conditions(@Nullable String where, @Nullable Object... whereArgs) {
        this.where = where;
        this.whereArgs = whereArgs;
    }

    @NotNull
    public Conditions where(@Nullable String where, @Nullable Object... whereArgs) {
        this.where = where;
        this.whereArgs = whereArgs;
        return this;
    }

    @NotNull
    public Conditions limit(long resultLimit) {
        this.resultLimit = resultLimit;
        this.resultOffset = 0;
        return this;
    }

    @NotNull
    public Conditions limit(long resultLimit, long resultOffset) {
        this.resultLimit = resultLimit;
        this.resultOffset = resultOffset;
        return this;
    }

}
