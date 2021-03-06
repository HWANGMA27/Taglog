package toyproject.taglog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.dto.UserDTO;
import toyproject.taglog.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public List<UserDTO> findAllUser() {
        return userRepository.findAll()
                .stream()
                .map(user -> UserDTO.builder().userId(user.getId())
                        .name(user.getName())
                        .picture(user.getPicture())
                        .role(user.getRole())
                        .build())
                .collect(Collectors.toList());
    }
}
