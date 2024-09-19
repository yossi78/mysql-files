package com.check_point.users_managment.watchdog;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public enum OperationType {

    ADD,
    DELETE
}
