package com.KOndziu.usercarservice.controllers;

import com.KOndziu.usercarservice.modules.User;
import com.KOndziu.usercarservice.payload.UserDTO;
import com.KOndziu.usercarservice.repos.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;


    @Test
    void getUserByIdTest() throws Exception {
        UserDTO userDTO = new UserDTO(2, "Test", "User", "", "", "");

        mockMvc.perform(get("/users/{userId}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());

        Optional<User> user = userRepository.findById(2);

        assertEquals(user.get().getName(), userDTO.getName());
        assertEquals(user.get().getSecondName(), userDTO.getSecondName());
    }

    @Test
    public void findUserByNameAndSurnameTest() throws Exception{
        UserDTO userDTO = new UserDTO(2, "Test", "User", "", "", "");

        mockMvc.perform(get("/users/find/{name}/{surname}","Test","User")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(userDTO.getId())))
                .andExpect(jsonPath("$.name",Matchers.is(userDTO.getName())));
    }

    @Test
    public void addExistingUserTest() throws Exception{
        UserDTO userDTO=new UserDTO(1,"Kacper","Podgórski","","","");

        String jsonUserDTO=objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/users/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonUserDTO))
                .andExpect(content().string("User already exists"));
    }
}