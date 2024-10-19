package com.toolrental.domain.repository;

import com.toolrental.domain.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO in a real system Repository would be backed by the spring-data persistence store. e.g. relational database.
 *      For this demo, use a simple in memory repository.
 */
public class InMemoryRepository<EntityType extends Entity<PrimaryKeyType>, PrimaryKeyType> implements Repository<EntityType, PrimaryKeyType> {

    private Map<PrimaryKeyType, EntityType> values = new HashMap<>();

    @Override
    public void create(EntityType obj){
        values.put(obj.getId(), obj);
    }

    @Override
    public EntityType get(PrimaryKeyType primaryKey){
        return values.get(primaryKey);
    }

    @Override
    public void delete(PrimaryKeyType primaryKey){
        values.remove(primaryKey);
    }
}
