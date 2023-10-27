package com.nurfet.springsecurity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "roles")
public final class Role extends AbstractEntity<Long> implements GrantedAuthority {

    @Serial
    private static final  long serialVersionUID = 7217778059836250424L;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();


    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String getAuthority() {
        return name;
    }


    @Override
    public String toString() {
        return name;
    }
}
