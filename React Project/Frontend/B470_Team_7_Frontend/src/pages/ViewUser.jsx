import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { toast } from 'react-toastify'; // Import toast
import axios from 'axios';
import { API_BASE_URL } from '../api';
import '../styles/ViewUser.css';
import AuthService from '../auth/AuthService';

export default function ViewUser() {
    const [userPosts, setUserPosts] = useState([]);
    const [selectedPostIds, setSelectedPostIds] = useState([]);
    const [user, setUser] = useState({ name: '', username: '', email: '', role: '' }); // Ensure role is included
    
    const { id } = useParams();
    const currentUserRole = AuthService.getUserRole();

    useEffect(() => {
        loadUser();
        loadUserPosts();
    }, []);

    const loadUser = async () => {
        try {
            const result = await axios.get(`${API_BASE_URL}/user/${id}`);
            setUser(result.data);
        } catch (error) {
            console.error('Error loading user:', error);
            toast.error("Failed to load user data.", {
                className: "custom-toast custom-toast-error",
            });
        }
    };

    const loadUserPosts = async () => {
        try {
            const result = await axios.get(`${API_BASE_URL}/userposts/by-user/${id}`);
            setUserPosts(result.data);
        } catch (error) {
            console.error('Error loading user posts:', error);
            toast.error("Failed to load user posts.", {
                className: "custom-toast custom-toast-error",
            });
        }
    };

    const handleCheckboxChange = (postId) => {
        setSelectedPostIds((prevSelected) =>
            prevSelected.includes(postId)
                ? prevSelected.filter((id) => id !== postId)
                : [...prevSelected, postId]
        );
    };

    const handleDeletePost = async () => {
        if (selectedPostIds.length === 0) {
            toast.error("Please select at least one post to delete.", {
                className: "custom-toast custom-toast-error",
            });
            return;
        }
    
        try {
            const response = await axios({
                method: 'delete',
                url: `${API_BASE_URL}/userposts/bulk`,
                headers: {
                    'Content-Type': 'application/json',
                    ...AuthService.getAuthHeader(),
                },
                data: selectedPostIds,
            });
    
            setUserPosts((prevPosts) =>
                prevPosts.filter((post) => !selectedPostIds.includes(post.id))
            );
            setSelectedPostIds([]);
            toast.success("Selected posts deleted successfully!", {
                className: "custom-toast custom-toast-success",
            });
        } catch (error) {
            console.error('❌ Error deleting posts:', error.response || error);
            toast.error("Failed to delete posts. Please try again.", {
                className: "custom-toast custom-toast-error",
            });
        }
    };

    const handleDeleteUser = async (userId) => {
        // Check if the user being deleted is an admin
        if (user.role === "ADMIN") {
            toast.error("Admins cannot be deleted.", {
                className: "custom-toast custom-toast-error",
            });
            return;
        }

        // Show a confirmation toast with buttons if checks pass
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
                                    const response = await axios({
                                        method: 'delete',
                                        url: `${API_BASE_URL}/user/${userId}`,
                                        headers: {
                                            'Content-Type': 'application/json',
                                            ...AuthService.getAuthHeader(),
                                        },
                                    });
                    
                                    toast.success("User deleted successfully.", {
                                        className: "custom-toast custom-toast-success",
                                        onClose: () => (window.location.href = "/user_info"),
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
                autoClose: false, // Keep open until user responds
                closeOnClick: false,
                draggable: false,
                className: "custom-toast-delete",
            }
        );
    };

    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    };

    return (
        <div className="view-user-container">
            <div className="py-4">
                <div className="row">
                    <div className="col-md-12 border rounded p-4 mt-2 shadow bg-light">
                        <h2 className="text-center m-4">User Details</h2>
                        <div className="card quote-box">
                            <div className="card-header pb-4">
                                <div className="mb-2">
                                    User Information by ID: {user.id}
                                </div>
                                <ul className="list-group list-group-flush">
                                    <li className="list-group-item">
                                        <b>Username: </b>
                                        {user.username}
                                    </li>
                                    <li className="list-group-item">
                                        <b>Email: </b>
                                        {user.email}
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <h2 className="text-center m-4">User Posts</h2>
                        {userPosts.map((post) => (
                            <div className="card quote-box my-2 mt-4" key={post.id}>
                                <div className="card-header pb-4">
                                    <div className="mb-2">Post ID: {post.id}</div>
                                    <ul className="list-group list-group-flush">
                                        <li className="list-group-item">
                                            <div className="user-post">{post.post}</div>
                                            {currentUserRole === "ADMIN" && (
                                                <div className="checkbox-select">
                                                    <label className="checkbox-label">
                                                        <span className="checkbox-title">Select</span>
                                                        <input
                                                            type="checkbox"
                                                            checked={selectedPostIds.includes(post.id)}
                                                            onChange={() => handleCheckboxChange(post.id)}
                                                        />
                                                    </label>
                                                </div>
                                            )}
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        ))}

                        <div className="button-group">
                            <button
                                className="btn btn-primary"
                                onClick={scrollToTop}
                            >
                                <span className="btn-edit-desktop">Back to Top</span>
                                <span className="btn-edit-mobile">Top</span>
                            </button>

                            {currentUserRole === "ADMIN" && (
                                <button
                                    className="view-user-post-del btn btn-danger mx-2"
                                    onClick={handleDeletePost}
                                >
                                    <span className="btn-edit-desktop">Delete Selection</span>
                                    <span className="btn-edit-mobile">✖</span>
                                </button>
                            )}

                            {currentUserRole === "ADMIN" && (
                                <button
                                    className="btn btn-outline-danger mx-2"
                                    onClick={() => handleDeleteUser(user.id)}
                                    disabled={user.role === "ADMIN"}
                                >
                                    <span className="btn-edit-desktop">Delete User</span>
                                    <span className="btn-edit-mobile delete-user-emoji">👤</span>
                                </button>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}