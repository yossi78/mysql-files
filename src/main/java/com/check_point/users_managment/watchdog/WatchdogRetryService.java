package com.check_point.users_managment.watchdog;


import com.check_point.users_managment.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;



@Service
@AllArgsConstructor
@Slf4j
@EnableScheduling
public class WatchdogRetryService {

    private final WatchdogFileService watchdogFileService;

    private final UserService userService;

    @Scheduled(fixedRate = 60000)
    public void performTask() {
        List<UserAction> userActions = watchdogFileService.readAllOperations();

        for ( UserAction userAction: userActions)
        {
            switch (userAction.getOperationType()) {
                case ADD:
                    userService.addUser(userAction.getUser(),false);
                    break;
                case DELETE:
                    userService.deleteUserById(userAction.getUser().getId(),false);
                    break;
            }
        }


    }
}
