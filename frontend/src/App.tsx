import { Box, Button, Flex } from "@chakra-ui/react";
import { Link, Outlet } from "react-router-dom";

const App = () => {
  return (
    <Box p={4}>
      <Flex justify="center" gap={4} mb={8}>
        <Button as={Link} to="/" colorScheme="teal" variant="solid">
          Ask
        </Button>
        <Button as={Link} to="/admin" colorScheme="teal" variant="outline">
          Admin
        </Button>
      </Flex>

      <Outlet />
    </Box>
  );
};

export default App;