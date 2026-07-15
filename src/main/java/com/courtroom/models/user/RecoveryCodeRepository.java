package com.courtroom.models.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RecoveryCodeRepository extends JpaRepository<RecoveryCode, String> {

    /**
     * Returns all unused recovery codes for a user.
     */
    List<RecoveryCode> findByUserAndUsedFalse(User user);

    /**
     * Returns all recovery codes for a user.
     */
    List<RecoveryCode> findByUser(User user);

    /**
     * Deletes all recovery codes for a user.
     */
    void deleteByUser(User user);
}