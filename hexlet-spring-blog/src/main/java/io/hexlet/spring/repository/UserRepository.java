package io.hexlet.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.hexlet.spring.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
}
