package toyproject.taglog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.taglog.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    //기존 유저인지 신규인지 판단하는 메소드
    Optional<User> findByEmail(String email);
}
