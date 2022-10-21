package com.litts.wormlite.config;

import com.litts.wormlite.config.annotation.Column;
import com.litts.wormlite.config.annotation.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Field;

public class FieldConfig {

    private final Field access;
    private final SQLiteType sqliteType;
    private final boolean isSerializable;

    private final String column;
    private final boolean isIdentifier;

    protected FieldConfig(@NotNull Field field) {
        access = field;
        access.setAccessible(true);
        sqliteType = SQLiteType.fromJavaType(field.getType());
        isSerializable = Serializable.class.isAssignableFrom(field.getType());
        column = loadColumnName();
        isIdentifier = access.getDeclaredAnnotation(Identifier.class) != null;
    }

    @NotNull
    public String getColumn() {
        return column;
    }

    @NotNull
    public SQLiteType getSqliteType() {
        return sqliteType;
    }

    @NotNull
    public Field getAccess() {
        return access;
    }

    public boolean isIdentifier() {
        return isIdentifier;
    }

    public boolean isSerializable() {
        return isSerializable;
    }

    private String loadColumnName(){
        Column annotation = access.getDeclaredAnnotation(Column.class);
        return annotation != null ? annotation.value() : access.getName().toLowerCase();
    }

}
