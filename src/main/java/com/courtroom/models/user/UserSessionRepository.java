package com.courtroom.models.user;
import com.courtroom.models.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    List<UserSession> findByUser(User user);

    Optional<UserSession> findByAccessToken(String accessToken);

    Optional<UserSession> findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);

    void deleteByUser(User user);


    @Modifying
    @Query("""
    UPDATE UserSession s
    SET s.accessToken = :accessToken
    WHERE s.id = :sessionId
""")
    int updateAccessToken(@Param("sessionId") String sessionId,
                          @Param("accessToken") String accessToken);

    @Modifying
    @Query("""
    UPDATE UserSession s
    SET s.refreshToken = :refreshToken
    WHERE s.id = :sessionId
""")
    int updateRefreshToken(@Param("sessionId") String sessionId,
                           @Param("refreshToken") String refreshToken);

    @Modifying
    @Query("""
    UPDATE UserSession s
    SET s.accessToken = :accessToken,
        s.refreshToken = :refreshToken
    WHERE s.id = :sessionId
""")
    int updateTokens(@Param("sessionId") String sessionId,
                     @Param("accessToken") String accessToken,
                     @Param("refreshToken") String refreshToken);

    @Modifying
    @Query("""
    UPDATE UserSession s
    SET s.accessToken = null,
        s.refreshToken = null
    WHERE s.id = :sessionId
""")
    int clearTokens(@Param("sessionId") String sessionId);

    @Modifying
    @Query("""
    UPDATE UserSession s
    SET s.accessToken = null
    WHERE s.id = :sessionId
""")
    int clearAccessToken(@Param("sessionId") String sessionId);

    @Modifying
    @Query("""
    UPDATE UserSession s
    SET s.refreshToken = null
    WHERE s.id = :sessionId
""")
    int clearRefreshToken(@Param("sessionId") String sessionId);
}