package com.toolrental.domain.repository;

import com.toolrental.domain.entity.Entity;

// TODO in real system this would be removed and we use spring-data instead. See org.springframework.data.repository.CrudRepository
//      - this is only here for the InMemoryRepository used for the demo.
public interface Repository<EntityType extends Entity<PrimaryKeyType>, PrimaryKeyType> {

    void create(EntityType obj);

    EntityType get(PrimaryKeyType primaryKey);

    void delete(PrimaryKeyType primaryKey);
}
