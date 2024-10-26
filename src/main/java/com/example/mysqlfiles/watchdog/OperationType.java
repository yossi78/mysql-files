package com.example.mysqlfiles.watchdog;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public enum OperationType {

    ADD,
    DELETE
}
