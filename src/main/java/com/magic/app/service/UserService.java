package com.magic.app.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.magic.app.entity.Users;
import com.magic.app.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public Users getUser(int id) {
		return userRepository.findById(id);
	}

	public List<Users> getAllUsers() {
		return userRepository.findAll(Sort.by(Direction.ASC, "lastName"));
	}

	public void deleteUser(int id) {
		userRepository.deleteById(id);
	}

	public boolean isUserExist(int id) {
		return userRepository.existsById(id);
	}

	public ResponseEntity<Integer> addUser(Users user) {
		ResponseEntity<Integer> response = null;
		Users objUser = userRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());
		if (Objects.nonNull(objUser)) {
			response = new ResponseEntity<>(HttpStatus.FOUND);
			return response;
		}
		Users users = userRepository.save(user);
		response = new ResponseEntity<>(users.getId(), HttpStatus.CREATED);
		return response;
	}
}
