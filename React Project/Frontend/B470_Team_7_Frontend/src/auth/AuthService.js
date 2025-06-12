import axios from "axios";
import { API_BASE_URL } from '../api';

const API_URL = `${API_BASE_URL}/auth`;

const AuthService = {
  register: async (email, password, role, username) => {
    try {
      const response = await axios.post(`${API_URL}/register`, { email, password, role, username });
      return response.data;
    } catch (error) {
      console.error("Registration error:", error.response?.data || error.message);
      throw error;
    }
  },

  registerAdmin: async (email, username, password) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/admin/register`, { email, username, password }, {
        headers: AuthService.getAuthHeader(),
      });
      return response.data;
    } catch (error) {
      console.error("Admin registration error:", error.response?.data || error.message);
      throw error;
    }
  },

  login: (token) => {
    if (!token || token.split(".").length !== 3) {
      console.error("Invalid token received:", token);
      return;
    }
    localStorage.setItem("token", token);
  },

  logout: () => {
    localStorage.removeItem("token");
  },

  isAuthenticated: () => {
    const token = localStorage.getItem("token");
    if (!token || token.split(".").length !== 3) return false;

    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      const expiryTime = payload.exp * 1000;
      if (Date.now() >= expiryTime) {
        AuthService.logout();
        return false;
      }
      return true;
    } catch (error) {
      console.error("Invalid token:", error);
      AuthService.logout();
      return false;
    }
  },

  getUserRole: () => {
    const token = localStorage.getItem("token");
    if (!token || token.split(".").length !== 3) return "GUEST";

    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      return payload.role || "GUEST";
    } catch (error) {
      console.error("Error decoding token:", error);
      return "GUEST";
    }
  },

  getToken: () => {
    return localStorage.getItem("token");
  },

  getAuthHeader: () => {
    const token = localStorage.getItem("token");
    if (token && token.split(".").length === 3) {
      const header = { Authorization: `Bearer ${token}` };
      return header;
    } else {
      return {};
    }
  }
};

export default AuthService;