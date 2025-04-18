#!/bin/bash

echo "⏳ Waiting for Spring Boot to start and insert default data..."
sleep 2

echo "🔁 Building FAISS index from H2 database..."
cd rag-python
source .venv/bin/activate
python build_index.py
cd ..

