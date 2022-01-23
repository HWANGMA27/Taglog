package toyproject.taglog.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.taglog.dto.UserDTO;
import toyproject.taglog.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@ApiOperation(value = "User API")
public class UserContoroller {

    private final UserService userService;
    @Operation(summary = "유저 전체 조회", description = "전체 유저를 조회합니다.")
    @GetMapping("/all")
    public List<UserDTO> findAllUser(){
        return userService.findAllUser();
    }
}
