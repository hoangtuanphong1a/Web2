import React, { useState } from 'react';
import { Form, Button, Container, Alert, Card, Col, Row } from 'react-bootstrap';
import { loginUser } from '../services/api';
import { useNavigate } from 'react-router-dom';
import { AuthRequest } from '../types/user';
import 'bootstrap/dist/css/bootstrap.min.css'; 

const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const authRequest: AuthRequest = { username: email, password };
    try {
      const response = await loginUser(authRequest);
      localStorage.setItem('token', response.data);
      navigate('/users');
    } catch (err) {
      setError('Login failed. Check your credentials.');
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center min-vh-100">
      <Row className="w-100">
        <Col md={6} lg={4} className="mx-auto">
          <Card className="shadow-lg border-0">
            <Card.Body className="p-4">
              <h2 className="text-center mb-4" style={{ color: '#333', fontWeight: 'bold' }}>
                Login
              </h2>
              {error && <Alert variant="danger" className="text-center">{error}</Alert>}
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="email">
                  <Form.Label>Email</Form.Label>
                  <Form.Control
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Enter your email"
                    required
                    className="rounded-pill"
                  />
                </Form.Group>
                <Form.Group className="mb-4" controlId="password">
                  <Form.Label>Password</Form.Label>
                  <Form.Control
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Enter your password"
                    required
                    className="rounded-pill"
                  />
                </Form.Group>
                <Button variant="primary" type="submit" className="w-100 rounded-pill py-2">
                  Login
                </Button>
              </Form>
              <div className="text-center mt-3">
                <small>
                  Don't have an account?{' '}
                  <a href="/register" style={{ color: '#007bff', textDecoration: 'none' }}>
                    Register here
                  </a>
                </small>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Login;
