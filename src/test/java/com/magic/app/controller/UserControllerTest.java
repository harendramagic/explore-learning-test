package com.magic.app.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magic.app.entity.Users;
import com.magic.app.repository.UserRepository;
import com.magic.app.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserService mockUserService;

	@MockBean
	private UserRepository mockUserRepository;

	Users user = new Users(1, "First", "Last");

	@Test
	public void testGetAllUsers_Unauthorized() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/user")).andExpect(status().isUnauthorized());
	}

	private String basicUserAuth(boolean userAuth) throws UnsupportedEncodingException {
		return "Basic "
				+ Base64.getEncoder().encodeToString(((userAuth ? "user" : "admin") + ":password").getBytes("UTF-8"));
	}

	@Test
	public void testGetAllUsers_Ok() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/user").header("Authorization", basicUserAuth(true)))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetUser_NotFound() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/user/1").header("Authorization", basicUserAuth(true)))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testGetUser_Ok() throws Exception {
		when(mockUserService.getUser(1)).thenReturn(user);
		mvc.perform(MockMvcRequestBuilders.get("/api/user/1").header("Authorization", basicUserAuth(true)))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		verify(mockUserService, times(1)).getUser(1);
	}

	@Test
	public void testDeleteUser_Unauthorized() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/api/user/1")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testDeleteUser_Forbidden() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/api/user/1").header("Authorization", basicUserAuth(true)))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testDeleteUser_NotFound() throws Exception {
		when(mockUserService.isUserExist(1)).thenReturn(false);
		mvc.perform(MockMvcRequestBuilders.delete("/api/user/1").header("Authorization", basicUserAuth(false)))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteUser_Ok() throws Exception {
		when(mockUserService.isUserExist(1)).thenReturn(true);
		doNothing().when(mockUserService).deleteUser(1);
		mvc.perform(MockMvcRequestBuilders.delete("/api/user/1").header("Authorization", basicUserAuth(false)))
				.andExpect(status().isOk());

		verify(mockUserService, times(1)).isUserExist(1);
	}

	@Test
	public void testAddUser_Unauthorized() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/user")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testAddUser_Forbidden() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/user").header("Authorization", basicUserAuth(true)))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testAddUser_BadRequest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/user").header("Authorization", basicUserAuth(false)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testAddUser_UnsupportedMediaType() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/user").content(user.toString()).header("Authorization",
				basicUserAuth(false))).andExpect(status().isUnsupportedMediaType());
	}

	@Test
	public void testAddUser_Found() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		when(mockUserService.addUser(Mockito.any())).thenReturn(new ResponseEntity<Integer>(HttpStatus.FOUND));
		when(mockUserRepository.findByFirstNameAndLastName(Mockito.anyString(), Mockito.anyString())).thenReturn(user);
		mvc.perform(MockMvcRequestBuilders.post("/api/user").content(objectMapper.writeValueAsString(user))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.header("Authorization", basicUserAuth(false))).andExpect(status().isFound());

		verify(mockUserService, times(1)).addUser(Mockito.any());
	}

	@Test
	public void testAddUser_Created() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		when(mockUserService.addUser(Mockito.any()))
				.thenReturn(new ResponseEntity<Integer>(user.getId(), HttpStatus.CREATED));
		mvc.perform(MockMvcRequestBuilders.post("/api/user").content(objectMapper.writeValueAsString(user))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.header("Authorization", basicUserAuth(false))).andExpect(status().isCreated());

		verify(mockUserService, times(1)).addUser(Mockito.any());
	}

}
