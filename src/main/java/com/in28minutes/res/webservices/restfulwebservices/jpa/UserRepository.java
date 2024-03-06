package com.in28minutes.res.webservices.restfulwebservices.jpa;

import com.in28minutes.res.webservices.restfulwebservices.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
