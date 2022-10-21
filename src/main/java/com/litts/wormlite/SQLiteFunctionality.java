package com.litts.wormlite;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SQLiteFunctionality<D, I> {

        void insert(@NotNull String table, @NotNull D data);
        I query(@NotNull String table, @Nullable Conditions conditions, @Nullable String... columns);
        long queryCount(@NotNull String table, @Nullable Conditions conditions);
        long update(@NotNull String table, @Nullable Conditions conditions, @NotNull D data);
        void delete(@NotNull String table, @Nullable Conditions conditions);

}
