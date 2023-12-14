package com.example.fourth.web.controller;

import com.example.fourth.exception.EntityNotFoundException;
import com.example.fourth.mapper.UserMapper;
import com.example.fourth.model.User;
import com.example.fourth.service.UserService;
import com.example.fourth.util.TestUtil;
import com.example.fourth.web.AbstractTestController;
import com.example.fourth.web.model.defaults.FindAllSettings;
import com.example.fourth.web.model.user.UserListResponse;
import com.example.fourth.web.model.user.UserResponse;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

public class UserControllerTest extends AbstractTestController {

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;


    @Test
    public void whenFindAllUsers_returnAllUser() throws Exception {
        User user1 = createUser(1L, null, null);
        User user2 = createUser(2L, null, null);
        User user3 = createUser(3L, null, null);
        List<User> users = new ArrayList<>() {{
            add(user1);
            add(user2);
            add(user3);
        }};

        UserResponse userResponse1 = createUserResponse(1L);
        UserResponse userResponse2 = createUserResponse(2L);
        UserResponse userResponse3 = createUserResponse(3L);
        List<UserResponse> userResponses = new ArrayList<>() {{
            add(userResponse1);
            add(userResponse2);
            add(userResponse3);
        }};
        UserListResponse response = new UserListResponse(userResponses);
        FindAllSettings settings = getFindAllSettings();

        Mockito.when(userService.findAll(settings)).thenReturn(users);
        Mockito.when(userMapper.userListToUserListResponse(users)).thenReturn(response);

        String expected = TestUtil
                .readStringFromResource("response/user/find-all.json");
        String actual = mockMvc.perform(get("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(settings)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(userService, Mockito.times(1)).findAll(settings);
        Mockito.verify(userMapper, Mockito.times(1)).userListToUserListResponse(users);

        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    public void whenFindUserById_returnUserResponse() throws Exception {
        User user = createUser(1L, null, null);
        UserResponse userResponse = createUserResponse(1L);

        Mockito.when(userService.findById(1L)).thenReturn(user);
        Mockito.when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

        String expected = TestUtil.readStringFromResource("response/user/find-by-id.json");
        String actual = mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(userService, Mockito.times(1)).findById(1L);
        Mockito.verify(userMapper, Mockito.times(1)).userToUserResponse(user);

        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    public void whenFindUserByNotExistsId_returnErrorResponse() throws Exception {
        Mockito.when(userService.findById(100500L)).thenThrow(new EntityNotFoundException("User with ID 100500 not found"));

        String expected = TestUtil.readStringFromResource("response/user/not-found.json");
        String actual = mockMvc.perform(get("/api/user/100500"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(userService, Mockito.times(1)).findById(100500L);

        JsonAssert.assertJsonEquals(expected, actual);
    }

}
