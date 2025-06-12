import React, { useState, useEffect } from "react";
import "../styles/QuoteBox.css";
import StarRating from "./StarRating";
import { API_BASE_URL } from '../api'; 
import AuthService from "../auth/AuthService"; 

export default function QuoteBox({
  quote,
  author: initialAuthor, 
  postId,
  userId,
  cumulativeRating,
  numOfRatings,
  onRatingUpdate
}) {
  const [author, setAuthor] = useState(initialAuthor || "Loading..."); // State for dynamic author

  // Fetch username when component mounts or postId changes
  useEffect(() => {
    const fetchUsername = async () => {
      try {
        const authHeader = AuthService.getAuthHeader(); // Get auth header if needed
        const response = await fetch(`${API_BASE_URL}/userposts/${postId}/username`, {
          headers: {
            "Content-Type": "application/json",
            ...authHeader, // Include auth header if endpoint requires authentication
          },
        });

        if (!response.ok) {
          throw new Error("Failed to fetch username");
        }

        const username = await response.text(); // Endpoint returns plain text (username)
        setAuthor(username || "Unknown Author"); // Update author state
      } catch (error) {
        console.error("Error fetching username:", error);
        setAuthor("Unknown Author"); // Fallback on error
      }
    };

    fetchUsername();
  }, [postId]); // Re-run if postId changes

  return (
    <div className="quote-box">
      <div className="quote-content">
        <span className="quote-text-wrapper">
          <span className="opening-quote-mark">“</span>
          <span className="quote-text">{quote}</span> 
          <span className="closing-quote-mark">”</span>
        </span>
      </div>
      <div className="quote-stats">
        <div className="star-rating">
          <StarRating
            postId={postId}
            userId={userId}
            cumulativeRating={cumulativeRating}
            numOfRatings={numOfRatings}
            onRatingUpdate={onRatingUpdate}
          />
        </div>
        <div className="quote-author">{author}</div>
      </div>
    </div>
  );
}