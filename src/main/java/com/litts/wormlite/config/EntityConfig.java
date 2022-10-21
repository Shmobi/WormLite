package com.litts.wormlite.config;

import com.litts.wormlite.config.annotation.Ignored;
import com.litts.wormlite.config.annotation.Table;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EntityConfig<E> {

    private final static Map<Class<?>, EntityConfig<?>> REGISTRY = new HashMap<>();

    private final Class<E> entityType;
    private final Map<String, FieldConfig> fieldConfigs = new HashMap<>();
    private final String identifierName;
    private final String table;

    private EntityConfig(Class<E> entityType) {
        this.entityType = entityType;
        identifierName = loadFieldConfigs(entityType);
        table = loadTableName();
    }

    @NotNull
    public static <T> EntityConfig<T> loadConfig(@NotNull Class<T> entityType) {
        EntityConfig<T> config;
        synchronized (REGISTRY) {
            config = (EntityConfig<T>) REGISTRY.get(entityType);
            if (config == null) {
                config = new EntityConfig<>(entityType);
                REGISTRY.put(entityType, config);
            }
        }
        return config;
    }

    @NotNull
    public Class<E> getEntityType() {
        return entityType;
    }

    @NotNull
    public String getIdentifierName() {
        return identifierName;
    }

    @Nullable
    public FieldConfig getFieldConfig(@NotNull String fieldName) {
        return fieldConfigs.get(fieldName);
    }

    @NotNull
    public String getTable() {
        return table;
    }

    private String loadFieldConfigs(Class<?> type) {
        String identifierName = null;
        if (type != Object.class) {
            identifierName = loadFieldConfigs(type);
            for (Field field : type.getDeclaredFields()) {
                if (!isFieldIgnored(field)) {
                    FieldConfig config = new FieldConfig(field);
                    if (fieldConfigs.put(config.getColumn(), config) != null) {
                        throw new EntityConfigException("Duplicate field '" + config.getColumn() + "' found in class hierarchy: " + entityType);
                    }
                    if (config.isIdentifier()) {
                        if (identifierName != null) {
                            throw new EntityConfigException("Multiple identifiers found in class hierarchy: " + entityType);
                        }
                        identifierName = config.getColumn();
                    }
                }
            }
        }
        return identifierName;
    }

    private boolean isFieldIgnored(Field field) {
        Ignored annotation = field.getDeclaredAnnotation(Ignored.class);
        if (annotation != null) {
            return annotation.value();
        } else {
            int modifiers = field.getModifiers();
            return Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers);
        }
    }

    private String loadTableName(){
        Table annotation = entityType.getDeclaredAnnotation(Table.class);
        return annotation != null ? annotation.value() : entityType.getSimpleName().toUpperCase();
    }

}
