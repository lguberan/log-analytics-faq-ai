from fastapi import FastAPI, Body
from pydantic import BaseModel
from typing import List

from embedding_store import search_similar_questions, build_and_save_index

app = FastAPI()


class RAGRequest(BaseModel):
    question: str


class RAGResponse(BaseModel):
    snippets: List[str]


@app.post("/rag/query", response_model=RAGResponse)
async def rag_query(
        req: RAGRequest = Body(
            ...,
            examples={
                "Kafka": {
                    "summary": "Ask about Kafka",
                    "value": {
                        "question": "How is Kafka used in this project?"
                    }
                },
                "OpenAI": {
                    "summary": "Ask about OpenAI",
                    "value": {
                        "question": "How does OpenAI integrate with the FAQ system?"
                    }
                }
            }
        )
):
    results = search_similar_questions(req.question)
    return RAGResponse(
        snippets=[r["answer"] for r in results]
    )


@app.post("/rag/rebuild")
def rebuild_index():
    print("📡 Rebuild endpoint hit!")
    build_and_save_index()
    return {"message": "RAG index rebuilt successfully."}
