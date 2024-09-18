package com.check_point.users_managment.service;



import com.check_point.users_managment.entity.User;
import com.check_point.users_managment.exception.ResourceNotFoundException;
import com.check_point.users_managment.repository.UserRepository;
import com.check_point.users_managment.utils.PasswordUtil;
import com.check_point.users_managment.watchdog.AddWatchdogOperation;
import com.check_point.users_managment.watchdog.UpdateWatchdogOperation;
import com.check_point.users_managment.watchdog.WatchdogFileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final WatchdogFileService watchdogFileService;

    public User addUser(User user, boolean retry) {
        try {
            user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
            return userRepository.save(user);
        } catch (Exception e) {
            if (retry) {
                watchdogFileService.appendOperation(new AddWatchdogOperation(user));
            }
        }
        return user;
    }


    public ResponseEntity<User> findUserById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    public void deleteUserById(Long id, boolean retry) {
        boolean exist = userRepository.existsById(id);
        if (!exist) {
            throw new ResourceNotFoundException("User not found Id " + id);
        }
        userRepository.deleteById(id);
    }

    //    public User findFirstNameAndEmail(String firstname,String email){
//        return userRepository.findByFirstNameAndEmail(firstname,email);
//    }
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
