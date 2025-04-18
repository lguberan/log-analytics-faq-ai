package com.guberan.logfaqai.config;

import com.guberan.logfaqai.model.Question;
import com.guberan.logfaqai.repository.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final QuestionRepository questionRepository;

    public DatabaseSeeder(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public void run(String... args) {
        if (questionRepository.count() == 0) {
            Question q = new Question();
            q.setText("How is Kafka used in this project?");
            q.setAnswer("Kafka is used to stream logs in real time.");
            q.setValidated(true);
            questionRepository.save(q);
            System.out.println("✅ Inserted default question into H2 database.");
        }
    }
}