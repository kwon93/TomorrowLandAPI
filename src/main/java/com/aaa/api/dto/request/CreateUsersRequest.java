package com.aaa.api.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateUsersRequest {

    @Email(message = "올바른 E-Mail 형식을 작성해주세요. ex) abc123@gmail.com ")
    @NotBlank(message = "E-Mail을 작성해주세요.")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{6,12}$", message = "비밀번호는 영어 소문자와 숫자의 조합으로 6글자 이상 12글자 미만이어야 합니다.")
    private String password;
    @Size(max = 10, message = "당신이 '황금독수리 온 세상을 놀라게하다' 님이 아니시라면 이름은 10글자 미만으로 입력해주세요.")
    private String name;

    @Builder
    public CreateUsersRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
