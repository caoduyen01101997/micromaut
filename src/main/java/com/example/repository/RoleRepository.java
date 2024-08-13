package com.example.repository;

import com.example.document.Role;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
