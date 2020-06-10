package com.magic.app.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void testGetAllUsersUnauthorized() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/user")).andExpect(status().isUnauthorized());
	}

	private String basicUserAuth(boolean userAuth) throws UnsupportedEncodingException {
		return "Basic "
				+ Base64.getEncoder().encodeToString(((userAuth ? "user" : "admin") + ":password").getBytes("UTF-8"));
	}

	@Test
	public void testGetAllUsers() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/user").header("Authorization", basicUserAuth(false)))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetUserNotFound() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/user/1").header("Authorization", basicUserAuth(false)))
				.andExpect(status().isNotFound());
	}

}
