package com.check_point.users_managment.repository;



import com.check_point.users_managment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
