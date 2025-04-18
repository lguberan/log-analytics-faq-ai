#!/bin/bash

echo "🚀 Starting backend (Spring Boot)..."
./mvnw spring-boot:run &
BACKEND_PID=$!

echo "🧠 Starting FastAPI (RAG)..."
cd rag-python
source .venv/bin/activate
uvicorn main:app --port 8001 --reload &
FASTAPI_PID=$!
cd ..

echo "🌐 Starting React frontend..."
cd frontend
npm run dev &
FRONTEND_PID=$!
cd ..

echo "✅ All services started."
echo "🔴 To stop: kill $BACKEND_PID $FASTAPI_PID $FRONTEND_PID"
