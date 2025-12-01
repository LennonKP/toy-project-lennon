package com.example.demo.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.ListCrudRepository;

import com.example.demo.repository.entity.User;

public interface UserRepository extends ListCrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByHandle(String handle);

    Set<User> findByEmailIn(Collection<String> emails);

    boolean existsByHandle(String handle);
}
