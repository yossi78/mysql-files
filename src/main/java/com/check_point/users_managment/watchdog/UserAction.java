package com.check_point.users_managment.watchdog;


import com.check_point.users_managment.entity.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonSerialize
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAction {
    private OperationType operationType;
    private User user;
}
