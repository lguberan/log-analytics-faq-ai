import { useState } from 'react';
import axios from 'axios';
import { Box, Input, Button, Textarea, VStack, Heading } from '@chakra-ui/react';

export const AskForm = () => {
  const [question, setQuestion] = useState('');
  const [answer, setAnswer] = useState('');
  const [snippets, setSnippets] = useState<string[]>([]);

  const ask = async () => {
    const response = await axios.post('http://localhost:8080/api/faq/ask', { question });
    setAnswer(response.data.answer);
    setSnippets(response.data.snippets || []);
  };

  return (
    <Box p={8}>
      <Heading mb={4}>Ask the AI about your log platform</Heading>
      <VStack spacing={4}>
        <Input
          placeholder="Ask your question..."
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
        />
        <Button onClick={ask}>Submit</Button>
        <Textarea readOnly value={answer} placeholder="AI Response..." />
        {snippets.length > 0 && (
          <Box>
            <Heading size="sm" mt={4}>Context Snippets:</Heading>
            <ul>
              {snippets.map((s, i) => <li key={i}>• {s}</li>)}
            </ul>
          </Box>
        )}
      </VStack>
    </Box>
  );