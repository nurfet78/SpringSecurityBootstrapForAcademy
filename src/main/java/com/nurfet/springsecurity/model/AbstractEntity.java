package com.nurfet.springsecurity.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Transient;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.domain.Persistable;


@MappedSuperclass
public abstract class AbstractEntity<T extends Number> implements Persistable<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;


    @Nullable
    @Override
    public T getId() {
        return id;
    }


    public void setId(@Nullable T id) {
        this.id = id;
    }


    @Transient
    @Override
    public boolean isNew() {
        return getId() == null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity<?> that = (AbstractEntity<?>) o;
        if (getId() == null || that.getId() == null) {
            return false;
        }

        return getId().equals(that.getId());
    }


    @Override
    public int hashCode() {
        int hashCode = 17;
        return hashCode += null == getId() ? 0 : getId().hashCode() * 31;
    }
}
