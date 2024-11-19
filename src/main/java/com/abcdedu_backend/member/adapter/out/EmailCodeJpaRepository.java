package com.abcdedu_backend.member.adapter.out;

import com.abcdedu_backend.member.adapter.out.entity.EmailCodeEntity;
import org.springframework.data.repository.CrudRepository;

public interface EmailCodeJpaRepository extends CrudRepository<EmailCodeEntity, String> {
}
