package com.check_point.users_managment.watchdog;


import com.check_point.users_managment.entity.User;
import com.check_point.users_managment.service.UserService;
import com.check_point.users_managment.utils.FileUtil;
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
                    User user = userService.addUser(userAction.getUser(),false);
                    if(user.getId()!=null){
                        FileUtil.deleteFile(user.getFilePath());
                    }
                    break;
                case DELETE:
                    Boolean result = userService.deleteUserById(userAction.getUser().getId(),false);
                    if(result){
                        FileUtil.deleteFile(userAction.getUser().getFilePath());
                    }
                    break;
            }
        }


    }
}
