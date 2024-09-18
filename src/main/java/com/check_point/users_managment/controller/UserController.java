package com.check_point.users_managment.controller;

import com.check_point.users_managment.entity.User;
import com.check_point.users_managment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.retry.annotation.Recover;




@RestController
@RequestMapping(path = "/user")
@EnableRetry
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User addedUser = userService.addUser(user,true);
        return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
    }

    @Retryable(value = { Exception.class },maxAttempts = 5,backoff = @Backoff(delay = 5000))
    @GetMapping(path = "/{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") Long id) {
        ResponseEntity<User> userEntity=null;
        try {
            userEntity = userService.findUserById(id);
        } catch (Exception e) {
            System.out.println("Failed to get user from DB - try again");
            throw e;
        }
        return userEntity;
    }


    @GetMapping
    @Retryable(value = { Exception.class }, maxAttempts = 5, backoff = @Backoff(delay = 5000))
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            System.out.println("Failed to get users from DB - try again");
            throw e;
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        try {
            userService.deleteUserById(id, true);
            return ResponseEntity.ok("User Deleted Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting the user: " + e.getMessage());
        }
    }



    @Recover
    public void recover(Exception e){
        System.out.println("Failed to fetch from DB after several attempts" +e +  e.getMessage());
    }


}
