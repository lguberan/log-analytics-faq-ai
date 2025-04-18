import faiss
import json
from sentence_transformers import SentenceTransformer

# load model and index
model = SentenceTransformer('all-MiniLM-L6-v2')
index = faiss.read_index("rag_index.faiss")

# load metadata
with open("rag_metadata.json", "r") as f:
    metadata = json.load(f)


def search_similar_questions(query: str, top_k: int = 3):
    query_embedding = model.encode([query])
    distances, indices = index.search(query_embedding, top_k)

    results = []
    for i in indices[0]:
        if 0 <= i < len(metadata):
            results.append(metadata[i])
    return results
