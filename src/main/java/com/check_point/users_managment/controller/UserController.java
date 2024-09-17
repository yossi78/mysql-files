package com.check_point.users_managment.controller;

import com.check_point.users_managment.entity.User;
import com.check_point.users_managment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
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

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") Long id) {
        return userService.findUserById(id);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> e = userService.getAllUsers();
        return ResponseEntity.ok(e);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        userService.deleteUserById(id,true);
        return ResponseEntity.ok("User Deleted Successfully");
    }


}
