package com.toolrental.domain.entity;

/**
 * All persisted entities must have a primary key.
 * @param <PrimaryKeyType>
 */
public interface Entity<PrimaryKeyType> {
    PrimaryKeyType getId();
}
