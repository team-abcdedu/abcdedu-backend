package com.abcdedu_backend.member.application;

import com.abcdedu_backend.member.application.dto.command.SignupCommand;
import com.abcdedu_backend.member.domain.LoginToken;
public interface AuthUseCase {

    void signUp(SignupCommand command);

    LoginToken login(String email, String password);

    String reissue(String refreshToken);

    void logout(String refreshToken);
}
