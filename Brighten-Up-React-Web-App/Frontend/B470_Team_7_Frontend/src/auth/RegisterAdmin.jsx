import { useState, useRef } from "react";
import { useNavigate, Link } from "react-router-dom";
import AuthService from "./AuthService";
import "../styles/RegisterModal.css";

function RegisterAdmin() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [errors, setErrors] = useState({ username: "", email: "", password: "" });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const cardRef = useRef(null);

  // Validate input for whitespace
  const validateInput = (field, value) => {
    const trimmedValue = value.trim();
    if (field === "Username" && /\s/.test(trimmedValue)) {
      return "Username cannot contain spaces.";
    }
    if (field === "Email" && /\s/.test(trimmedValue)) {
      return "Email cannot contain spaces.";
    }
    if (field === "Password" && /\s/.test(trimmedValue)) {
      return "Password cannot contain spaces.";
    }
    return "";
  };

  const handleInputChange = (field, value, setValue) => {
    setValue(value);
    const error = validateInput(field, value);
    setErrors((prev) => ({ ...prev, [field.toLowerCase()]: error }));
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setMessage("");
    setLoading(true);

    // Trim inputs
    const trimmedUsername = username.trim();
    const trimmedEmail = email.trim();
    const trimmedPassword = password.trim();

    // Validate inputs
    const usernameError = validateInput("Username", trimmedUsername);
    const emailError = validateInput("Email", trimmedEmail);
    const passwordError = validateInput("Password", trimmedPassword);

    if (usernameError || emailError || passwordError) {
      setErrors({
        username: usernameError,
        email: emailError,
        password: passwordError,
      });
      setLoading(false);
      return;
    }

    try {
      await AuthService.registerAdmin(trimmedEmail, trimmedUsername, trimmedPassword);
      setMessage("✅ Admin registration successful! Redirecting to registered users page...");
      setTimeout(() => navigate("/user_info"), 1500);
    } catch (error) {
      setMessage("❌ Registration failed. Username or Email may already be in use.");
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
    <div className="register-page" onClick={handleOutsideClick}>
      <div className="overlay"></div>
      <div className="register-card" ref={cardRef}>
        <h2>Admin Registration</h2>
        <form onSubmit={handleRegister}>
          <div>
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => handleInputChange("Username", e.target.value, setUsername)}
              required
            />
            {errors.username && <p className="error">{errors.username}</p>}
          </div>
          <div>
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => handleInputChange("Email", e.target.value, setEmail)}
              required
            />
            {errors.email && <p className="error">{errors.email}</p>}
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
          <button type="submit" disabled={loading || errors.username || errors.email || errors.password}>
            {loading ? "Registering new admin..." : "Register"}
          </button>
        </form>
        <p>
          Return to <Link to="/">Home</Link>
        </p>
        {message && <p>{message}</p>}
      </div>
    </div>
  );
}

export default RegisterAdmin;