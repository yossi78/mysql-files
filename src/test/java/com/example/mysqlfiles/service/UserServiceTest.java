package com.example.mysqlfiles.service;
import com.example.mysqlfiles.entity.User;
import com.example.mysqlfiles.repository.UserRepository;
import com.example.mysqlfiles.response.UserResponse;
import com.example.mysqlfiles.watchdog.UserAction;
import com.example.mysqlfiles.watchdog.WatchdogFileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WatchdogFileService watchdogFileService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser_Success() {
        User user = new User();
        user.setPassword("password123");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.addUser(user, false);

        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
        verify(watchdogFileService, never()).appendOperation(any(UserAction.class));
    }

    @Test
    public void testAddUser_WithRetry() {
        User user = new User();
        user.setPassword("password123");

        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException());

        User result = userService.addUser(user, true);

        assertNotNull(result);
        verify(watchdogFileService, times(1)).appendOperation(any(UserAction.class));
    }



    @Test
    public void testFindUserById_NotFound() throws JsonProcessingException {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<UserResponse> response = userService.findUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetAllUsers() {
        userService.getAllUsers();
        verify(userRepository, times(1)).findAll();
    }



    @Test
    public void testDeleteUserById_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        Boolean result = userService.deleteUserById(1L, false);

        assertFalse(result);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDeleteUserById_WithRetry() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException()).when(userRepository).deleteById(1L);

        Boolean result = userService.deleteUserById(1L, true);

        assertTrue(result);
        verify(watchdogFileService, times(1)).appendOperation(any(UserAction.class));
    }
}
