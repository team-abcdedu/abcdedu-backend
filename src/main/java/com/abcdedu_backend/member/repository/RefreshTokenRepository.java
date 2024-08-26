package com.abcdedu_backend.member.repository;

import com.abcdedu_backend.member.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
