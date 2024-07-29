package com.aaa.api.controller.dto.request;

import com.aaa.api.service.dto.request.LoginServiceRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequest {

    @Email(message = "올바른 E-Mail 형식을 입력해주세요. ex) abc123@gmail.com ")
    @NotBlank(message = "E-Mail을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{6,12}$", message = "비밀번호는 영어 소문자와 숫자의 조합으로 6글자 이상 12글자 미만이어야 합니다.")
    private String password;

    @Builder
    private LoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public LoginServiceRequest toServiceDto(){
        return LoginServiceRequest.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}
