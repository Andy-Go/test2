package com.accenture.standard.service.service;

import com.accenture.common.result.EmptyResult;
import com.accenture.common.result.StringResult;
import com.accenture.standard.entity.User;
import com.accenture.standard.result.UserResult;
import com.accenture.standard.service.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

import static com.accenture.common.result.ResultCode.SUCCESS;
import static com.accenture.common.result.ResultCode.USER_NOT_FOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        userService = new UserService();
        initMocks(this);

    }

    @After
    public void tearDown() {
        reset(userRepository);
    }

    @Test
    public void testAddUser() throws Exception {
        User user = User.newBuilder().name("testName").pwd("pwd").build();
        when(userRepository.save(user)).thenReturn(user);
        StringResult result = userService.addUser(user);

        assertThat(result.getCode()).isEqualTo(SUCCESS);
        assertThat(result.getPayload()).isNotEmpty();
    }

    @Test
    public void testDeleteUser() throws Exception {
        userService.deleteUser("id");
    }

    @Test
    public void testUpdateUser() throws Exception {
        String id = UUID.randomUUID().toString();
        User user = User.newBuilder()
                .id(id)
                .name("name")
                .pwd("pwd")
                .build();
        when(userRepository.exists(id)).thenReturn(true);
        EmptyResult result = userService.updateUser(user);
        assertThat(result.getCode()).isEqualTo(SUCCESS);
    }

    @Test
    public void testUpdateUser_UserNotFound() throws Exception {
        String id = UUID.randomUUID().toString();
        User user = User.newBuilder()
                .id(id)
                .name("name")
                .pwd("pwd")
                .build();
        when(userRepository.exists(id)).thenReturn(false);
        EmptyResult result = userService.updateUser(user);
        assertThat(result.getCode()).isEqualTo(USER_NOT_FOUNT);
    }

    @Test
    public void testFindUserById() throws Exception {
        String id = UUID.randomUUID().toString();
        User user = User.newBuilder()
                .id(UUID.randomUUID().toString())
                .name("name")
                .pwd("pwd")
                .build();
        when(userRepository.findOne(id)).thenReturn(user);
        UserResult result = userService.findUserById(id);
        assertThat(result.getPayload()).isNotNull();
    }

    @Test
    public void testFindUserById_NotFound() throws Exception {
        String id = UUID.randomUUID().toString();

        when(userRepository.findOne(id)).thenReturn(null);
        UserResult result = userService.findUserById(id);

        assertThat(result.getCode()).isEqualTo(USER_NOT_FOUNT);
    }
}