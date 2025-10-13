package com.puzzlix.solid_task.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class UserRequest {

    // 회원 가입 DTO
    // UserRequest.SignUp a = new UserRequest.SignUp()
    @Getter
    @Setter
    public static class SignUp {
        @NotEmpty
        @Size(min = 2, max = 20)
        private String name;

        @NotEmpty
        @Email  // 정규 표현식 필요 없음
        private String email;

        @NotEmpty
        @Size(min = 2, max = 20)
        private String password;
    }


    // 로그인 DTO
    @Getter
    @Setter
    public static class Login {
        @NotEmpty
        @Email
        private String email;
        @NotEmpty
        private String password;
    }
}
