package com.novare.tredara.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novare.tredara.models.User;
import com.novare.tredara.payloads.LoginRequest;
import com.novare.tredara.payloads.SignupRequest;
import com.novare.tredara.security.jwt.JwtTokenUtil;
import com.novare.tredara.security.userdetails.UserDetailsImpl;
import com.novare.tredara.services.LogService;
import com.novare.tredara.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenUtil jwtUtils;

    @MockBean
    private UserService userService;

    @MockBean
    private LogService logService;

    @Test
    void testAuthenticateUserSuccess() throws Exception {
        // Create a sample LoginRequest
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

        // Mock the authentication process
        UserDetailsImpl userDetails = UserDetailsImpl.build(new User());
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtCookie(userDetails)).thenReturn(ResponseCookie.from("jwtCookie", "jwtToken").build());

        mockMvc.perform(post("/api/v1/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest))
                        .with(csrf())) // Enable CSRF protection for the test
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, "jwtCookie=jwtToken; HttpOnly; Secure; SameSite=Strict"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.fullName").exists())
                .andExpect(jsonPath("$.role").exists())
                .andExpect(jsonPath("$.type").exists());
    }

    @Test
    void testAuthenticateUserAuthenticationFailure() throws Exception {
        // Create a sample LoginRequest with incorrect credentials
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongPassword");

        // Mock the authentication process to throw an exception (authentication failure)
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Authentication failed"));

        mockMvc.perform(post("/api/v1/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest))
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Authentication failed: Authentication failed"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "CUSTOMER")
    void testRegisterUserSuccess() throws Exception {
        // Create a sample SignupRequest
        SignupRequest signupRequest = new SignupRequest(1L, "existing@example.com", "password","",0);

        // Mock user service behavior
        when(userService.isPasswordValid(signupRequest.getPassword())).thenReturn(true);
        when(userService.existsByEmail(signupRequest.getEmail())).thenReturn(false);

        // Mock JWT generation
        UserDetailsImpl userDetails = UserDetailsImpl.build(new User());
        when(jwtUtils.generateJwtCookie(userDetails)).thenReturn(ResponseCookie.from("jwtCookie", "jwtToken").build());

        mockMvc.perform(post("/api/v1/signup/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signupRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, "jwtCookie=jwtToken; HttpOnly; Secure; SameSite=Strict"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.fullName").exists())
                .andExpect(jsonPath("$.role").exists());
    }

    @Test
    void testRegisterUserInvalidPassword() throws Exception {
        // Create a sample SignupRequest with an invalid password
        SignupRequest signupRequest = new SignupRequest(1L, "existing@example.com", "password","",0);

        mockMvc.perform(post("/api/v1/signup/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signupRequest))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Password does not meet the criteria. It must be at least 8 characters and contain uppercase and lowercase letters and special symbols"));
    }

    @Test
    void testRegisterUserEmailInUse() throws Exception {
        // Create a sample SignupRequest with an email that is already in use
        SignupRequest signupRequest = new SignupRequest(1L, "existing@example.com", "password","",0);

        // Mock user service behavior
        when(userService.isPasswordValid(signupRequest.getPassword())).thenReturn(true);
        when(userService.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        mockMvc.perform(post("/api/v1/signup/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signupRequest))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already in use!"));
    }

    @Test
    @WithMockUser
    void testLogoutUser() throws Exception {
        mockMvc.perform(post("/api/v1/logout/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, "jwtCookie=; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; HttpOnly; Secure; SameSite=Strict"))
                .andExpect(jsonPath("$.message").value("You've been signed out!"));
    }

    // Utility method to convert an object to JSON
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
