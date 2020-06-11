package com.magic.app.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.magic.app.entity.Users;
import com.magic.app.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {

	@InjectMocks
	UserService userService;

	@Mock
	UserRepository userRepository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	Users user = new Users(1, "fName", "lName");

	@Test
	public void testGetUserById() {
		when(userRepository.findById(Mockito.anyInt())).thenReturn(user);
		Users objUser = userService.getUser(1);
		assertEquals(objUser.getFirstName(), user.getFirstName());

		verify(userRepository, times(1)).findById(1);
	}

	@Test
	public void testGetAllUsers() {
		List<Users> userList = new ArrayList<Users>();
		userList.add(user);
		when(userRepository.findAll(Sort.by(Direction.ASC, "lastName"))).thenReturn(userList);
		
		List<Users> listUsers = userService.getAllUsers();
		assertEquals(1, listUsers.size());

		verify(userRepository, times(1)).findAll(Sort.by(Direction.ASC, "lastName"));
	}

	@Test
	public void TestIsUserExist() {
		when(userRepository.existsById(Mockito.anyInt())).thenReturn(true);
		
		boolean isExist = userService.isUserExist(1);
		assertTrue(isExist);

		verify(userRepository, times(1)).existsById(1);
	}

	@Test
	public void testDeleteUser() {
		doNothing().when(userRepository).deleteById(Mockito.anyInt());
		userService.deleteUser(Mockito.anyInt());
		verify(userRepository, times(1)).deleteById(Mockito.anyInt());
	}

	@Test
	public void testAddUser_Created() {
		Integer id = 1;
		when(userRepository.save(Mockito.any())).thenReturn(user);
		
		ResponseEntity<Integer> response = userService.addUser(user);
		assertEquals(id, response.getBody());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		verify(userRepository, times(1)).save(Mockito.any());
	}
	
	@Test
	public void testAddUser_Found() {
		Integer id = null;
		when(userRepository.findByFirstNameAndLastName(Mockito.anyString(), Mockito.anyString())).thenReturn(user);
		
		ResponseEntity<Integer> response = userService.addUser(user);
		assertEquals(id, response.getBody());
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		
		verify(userRepository, times(1)).findByFirstNameAndLastName(Mockito.anyString(), Mockito.anyString());
	}
}
