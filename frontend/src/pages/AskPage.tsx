import { useState, useEffect } from "react";
import {
  Box,
  Button,
  Flex,
  Heading,
  Text,
  Textarea,
  UnorderedList,
  ListItem,
  Image,
} from "@chakra-ui/react";
import axios from "axios";

interface AskResponse {
  question: string;
  answer: string;
  validated: boolean;
  snippets?: string[];
}

interface Question {
  id: number;
  text: string;
  answer: string;
}

const AskPage = () => {
  const [question, setQuestion] = useState("");
  const [askResponse, setAskResponse] = useState<AskResponse | null>(null);
  const [recentQuestions, setRecentQuestions] = useState<Question[]>([]);

  const handleAsk = async () => {
    try {
      const response = await axios.post<AskResponse>("/api/faq/ask", {
        question,
      });
      setAskResponse(response.data);
    } catch (error) {
      console.error("Failed to get AI response", error);
    }
  };

  useEffect(() => {
    axios
      .get<Question[]>("/api/admin/questions?validated=true")
      .then((res) => setRecentQuestions(res.data.slice(-20).reverse()))
      .catch((err) => console.error("Failed to fetch recent questions", err));
  }, []);

  return (
    <Flex direction="column" align="center" mt={10}>
      <Box width="100%" maxW="800px" p={4} textAlign="left">
        <Flex align="center" mb={6}>
          <Image
            src="/ai-faq-image.png"
            alt="AI Assistant"
            boxSize="60px"
            mr={4}
          />

          <Box>
            <Heading as="h2" size="lg">
              AI-assisted FAQ
            </Heading>
            <Text fontSize="md" color="gray.600">
              for the Real-Time Log Analytics Project
            </Text>
          </Box>
        </Flex>

        <Textarea
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          placeholder="Type your question here..."
          size="lg"
          height="120px"
          mb={4}
          fontSize="lg"
        />
        <Button
          colorScheme="blue"
          onClick={handleAsk}
          isDisabled={!question.trim()}
          width="fit-content"
        >
          Submit
        </Button>
      </Box>

      {askResponse && (
        <Box mt={10} width="100%" maxW="780px" p={4} borderWidth={1} borderRadius="md">
          <Heading as="h3" size="md" mb={2}>
            💬 AI Answer
          </Heading>
          <Text fontSize="lg" mb={2}>{askResponse.answer}</Text>

          {askResponse.snippets && askResponse.snippets.length > 0 && (
            <>
              <Text fontSize="sm" color="gray.500" mb={1}>
                Context snippets:
              </Text>
              <UnorderedList>
                {askResponse.snippets.map((s, i) => (
                  <ListItem key={i}>{s}</ListItem>
                ))}
              </UnorderedList>
            </>
          )}
        </Box>
      )}

      <Box mt={10} width="100%" maxW="700px" p={4}>
        <Heading as="h4" size="md" mb={2}>
          📜 Recent Validated Questions
        </Heading>
        <UnorderedList spacing={3}>
          {recentQuestions.map((q) => (
            <ListItem key={q.id}>
              <Text fontWeight="bold">{q.text}</Text>
              <Text fontSize="sm" color="gray.600" ml={4}>
                {q.answer}
              </Text>
            </ListItem>
          ))}
        </UnorderedList>
      </Box>
    </Flex>
  );
};

export default AskPage;