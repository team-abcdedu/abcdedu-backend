package com.abcdedu_backend.homework.repository;

import com.abcdedu_backend.homework.entity.HomeworkRepresentative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HomeworkRepresentativeRepository extends JpaRepository<HomeworkRepresentative, Long> {

    @Query("SELECT r FROM HomeworkRepresentative r WHERE r.id = (SELECT MAX(r2.id) FROM HomeworkRepresentative r2)")
    Optional<HomeworkRepresentative> findLatestRepresentative();

}
