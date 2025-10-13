package com.puzzlix.solid_task.domain.user;

import com.puzzlix.solid_task._global.config.jwt.JwtTokenProvider;
import com.puzzlix.solid_task._global.dto.CommonResponseDto;
import com.puzzlix.solid_task.domain.user.dto.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto<?>> signUp(@Valid @RequestBody UserRequest.SignUp request) {

        userService.signUp(request);
        return ResponseEntity.ok(CommonResponseDto.success(null, "회원가입이 완료되었습니다"));
    }

    // 자원 요청 -> GET
    // 자원 생성 --> POST
    // 자우너 수정 --> PUT, Patch
    // 자원 삭제 --> DELETE
    // 보안상 중요해서 GET이 아니라 POST를 사용한다
    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<?>> login(@Valid @RequestBody UserRequest.Login request) {
        User user = userService.login(request);
        // 사용자 이메일을 기반으로 JWT토큰 생성
        String token = jwtTokenProvider.createToken(user.getEmail());  // 토큰에 진짜 중요한 정보를 넣으면 안됨(해봣짜 이메일 정도?)

        return ResponseEntity.ok(CommonResponseDto.success(null, "로그인에 성공했습니다"));
    }


}
