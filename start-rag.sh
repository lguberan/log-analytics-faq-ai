#!/bin/bash
cd rag-python || exit
source .venv/bin/activate
uvicorn main:app --reload --port 8001