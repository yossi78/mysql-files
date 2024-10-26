package com.example.mysqlfiles.repository;


import com.example.mysqlfiles.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
