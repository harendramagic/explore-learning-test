package com.magic.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.magic.app.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

	Users findById(int id);

	Users findByFirstNameAndLastName(String firstName, String lastName);
}
