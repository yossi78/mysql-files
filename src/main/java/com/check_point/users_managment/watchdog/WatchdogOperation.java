package com.check_point.users_managment.watchdog;


import com.check_point.users_managment.entity.User;

public interface WatchdogOperation {
    OperationType operationType();

    User user();
}
