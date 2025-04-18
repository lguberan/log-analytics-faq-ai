import { useEffect, useState } from 'react';
import axios from 'axios';
import {
  Box,
  Text,
  Heading,
  VStack,
  Button,
  Textarea,
  useToast,
} from '@chakra-ui/react';

type Question = {
  id: number;
  text: string;
  answer: string;
};

const AdminPage = () => {
  const [questions, setQuestions] = useState<Question[]>([]);
  const [editedAnswers, setEditedAnswers] = useState<{ [id: number]: string }>({});
  const toast = useToast();

  useEffect(() => {
    axios.get('/api/admin/questions?validated=false')
      .then(res => setQuestions(res.data))
      .catch(() => toast({ title: "Error fetching questions", status: "error" }));
  }, []);

  const validateAnswer = (id: number) => {
    axios.post(`/api/admin/questions/${id}/validate`, {
      correctedAnswer: editedAnswers[id] || ""
    }).then(() => {
      setQuestions(prev => prev.filter(q => q.id !== id));
      toast({ title: "Answer validated", status: "success" });
    }).catch(() => toast({ title: "Error validating", status: "error" }));
  };

  const deleteQuestion = (id: number) => {
    axios.delete(`/api/admin/questions/${id}`)
      .then(() => {
        setQuestions(prev => prev.filter(q => q.id !== id));
        toast({ title: "Question deleted", status: "info" });
      })
      .catch(() => toast({ title: "Error deleting question", status: "error" }));
  };

  return (
    <Box>
      <Heading mb={4}>Admin Panel</Heading>
      <VStack spacing={6} align="stretch">
        {questions.map((q) => (
          <Box key={q.id} p={4} borderWidth="1px" borderRadius="md">
            <Text fontWeight="bold">Q: {q.text}</Text>
            <Textarea
              mt={2}
              defaultValue={q.answer}
              onChange={(e) => setEditedAnswers({ ...editedAnswers, [q.id]: e.target.value })}
            />
            <Button mt={2} colorScheme="green" onClick={() => validateAnswer(q.id)}>
              Validate
            </Button>
            <Button mt={2} ml={2} colorScheme="gray" onClick={() => deleteQuestion(q.id)}>
              Delete
            </Button>
          </Box>
        ))}
        {questions.length === 0 && <Text>No unvalidated questions ✨</Text>}
      </VStack>
    </Box>
  );
};

export default AdminPage;