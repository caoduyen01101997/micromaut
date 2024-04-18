package com.example.repository;

import com.example.document.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface CommentRepository extends JpaRepository<User, Long> {

}
