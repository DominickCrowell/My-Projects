import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import { toast } from 'react-toastify';
import AuthService from "../auth/AuthService";
import { API_BASE_URL } from '../api';
import '../styles/UserInfo.css';

export default function UserInfo() {
  const [users, setUsers] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async (query = "") => {
    try {
      const url = query
        ? `${API_BASE_URL}/users/search?query=${encodeURIComponent(query)}`
        : `${API_BASE_URL}/users`;
      const result = await axios.get(url);
      setUsers(result.data);
      setFilteredUsers(result.data);
    } catch (error) {
      console.error("Error loading users:", error);
      toast.error("Failed to load users. Please try again.", {
        className: "custom-toast custom-toast-error",
      });
    }
  };

  const handleSearch = async (e) => {
    const query = e.target.value;
    setSearchQuery(query);
    await loadUsers(query); 
  };

  const handleDeleteUser = async (userId) => {
    const userToDelete = users.find((user) => user.id === userId);
    if (!userToDelete) {
      toast.error("User not found.", {
        className: "custom-toast custom-toast-error",
      });
      return;
    }

    if (userToDelete.role === "ADMIN") {
      toast.error("Admins cannot be deleted.", {
        className: "custom-toast custom-toast-error",
      });
      return;
    }

    toast(
      ({ closeToast }) => (
        <div className="delete-confirm-toast">
          <p>Are you sure you want to delete this user and all their posts?</p>
          <div className="toast-buttons">
            <button
              className="btn btn-outline-secondary"
              onClick={async () => {
                closeToast();
                try {
                  await axios({
                    method: 'delete',
                    url: `${API_BASE_URL}/user/${userId}`,
                    headers: {
                      'Content-Type': 'application/json',
                      ...AuthService.getAuthHeader(),
                    },
                  });
                  toast.success("User deleted successfully.", {
                    className: "custom-toast custom-toast-success",
                    onClose: () => {
                      loadUsers(searchQuery); 
                      if (window.location.pathname === `/viewuser/${userId}`) {
                        window.location.href = "/user_info";
                      }
                    },
                  });
                } catch (err) {
                  console.error("Failed to delete user:", err);
                  toast.error("Error deleting user.", {
                    className: "custom-toast custom-toast-error",
                  });
                }
              }}
            >
              Yes
            </button>
            <button
              className="btn btn-outline-secondary"
              onClick={closeToast}
            >
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
        className: "custom-toast-delete",
      }
    );
  };

  const isAuthenticated = AuthService.isAuthenticated();
  const userRole = AuthService.getUserRole();

  return (
    <div className="container-fluid">
      <div className="py-4">
        <div className="search-container">
          <input
            type="text"
            className="form-control search-input"
            placeholder="Search by username..."
            value={searchQuery}
            onChange={handleSearch}
          />
        </div>
        
        <div className="table-container">
          <table className="custom-table">
            <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">Username</th>
                <th scope="col">Email</th>
                <th scope="col">Action</th>
              </tr>
            </thead>
            <tbody>
              {filteredUsers.map((user, index) => (
                <tr key={user.id}>
                  <th scope="row">{index + 1}</th>
                  <td>{user.username}</td>
                  <td>{user.email}</td>
                  <td>
                    <Link className="btn btn-view mx-2" to={`/viewuser/${user.id}`}>
                      <span className="btn-edit-desktop">View</span>
                      <span className="btn-edit-mobile">👁</span>
                    </Link>
                    {isAuthenticated && userRole === "ADMIN" && (
                      <button
                        className="btn btn-delete mx-2"
                        onClick={() => handleDeleteUser(user.id)}
                        disabled={user.role === "ADMIN"}
                      >
                        <span className="btn-delete-desktop">Delete</span>
                        <span className="btn-delete-mobile">✖</span>
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}