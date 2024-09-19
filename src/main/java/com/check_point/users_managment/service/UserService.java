package com.check_point.users_managment.service;



import com.check_point.users_managment.entity.User;
import com.check_point.users_managment.exception.ResourceNotFoundException;
import com.check_point.users_managment.repository.UserRepository;
import com.check_point.users_managment.response.UserResponse;
import com.check_point.users_managment.utils.ConvertUtil;
import com.check_point.users_managment.utils.FileUtil;
import com.check_point.users_managment.utils.PasswordUtil;
import com.check_point.users_managment.watchdog.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final WatchdogFileService watchdogFileService;

    public User addUser(User user, boolean retry) {
        try {
            user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
            String filePath = user.getFilePath();
            User savedUser= userRepository.save(user);
            FileUtil.deleteFile(filePath);
            savedUser.setFilePath(null);
            return savedUser;
        } catch (Exception e) {
            if (retry) {
                UserAction userAction = UserAction.builder().operationType(OperationType.ADD).user(user).build();
                watchdogFileService.appendOperation(userAction);
            }
        }
        return user;
    }


    public ResponseEntity<UserResponse> findUserById(Long userId) throws JsonProcessingException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserResponse userResponse = ConvertUtil.convertObject(user,UserResponse.class);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    public void deleteUserById(Long id, boolean retry) {
        try {
            boolean exist = userRepository.existsById(id);
            if (!exist) {
                throw new ResourceNotFoundException("User not found Id " + id);
            }
            User user = userRepository.findById(id).get();
            userRepository.deleteById(id);
            FileUtil.deleteFile(user.getFilePath());
        } catch (ResourceNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            if (retry) {
                User user = User.builder().id(id).build();
                UserAction userAction = UserAction.builder().operationType(OperationType.DELETE).user(user).build();
                watchdogFileService.appendOperation(userAction);
            }
        }
    }



}
