package com.gmail.dimabah;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<CustomUser,Long> {

    CustomUser findByLogin(String login);
    boolean existsByLogin(String login);



}
