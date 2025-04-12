import React, { useState, useEffect } from 'react';
import { Container, Card, Alert, Col, Row } from 'react-bootstrap';
import { getUserById } from '../services/api';
import { User } from '../types/user';
import { useParams, useNavigate } from 'react-router-dom';

const UserDetail: React.FC = () => {
  const [user, setUser] = useState<User | null>(null);
  const [error, setError] = useState<string>('');
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const token = localStorage.getItem('token') || '';

  useEffect(() => {
    const fetchUser = async () => {
      if (!token) {
        navigate('/login');
        return;
      }
      try {
        const response = await getUserById(Number(id), token);
        setUser(response.data);
      } catch (err: any) {
        setError('Failed to load user details.');
        if (err.response?.status === 403) {
          navigate('/login');
        }
      }
    };
    fetchUser();
  }, [id, token, navigate]);

  if (error) return <Container className="mt-5"><Alert variant="danger">{error}</Alert></Container>;
  if (!user) return <Container className="mt-5"><div>Loading...</div></Container>;

  return (
    <Container className="d-flex justify-content-center align-items-center min-vh-100">
      <Row className="w-100">
        <Col md={6} lg={4} className="mx-auto">
          <Card className="shadow-lg border-0">
            <Card.Body className="p-4">
              <h2 className="text-center mb-4" style={{ color: '#333', fontWeight: 'bold' }}>
                User Detail
              </h2>
              <Card.Title className="text-center">{user.firstName} {user.lastName}</Card.Title>
              <Card.Text className="text-center">Email: {user.email}</Card.Text>
              <Card.Text className="text-center">Roles: {user.roles.map((role) => role.name).join(', ')}</Card.Text>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default UserDetail;