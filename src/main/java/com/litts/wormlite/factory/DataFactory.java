package com.litts.wormlite.factory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DataFactory<D>{

    @NotNull
    D createData(@NotNull Object entity, @Nullable String... fields);

}
