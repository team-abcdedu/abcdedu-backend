package com.abcdedu_backend.member.adapter.out;

import com.abcdedu_backend.member.adapter.out.entity.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenJpaRepository extends CrudRepository<RefreshTokenEntity, String> {

}
