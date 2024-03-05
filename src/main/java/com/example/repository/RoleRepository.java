package com.example.repository;

import com.example.document.Role;
import com.example.document.User;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

@MongoRepository
public interface RoleRepository extends CrudRepository<Role, String> {
}
