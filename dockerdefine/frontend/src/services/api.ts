import axios from 'axios';
import { AuthRequest, User } from '../types/user';

const API_URL = 'http://localhost:8080/api/users';

export const registerUser = async (user: User, roleIds: number[]) => {
  return axios.post(`${API_URL}/register?roleIds=${roleIds.join(',')}`, user);
};

export const loginUser = async (authRequest: AuthRequest) => {
  return axios.post(`${API_URL}/generateToken`, authRequest);
};

export const getAllUsers = async (token: string) => {
  return axios.get(`${API_URL}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
};

export const getUserById = async (id: number, token: string) => {
  return axios.get(`${API_URL}/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
};