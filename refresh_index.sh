#!/bin/bash

echo "⏳ Waiting for Spring Boot to start and insert default data..."
sleep 2

echo "🔁 Building FAISS index from H2 database..."
python3 rag-python/build_index.py
