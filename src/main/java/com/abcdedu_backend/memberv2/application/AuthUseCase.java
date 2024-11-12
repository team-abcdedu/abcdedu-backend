package com.abcdedu_backend.memberv2.application;

import com.abcdedu_backend.memberv2.application.dto.command.SignupCommand;
import com.abcdedu_backend.memberv2.application.domain.LoginToken;
public interface AuthUseCase {

    void signUp(SignupCommand command);

    LoginToken login(String email, String password);

    String reissue(String refreshToken);

    void logout(String refreshToken);
}
