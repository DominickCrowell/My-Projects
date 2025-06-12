import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import './styles/Toast.css'; // Import the toast styles
import './App.css';
import Navbar from "./layout/Navbar";
import Home from "./pages/Home";
import UserInfo from "./pages/UserInfo";
import InputQuote from './pages/InputQuote';
import Login from "./auth/Login";
import Register from "./auth/Register";
import AuthService from "./auth/AuthService";
import ViewUser from "./pages/ViewUser";
import MentalHealthDesk from "./pages/MentalHealthDesk";
import NotFound from "./pages/errors/NotFound"; 
import ThemeToggleButton from "./components/ThemeToggleButton";
import RegisterAdmin from "./auth/RegisterAdmin";

const ProtectedRoute = ({ element, requiredRoles }) => {
  const isAuthenticated = AuthService.isAuthenticated();
  const userRole = AuthService.getUserRole();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (requiredRoles && !requiredRoles.includes(userRole)) {
    return <Navigate to="/" replace />;
  }

  return element;
};


function App() {
  return (
    <div className="App">
      <Router>
        
        <Navbar />
        
        <ToastContainer
          position="top-right"
          autoClose={2500}
          hideProgressBar
          closeOnClick
          pauseOnHover
          draggable
        />

        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/health_desk" element={<ProtectedRoute element={<MentalHealthDesk />} />} />

          {/* ✅ Protected Route Input Quote */}
          <Route path="/user_info" element={<ProtectedRoute element={<UserInfo />} />} />
          <Route path="/input_quote" element={<ProtectedRoute element={<InputQuote />} />} />
          <Route path="/viewuser/:id" element={<ProtectedRoute element={<ViewUser />} />} /> 
          <Route 
            path="/register_admin" 
            element={<ProtectedRoute element={<RegisterAdmin />} requiredRoles={["ADMIN"]} />} 
          />

          <Route path="*" element={<NotFound />} />

        </Routes>
        <ThemeToggleButton />
      </Router>
    </div>
  );
}

export default App;