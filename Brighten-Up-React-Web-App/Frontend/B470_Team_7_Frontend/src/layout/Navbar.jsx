import React, { useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import AuthService from "../auth/AuthService";
import "../styles/Navbar.css";

export default function Navbar() {
  const navigate = useNavigate();
  const isAuthenticated = AuthService.isAuthenticated();
  const userRole = AuthService.getUserRole();
  const navbarRef = useRef(null);

  const handleLogout = () => {
    toast(
      ({ closeToast }) => (
        <div className="logout-confirm-toast">
          <p>Are you sure you want to log out?</p>
          <div className="toast-buttons">
            <button
              className="btn btn-outline-secondary"
              onClick={() => {
                closeToast();
                AuthService.logout();
                toast.success("Logout success!\nRedirecting to the home page...", {
                  className: "custom-toast custom-toast-success",
                  onClose: () => navigate("/"),
                });
              }}
            >
              Yes
            </button>
            <button className="btn btn-outline-secondary" onClick={closeToast}>
              No
            </button>
          </div>
        </div>
      ),
      {
        position: "top-center",
        autoClose: false,
        closeOnClick: false,
        draggable: false,
        className: "custom-toast-logout"
      }
    );
  };

  const handleNavLinkClick = () => {
    if (navbarRef.current) {
      const collapseElement = navbarRef.current;
      const bsCollapse = new window.bootstrap.Collapse(collapseElement, {
        toggle: false,
      });
      bsCollapse.hide();
    }
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-light fixed-top">
      <div className="container-fluid">
        <Link className="navbar-brand" to="/">Brighten Up</Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav" ref={navbarRef}>
          <ul className="navbar-nav ms-auto">
            {!isAuthenticated ? (
              <>
                <li className="nav-item">
                  <Link
                    className="btn btn-outline-dark me-2"
                    to="/login"
                    onClick={handleNavLinkClick}
                  >
                    Login
                  </Link>
                </li>
                <li className="nav-item">
                  <Link
                    className="btn btn-outline-dark"
                    to="/register"
                    onClick={handleNavLinkClick}
                  >
                    Register
                  </Link>
                </li>
              </>
            ) : (
              <>
                <li className="nav-item">
                  <Link
                    className="btn btn-outline-light btn-dark me-2"
                    to="/health_desk"
                    onClick={handleNavLinkClick}
                  >
                    Health
                  </Link>
                </li>
                <li className="nav-item">
                  <Link
                    className="btn btn-outline-light btn-dark me-2"
                    to="/input_quote"
                    onClick={handleNavLinkClick}
                  >
                    Post
                  </Link>
                </li>
                <li className="nav-item">
                  <Link
                    className="btn btn-outline-light btn-dark me-2"
                    to="/user_info"
                    onClick={handleNavLinkClick}
                  >
                    Users
                  </Link>
                </li>
                <li className="nav-item admin-container">
                  {userRole === "ADMIN" ? (
                    <>
                      <button className="btn btn-outline-dark admin-btn">
                        Admin
                      </button>
                      <div className="admin-options">
                      <Link
                          className="admin-option btn btn-outline-dark"
                          to="/register_admin"
                          onClick={handleNavLinkClick}
                        >
                          Registration
                        </Link>
                        <button
                          className="admin-option btn btn-outline-dark"
                          onClick={() => {
                            handleLogout();
                            handleNavLinkClick();
                          }}
                        >
                          Logout
                        </button>
                      </div>
                    </>
                  ) : (
                    <button
                      className="btn btn-outline-dark logout-hovered"
                      onClick={() => {
                        handleLogout();
                        handleNavLinkClick();
                      }}
                    >
                      <span className="not-hovered">Member</span>
                      <span className="hovered">Logout</span>
                    </button>
                  )}
                </li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
}