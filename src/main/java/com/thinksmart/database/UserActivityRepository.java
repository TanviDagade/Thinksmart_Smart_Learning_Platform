package com.thinksmart.database;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserActivityRepository extends JpaRepository<UserActivity,Integer> {

    List<UserActivity> findByEmail(String email);
    long countByEmailAndTopic(String email, String topic);

}