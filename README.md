# log-analytics-faq-ai

An intelligent FAQ assistant for my Kafka + Spring Boot log analytics project. Uses RAG (Retrieval-Augmented Generation)
with OpenAI GPT to generate answers, and supports human validation and continuous improvement of the knowledge base.

## 🔧 Startup Instructions

0.Make sure you’ve run this once to set up the Python environment:

```bash
cd rag-python
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
cd ..
```

1. Start the Spring Boot backend:

```bash
mvn spring-boot:run
```

2. Start the RAG Python microservice (from the root of the project):

```bash
./start-rag.sh
```
