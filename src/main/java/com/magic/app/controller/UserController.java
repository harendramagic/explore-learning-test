package com.magic.app.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magic.app.entity.Users;
import com.magic.app.service.UserService;

@RestController
@RequestMapping("/api")
@Validated
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/user")
	public ResponseEntity<List<Users>> getAllUsers() {
		ResponseEntity<List<Users>> response = null;
		List<Users> users = userService.getAllUsers();
		response = new ResponseEntity<>(users, HttpStatus.OK);
		return response;
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<Users> getUser(@PathVariable("id") int id) {
		ResponseEntity<Users> response = null;
		Users user = userService.getUser(id);
		if (Objects.nonNull(user)) {
			response = new ResponseEntity<>(user, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return response;
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable("id") int id) {
		ResponseEntity<Void> response = null;
		boolean userExist = userService.isUserExist(id);
		if (userExist) {
			userService.deleteUser(id);
			response = new ResponseEntity<>(HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return response;
	}

	@PostMapping("/user")
	public ResponseEntity<Integer> addUser(@RequestBody Users user) {
		ResponseEntity<Integer> response = null;
		if (StringUtils.isEmpty(user.getFirstName()) || StringUtils.isEmpty(user.getLastName())) {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			return response;
		}
		response = userService.addUser(user);
		return response;
	}
}
