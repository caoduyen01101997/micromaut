package com.example.repository;

import com.example.document.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface CommentRepository extends JpaRepository<User, Long> {

}
