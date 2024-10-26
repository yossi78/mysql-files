package com.example.mysqlfiles.service;
import com.example.mysqlfiles.entity.User;
import com.example.mysqlfiles.exception.ResourceNotFoundException;
import com.example.mysqlfiles.repository.UserRepository;
import com.example.mysqlfiles.response.UserResponse;
import com.example.mysqlfiles.utils.ConvertUtil;
import com.example.mysqlfiles.utils.FileUtil;
import com.example.mysqlfiles.utils.PasswordUtil;
import com.example.mysqlfiles.watchdog.OperationType;
import com.example.mysqlfiles.watchdog.UserAction;
import com.example.mysqlfiles.watchdog.WatchdogFileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
            UserResponse userResponse = ConvertUtil.convertObject(user, UserResponse.class);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            log.info("User has not been found id:"+userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    public Boolean deleteUserById(Long id, boolean retry) {
        try {
            boolean exist = userRepository.existsById(id);
            if (!exist) {
                throw new ResourceNotFoundException("User not found Id " + id);
            }
            userRepository.deleteById(id);
            return true;
        } catch (ResourceNotFoundException e) {
            log.error("User not been found: " + e.getMessage());
            return false;
        } catch (Exception e) {
            if (retry) {
                User user = User.builder().id(id).build();
                UserAction userAction = UserAction.builder().operationType(OperationType.DELETE).user(user).build();
                watchdogFileService.appendOperation(userAction);
                return true;
            }
            return false;
        }
    }



}
