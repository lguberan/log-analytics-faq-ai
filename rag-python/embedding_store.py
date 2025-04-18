import faiss
import json
import requests
from sentence_transformers import SentenceTransformer

model = SentenceTransformer('all-MiniLM-L6-v2')

# Try to load existing index
try:
    index = faiss.read_index("rag_index.faiss")
    with open("rag_metadata.json", "r") as f:
        metadata = json.load(f)
except Exception:
    index = None
    metadata = []


def search_similar_questions(query: str, top_k: int = 3):
    if index is None or not metadata:
        return []
    query_embedding = model.encode([query])
    distances, indices = index.search(query_embedding, top_k)

    results = []
    for i in indices[0]:
        if 0 <= i < len(metadata):
            results.append(metadata[i])
    return results


def build_and_save_index():
    API_URL = "http://localhost:8080/api/admin/questions?validated=true"
    response = requests.get(API_URL, auth=("admin", "secret"))
    questions = response.json()

    texts = [q["text"] for q in questions]
    answers = [q["answer"] for q in questions]

    if not texts:
        print("⚠️ No validated questions to index.")
        return

    embeddings = model.encode(texts)

    new_index = faiss.IndexFlatL2(embeddings.shape[1])
    new_index.add(embeddings)

    faiss.write_index(new_index, "rag_index.faiss")

    with open("rag_metadata.json", "w") as f:
        json.dump(
            [{"text": q, "answer": a} for q, a in zip(texts, answers)],
            f,
            indent=2
        )

    print(f"✅ FAISS index rebuilt with {len(texts)} entries.")
