package com.litts.wormlite;

import com.litts.wormlite.config.EntityConfig;
import com.litts.wormlite.config.EntityConfigException;
import com.litts.wormlite.factory.DataFactory;
import com.litts.wormlite.factory.EntityFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.parser.Entity;
import java.util.Collection;
import java.util.List;

public class Repository<D, I> {

    private final DataFactory<D> dataFactory;
    private final EntityFactory<I> entityFactory;
    private final SQLiteFunctionality<D, I> functionality;

    protected Repository(@NotNull DataFactory<D> dataFactory, @NotNull EntityFactory<I> entityFactory, SQLiteFunctionality<D, I> functionality) {
        this.dataFactory = dataFactory;
        this.entityFactory = entityFactory;
        this.functionality = functionality;
    }

    /* CREATE */
    public void create(@NotNull Object entity) {
        EntityConfig<?> config = EntityConfig.loadConfig(entity.getClass());
        D data = dataFactory.createData(entity);
        functionality.insert(config.getTable(), data);
    }

    public void createAll(@NotNull Object... entities) {
        for (Object entity : entities) {
            if (entity != null) {
                create(entity);
            }
        }
    }

    public void createAll(@NotNull Collection<?> entities) {
        for (Object entity : entities) {
            if (entity != null) {
                create(entity);
            }
        }
    }

    /* READ */
    @Nullable
    public <E> E read(@NotNull Class<E> entityType, long identifier, @Nullable String... fields) {
        EntityConfig<E> config = EntityConfig.loadConfig(entityType);
        Conditions conditions = new Conditions(SQLiteUtil.whereFieldMatchesValue(config.getIdentifierName()), identifier);
        return getFirst(readWhere(entityType, conditions, fields));
    }

    @NotNull
    public <E> List<E> readAll(@NotNull Class<E> entityType, @Nullable String... fields) {
        return readWhere(entityType, null, fields);
    }

    @NotNull
    public <E> List<E> readWhere(@NotNull Class<E> entityType, @Nullable Conditions conditions, @Nullable String... fields) {
        EntityConfig<E> config = EntityConfig.loadConfig(entityType);
        I input = functionality.query(config.getTable(), conditions, fields);
        return entityFactory.createEntities(entityType, input);
    }

    public long count(Class<?> entityType) {
        return countWhere(entityType, null);
    }

    public long countWhere(Class<?> entityType, Conditions conditions) {
        EntityConfig<?> config = EntityConfig.loadConfig(entityType);
        return functionality.queryCount(config.getTable(), conditions);
    }

    /* UPDATE */
    public boolean update(@NotNull Object entity, @Nullable String... fields) {
        EntityConfig<?> config = EntityConfig.loadConfig(entity.getClass());
        Conditions conditions = new Conditions(SQLiteUtil.whereFieldMatchesValue(config.getIdentifierName()));
        return updateWhere(entity.getClass(), conditions, dataFactory.createData(entity, fields)) > 0;
    }

    public long updateAll(@NotNull Object[] entities, @Nullable String... fields) {
        long updatedEntities = 0;
        for (Object entity : entities) {
            if (entity != null) {
                if (update(entity, fields)) {
                    updatedEntities++;
                }
            }
        }
        return updatedEntities;
    }

    public long updateAll(@NotNull Collection<?> entities, @Nullable String... fields) {
        long updatedEntities = 0;
        for (Object entity : entities) {
            if (entity != null) {
                if (update(entity, fields)) {
                    updatedEntities++;
                }
            }
        }
        return updatedEntities;
    }

    public long updateWhere(@NotNull Class<?> entityType, @Nullable Conditions conditions, @NotNull D data) {
        EntityConfig<?> config = EntityConfig.loadConfig(entityType);
        return functionality.update(config.getTable(), conditions, data);
    }

    /* DELETE */
    public void delete(@NotNull Object entity) {
        EntityConfig<?> config = EntityConfig.loadConfig(entity.getClass());
        Conditions conditions = new Conditions(SQLiteUtil.whereFieldMatchesValue(config.getIdentifierName()), getIdentifier(config, entity));
        deleteWhere(entity.getClass(), conditions);
    }

    public void deleteAll(@NotNull Object... entities) {
        for (Object entity : entities) {
            if (entity != null) {
                delete(entity);
            }
        }
    }

    public void deleteAll(@NotNull Collection<Object> entities) {
        for (Object entity : entities) {
            if (entity != null) {
                delete(entity);
            }
        }
    }

    public void deleteType(@NotNull Class<?> entityType) {
        deleteWhere(entityType, null);
    }

    public void deleteWhere(@NotNull Class<?> entityType, @Nullable Conditions conditions){
        EntityConfig<?> config = EntityConfig.loadConfig(entityType);
        functionality.delete(config.getTable(), conditions);
    }

    /* FACTORY */
    @NotNull
    public DataFactory<D> getDataFactory() {
        return dataFactory;
    }

    private <E> E getFirst(List<E> list) {
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    private static long getIdentifier(EntityConfig<?> config, Object entity) {
        try {
            return config.getFieldConfig(config.getIdentifierName()).getAccess().getLong(entity);
        } catch (Exception ex) {
            throw new EntityConfigException("Entity does not have an identifier of type long: " + config.getEntityType(), ex);
        }
    }

}
