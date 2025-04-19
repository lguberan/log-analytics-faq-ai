package com.guberan.logfaqai.config;

import com.guberan.logfaqai.model.Question;
import com.guberan.logfaqai.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final QuestionRepository questionRepository;

    public DatabaseSeeder(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public void run(String... args) {
        if (questionRepository.count() == 0) {
            questionRepository.save(new Question("What is Kafka used for in this project?", "Kafka is used to stream log events in real time from various services.", true, false));
            questionRepository.save(new Question("How are logs stored and visualized?", "Logs are stored in Elasticsearch and visualized in Kibana dashboards.", true, false));
            questionRepository.save(new Question("What role does Spring Boot play in the architecture?", "Spring Boot handles the REST API and processes log data before sending it to Kafka.", true, false));
            questionRepository.save(new Question("How does the AI assistant work?", "The assistant uses validated questions and answers as context to provide relevant responses via OpenAI.", true, false));
            log.info("✅ Inserted default question into H2 database.");
        }
    }
}