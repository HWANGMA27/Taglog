package toyproject.taglog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.entity.User;
import toyproject.taglog.exception.invalid.InvalidateUserException;
import toyproject.taglog.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public User findUserByid(Long userId) {
        return userRepository.findById(userId).orElseThrow(InvalidateUserException::new);
    }
}
