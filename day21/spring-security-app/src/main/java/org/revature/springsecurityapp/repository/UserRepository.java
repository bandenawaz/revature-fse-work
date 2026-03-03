package org.revature.springsecurityapp.repository;

import org.revature.springsecurityapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //Sprig Data JPA Automatically generaates this SQL Query
    //Select * from users where username = ?
    Optional<User> findByUsername(String username);
}
