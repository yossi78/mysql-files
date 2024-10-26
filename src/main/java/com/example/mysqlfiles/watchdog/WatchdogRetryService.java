package com.example.mysqlfiles.watchdog;
import com.example.mysqlfiles.entity.User;
import com.example.mysqlfiles.service.UserService;
import com.example.mysqlfiles.utils.FileUtil;
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
                    log.info("Add new user from file ");
                    User user = userService.addUser(userAction.getUser(),false);
                    if(user.getId()!=null){
                        FileUtil.deleteFile(user.getFilePath());
                    }
                    break;
                case DELETE:
                    log.info("Delete user from file ");
                    Boolean result = userService.deleteUserById(userAction.getUser().getId(),false);
                    if(result){
                        FileUtil.deleteFile(userAction.getUser().getFilePath());
                    }
                    break;
            }
        }


    }
}
