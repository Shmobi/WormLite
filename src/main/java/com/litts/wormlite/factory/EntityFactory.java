package com.litts.wormlite.factory;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface EntityFactory<I> {

    @NotNull
    <E> List<E> createEntities(@NotNull Class<E> entityType, @NotNull I input);

}
