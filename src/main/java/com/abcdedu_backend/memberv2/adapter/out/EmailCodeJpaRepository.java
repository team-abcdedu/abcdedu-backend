package com.abcdedu_backend.memberv2.adapter.out;

import com.abcdedu_backend.memberv2.adapter.out.entity.EmailCodeEntity;
import org.springframework.data.repository.CrudRepository;

public interface EmailCodeJpaRepository extends CrudRepository<EmailCodeEntity, String> {
}
