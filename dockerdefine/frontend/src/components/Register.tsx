import React, { useState } from 'react';
import { Form, Button, Container, Alert, Card, Col, Row } from 'react-bootstrap';
import { registerUser } from '../services/api';
import { User } from '../types/user';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './Register.css';

const Register: React.FC = () => {
  const [user, setUser] = useState<Partial<User>>({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    roles: [],
  });
  const [selectedRole, setSelectedRole] = useState<string>('USER');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const roleIds = selectedRole === 'USER' ? [1] : [2];
      await registerUser(user as User, roleIds);
      navigate('/login');
    } catch (err) {
      setError('Registration failed. Please check your input.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center min-vh-100">
      <Row className="w-100">
        <Col md={6} lg={4} className="mx-auto">
          <Card className="shadow-lg border-0">
            <Card.Body className="p-4">
              <h2 className="text-center mb-4" style={{ color: '#333', fontWeight: 'bold' }}>
                Register
              </h2>
              {error && (
                <Alert variant="danger" className="text-center">
                  {error}
                </Alert>
              )}
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="firstName">
                  <Form.Label>First Name</Form.Label>
                  <Form.Control
                    type="text"
                    value={user.firstName}
                    onChange={(e) => setUser({ ...user, firstName: e.target.value })}
                    placeholder="Enter your first name"
                    required
                    className="rounded-pill"
                  />
                </Form.Group>

                <Form.Group className="mb-3" controlId="lastName">
                  <Form.Label>Last Name</Form.Label>
                  <Form.Control
                    type="text"
                    value={user.lastName}
                    onChange={(e) => setUser({ ...user, lastName: e.target.value })}
                    placeholder="Enter your last name"
                    required
                    className="rounded-pill"
                  />
                </Form.Group>

                <Form.Group className="mb-3" controlId="email">
                  <Form.Label>Email</Form.Label>
                  <Form.Control
                    type="email"
                    value={user.email}
                    onChange={(e) => setUser({ ...user, email: e.target.value })}
                    placeholder="Enter your email"
                    required
                    className="rounded-pill"
                  />
                </Form.Group>

                <Form.Group className="mb-3" controlId="password">
                  <Form.Label>Password</Form.Label>
                  <Form.Control
                    type="password"
                    value={user.password}
                    onChange={(e) => setUser({ ...user, password: e.target.value })}
                    placeholder="Enter your password"
                    required
                    className="rounded-pill"
                  />
                </Form.Group>

                <Form.Group className="mb-4" controlId="role">
                  <Form.Label>Select Role</Form.Label>
                  <div className="d-flex justify-content-around">
                    <Form.Check
                      type="radio"
                      label="User"
                      name="role"
                      value="USER"
                      checked={selectedRole === 'USER'}
                      onChange={(e) => setSelectedRole(e.target.value)}
                      className="custom-radio"
                    />
                    <Form.Check
                      type="radio"
                      label="Admin"
                      name="role"
                      value="ADMIN"
                      checked={selectedRole === 'ADMIN'}
                      onChange={(e) => setSelectedRole(e.target.value)}
                      className="custom-radio"
                    />
                  </div>
                </Form.Group>

                <Button
                  variant="primary"
                  type="submit"
                  disabled={loading}
                  className="w-100 rounded-pill py-2"
                  style={{ backgroundColor: '#007bff', borderColor: '#007bff', fontWeight: 'bold' }}
                >
                  {loading ? 'Registering...' : 'Register'}
                </Button>
              </Form>
              <div className="text-center mt-3">
                <small>
                  Already have an account?{' '}
                  <a href="/login" style={{ color: '#007bff', textDecoration: 'none' }}>
                    Login here
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

export default Register;