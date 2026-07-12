package com.courtroom.models.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("""
        UPDATE User u
        SET u.accessToken = :accessToken
        WHERE u.id = :id
    """)
    int updateAccessToken(@Param("id") String id,
                          @Param("accessToken") String accessToken);

    @Modifying
    @Query("""
        UPDATE User u
        SET u.refreshToken = :refreshToken
        WHERE u.id = :id
    """)
    int updateRefreshToken(@Param("id") String id,
                           @Param("refreshToken") String refreshToken);

    @Modifying
    @Query("""
        UPDATE User u
        SET u.accessToken = :accessToken,
            u.refreshToken = :refreshToken
        WHERE u.id = :id
    """)
    int updateTokens(@Param("id") String id,
                     @Param("accessToken") String accessToken,
                     @Param("refreshToken") String refreshToken);

    @Modifying
    @Query("""
        UPDATE User u
        SET u.accessToken = null,
            u.refreshToken = null
        WHERE u.id = :id
    """)
    int clearTokens(@Param("id") String id);
}