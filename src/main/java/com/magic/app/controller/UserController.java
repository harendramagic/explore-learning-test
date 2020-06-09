package com.magic.app.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/user")
	ResponseEntity<List<Users>> getAllUsers() {
		ResponseEntity<List<Users>> response = null;
		List<Users> users = userService.getAllUsers();
		response = new ResponseEntity<List<Users>>(users, HttpStatus.OK);
		return response;
	}

	@GetMapping("/user/{id}")
	ResponseEntity<Users> getUser(@PathVariable("id") int id) {
		ResponseEntity<Users> response = null;
		Users user = userService.getUser(id);
		if (Objects.nonNull(user)) {
			response = new ResponseEntity<Users>(user, HttpStatus.OK);
		} else {
			response = new ResponseEntity<Users>(HttpStatus.NOT_FOUND);
		}
		return response;
	}

	@DeleteMapping("/user/{id}")
	ResponseEntity<Void> deleteUser(@PathVariable("id") int id) {
		ResponseEntity<Void> response = null;
		boolean userExist = userService.isUserExist(id);
		if (userExist) {
			userService.deleteUser(id);
			response = new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			response = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		return response;
	}

	@PostMapping("/user")
	ResponseEntity<Integer> addUser(@RequestBody Users user) {
		ResponseEntity<Integer> response = null;
		if (StringUtils.isEmpty(user.getFirstName()) || StringUtils.isEmpty(user.getLastName())) {
			response = new ResponseEntity<Integer>(HttpStatus.NOT_FOUND);
			return response;
		}
		response = userService.addUser(user);
		return response;
	}
}
