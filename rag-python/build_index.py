import faiss
import json
import requests
from sentence_transformers import SentenceTransformer

# REST API endpoint for validated questions
API_URL = "http://localhost:8080/api/admin/questions?validated=true"
AUTH = ("ragbuilder", "ragpass")

# Call Spring Boot endpoint
response = requests.get(API_URL, auth=AUTH)
print("🔍 Response status:", response.status_code)
print("🔍 Response body:")
print(response.text)
data = response.json()

questions = [item["text"] for item in data]
answers = [item["answer"] for item in data]

print(f"📊 {len(questions)} validated questions found via REST")

# Generate embeddings
model = SentenceTransformer("all-MiniLM-L6-v2")
embeddings = model.encode(questions)

# Create FAISS index
dimension = embeddings[0].shape[0]
index = faiss.IndexFlatL2(dimension)
index.add(embeddings)

# Save index
faiss.write_index(index, "rag_index.faiss")

# Save metadata
metadata = [{"question": q, "answer": a} for q, a in zip(questions, answers)]
with open("rag_metadata.json", "w") as f:
    json.dump(metadata, f)

print("✅ FAISS index built and saved.")
