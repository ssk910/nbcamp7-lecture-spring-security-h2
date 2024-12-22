package com.sparta.nbcamp7lecturespringsecurityh2.repository;

import com.sparta.nbcamp7lecturespringsecurityh2.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * create on 2024. 12. 22. create by IntelliJ IDEA.
 *
 * <p> RefreshToken 엔티티를 위한 repository. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByToken(String token);

  Optional<RefreshToken> findByMember_Id(Long memberId);
}
