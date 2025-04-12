import React, { useState, useEffect } from 'react';
import { Container, Table, Button, Card, Col, Row } from 'react-bootstrap';
import { getAllUsers } from '../services/api';
import { User } from '../types/user';
import { useNavigate } from 'react-router-dom';

const UserList: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUsers = async () => {
      const token = localStorage.getItem('token');
      if (!token) {
        navigate('/login');
        return;
      }
      try {
        const response = await getAllUsers(token);
        setUsers(response.data);
      } catch (err) {
        console.error('Error fetching users:', err);
        navigate('/login');
      }
    };
    fetchUsers();
  }, [navigate]);

  return (
    <Container className="d-flex justify-content-center align-items-center min-vh-100">
      <Row className="w-100">
        <Col md={8} lg={6} className="mx-auto">
          <Card className="shadow-lg border-0">
            <Card.Body className="p-4">
              <h2 className="text-center mb-4" style={{ color: '#333', fontWeight: 'bold' }}>
                User List
              </h2>
              <Table striped bordered hover>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map((user) => (
                    <tr key={user.id}>
                      <td>{user.id}</td>
                      <td>{user.firstName} {user.lastName}</td>
                      <td>{user.email}</td>
                      <td>
                        <Button variant="info" onClick={() => navigate(`/users/${user.id}`)}>
                          Detail
                        </Button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default UserList;