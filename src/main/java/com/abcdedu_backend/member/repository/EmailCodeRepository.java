package com.abcdedu_backend.member.repository;

import com.abcdedu_backend.member.entity.EmailCode;
import org.springframework.data.repository.CrudRepository;

public interface EmailCodeRepository extends CrudRepository<EmailCode, String> {
}
