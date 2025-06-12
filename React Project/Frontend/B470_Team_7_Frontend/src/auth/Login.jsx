import { useState, useRef } from "react";
import axios from "axios";
import { useNavigate, Link } from "react-router-dom";
import AuthService from "./AuthService";
import { API_BASE_URL } from '../api';
import "../styles/LoginModal.css";

function Login() {
  const [identifier, setIdentifier] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [errors, setErrors] = useState({ identifier: "", password: "" });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const cardRef = useRef(null);

  // Validate input for whitespace
  const validateInput = (field, value) => {
    const trimmedValue = value.trim();
    // Check for internal whitespace
    if (/\s/.test(trimmedValue)) {
      return `${field} cannot contain spaces.`;
    }
    return "";
  };

  const handleInputChange = (field, value, setValue) => {
    setValue(value);
    const error = validateInput(field, value);
    setErrors((prev) => ({ ...prev, [field.toLowerCase()]: error }));
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setMessage("");
    setLoading(true);

    // Trim inputs
    const trimmedIdentifier = identifier.trim();
    const trimmedPassword = password.trim();

    // Validate inputs
    const identifierError = validateInput("Identifier", trimmedIdentifier);
    const passwordError = validateInput("Password", trimmedPassword);

    if (identifierError || passwordError) {
      setErrors({
        identifier: identifierError,
        password: passwordError,
      });
      setLoading(false);
      return;
    }

    try {
      const response = await axios.post(`${API_BASE_URL}/auth/login`, {
        identifier: trimmedIdentifier,
        password: trimmedPassword,
      });

      const token = response.data;
      if (!token || token.split(".").length !== 3) {
        console.error("Invalid token received:", token);
        throw new Error("Invalid token format");
      }

      AuthService.login(token);
      setMessage("✅ Login successful! Redirecting...");
      setTimeout(() => navigate("/"), 1500);
    } catch (error) {
      console.error("Login error:", error);
      setMessage("❌ Login failed. Check your credentials.");
    } finally {
      setLoading(false);
    }
  };

  const handleOutsideClick = (e) => {
    if (cardRef.current && !cardRef.current.contains(e.target)) {
      navigate("/");
    }
  };

  return (
    <div className="login-page" onClick={handleOutsideClick}>
      <div className="overlay"></div>
      <div className="login-card" ref={cardRef}>
        <h2>Welcome Back</h2>
        <form onSubmit={handleLogin}>
          <div>
            <input
              type="text"
              placeholder="Email or Username"
              value={identifier}
              onChange={(e) => handleInputChange("Identifier", e.target.value, setIdentifier)}
              required
            />
            {errors.identifier && <p className="error">{errors.identifier}</p>}
          </div>
          <div>
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => handleInputChange("Password", e.target.value, setPassword)}
              required
            />
            {errors.password && <p className="error">{errors.password}</p>}
          </div>
          <button type="submit" disabled={loading || errors.identifier || errors.password}>
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>
        <p>
          Don't have an account? <Link to="/register">Register</Link>
        </p>
        {message && <p>{message}</p>}
      </div>
    </div>
  );
}

export default Login;