package com.guberan.logfaqai.repository;

import com.guberan.logfaqai.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findTopByTextIgnoreCaseAndValidatedIsTrue(String text);

    List<Question> findByValidated(boolean validated);
}