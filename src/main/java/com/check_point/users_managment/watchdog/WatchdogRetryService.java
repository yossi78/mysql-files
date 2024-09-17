package com.check_point.users_managment.watchdog;


import com.check_point.users_managment.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;



@Service
@AllArgsConstructor
@Slf4j
public class WatchdogRetryService {

    private final WatchdogFileService watchdogFileService;

    private final UserService userService;

    @Scheduled(fixedRate = 60000)
    public void performTask() {
        List<WatchdogOperation> watchdogOperations = watchdogFileService.readAllOperations();

        for ( WatchdogOperation watchdogOperation: watchdogOperations)
        {
            switch (watchdogOperation.operationType()) {
                case ADD:
                    userService.addUser(watchdogOperation.user(),false);
                    break;
                case DELETE:
                    userService.deleteUserById(watchdogOperation.user().getId(),false);
                    break;
            }
        }


    }
}
