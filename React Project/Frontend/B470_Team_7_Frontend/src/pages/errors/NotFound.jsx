import React from "react";
import { Link } from "react-router-dom";
import "../../styles/NotFound.css";

export default function NotFound() {
  return (
    <div className="not-found-container">
      <h1>404 - Page Not Found</h1>
      <p>Sorry, there’s nothing here.</p>
      <p>The page you’re looking for doesn’t exist or has been moved.</p>
      <div className="not-found-actions">
        <Link to="/" className="btn btn-primary me-2">
          Go Back to Home
        </Link>
      </div>
    </div>
  );
}