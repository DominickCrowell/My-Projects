import React, { useState, useEffect } from "react";
import { toast } from "react-toastify";
import "../styles/StarRating.css";
import AuthService from "../auth/AuthService";
import { API_BASE_URL } from '../api';

export default function StarRating({
  postId,
  cumulativeRating = 0,
  numOfRatings = 0,
  onRatingUpdate = () => {},
}) {
  const [averageRating, setAverageRating] = useState(0);
  const [hoveredRating, setHoveredRating] = useState(0);
  const [totalRatings, setTotalRatings] = useState(numOfRatings);
  const [floatingValue, setFloatingValue] = useState(null);
  const [isAnimating, setIsAnimating] = useState(false);
  const [hasRated, setHasRated] = useState(false);
  const [userRating, setUserRating] = useState(0);

  const isAuthenticated = AuthService.isAuthenticated();

  useEffect(() => {
    if (numOfRatings > 0) {
      const avg = cumulativeRating / numOfRatings;
      setAverageRating(avg);
      setTotalRatings(numOfRatings);
    }
  }, [cumulativeRating, numOfRatings]);

  useEffect(() => {
    const fetchExistingRating = async () => {
      // Skip fetching if user is not authenticated or there are no ratings
      if (!isAuthenticated || numOfRatings === 0) {
        return;
      }

      try {
        const authHeader = AuthService.getAuthHeader();
        const response = await fetch(`${API_BASE_URL}/userposts/${postId}/my-rating`, {
          headers: {
            "Content-Type": "application/json",
            ...authHeader,
          },
        });

        if (response.ok) {
          const data = await response.json();
          setHasRated(true);
          setUserRating(data.rating);
        } else if (response.status === 401) {
          console.warn("🔒 Unauthorized access to my-rating – session may have expired.");
          AuthService.logout();
          toast.info("Session expired. Please log in again.", {
            className: "custom-toast custom-toast-info",
          });
        }
      } catch (error) {
        console.warn("ℹ️ Could not fetch rating:", error);
      }
    };

    fetchExistingRating();
  }, [postId, isAuthenticated, numOfRatings]);

  const handleRating = (newRating) => {
    if (!isAuthenticated) {
      toast.error("Please log in to rate posts.", {
        className: "custom-toast custom-toast-error",
      });
      return;
    }

    if (hasRated) {
      toast.error("You've already rated this post.", {
        className: "custom-toast custom-toast-error",
      });
      return;
    }

    setFloatingValue(`+${newRating}`);
    triggerAnimation();
    submitRatingToBackend(newRating);
  };

  const triggerAnimation = () => {
    setIsAnimating(true);
    setTimeout(() => {
      setFloatingValue(null);
      setIsAnimating(false);
    }, 1000);
  };

  const submitRatingToBackend = async (newRating) => {
    const authHeader = AuthService.getAuthHeader();
    if (!authHeader) {
      toast.error("You must be logged in to rate posts.", {
        className: "custom-toast custom-toast-error",
      });
      return;
    }

    try {
      const response = await fetch(`${API_BASE_URL}/userposts/${postId}/rate`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          ...authHeader,
        },
        body: JSON.stringify({ rating: newRating }),
      });

      const responseText = await response.text();

      if (!response.ok) {
        console.error("❌ Server responded with:", responseText);
        if (response.status === 401) {
          AuthService.logout();
          toast.info("Session expired. Please log in again.", {
            className: "custom-toast custom-toast-info",
          });
        }
        throw new Error("Failed to rate post");
      }

      const updatedPost = JSON.parse(responseText);
      const newAvg =
        updatedPost.numOfRatings > 0
          ? updatedPost.cumulativeRating / updatedPost.numOfRatings
          : 0;

      setAverageRating(newAvg);
      setTotalRatings(updatedPost.numOfRatings);
      setHasRated(true);
      setUserRating(newRating);
      onRatingUpdate();
    } catch (error) {
      console.error("🚫 Error rating post:", error);
      toast.error("Failed to submit rating.", {
        className: "custom-toast custom-toast-error",
      });
    }
  };

  return (
    <div className="star-rating-container">
      <div className="star-container" onMouseLeave={() => setHoveredRating(0)}>
        {[1, 2, 3, 4, 5].map((star) => {
          const isGlowing = hoveredRating
            ? star <= hoveredRating
            : hasRated
              ? star <= userRating
              : star <= averageRating;

          const shouldSpin = isAnimating && star <= userRating;

          return (
            <span
              key={star}
              onClick={() => handleRating(star)}
              onMouseEnter={() => setHoveredRating(star)}
              className={`star ${isGlowing ? (hasRated ? "rated" : "selected") : ""} ${shouldSpin ? "spinning" : ""}`}
              style={{ cursor: isAuthenticated ? "pointer" : "not-allowed" }}
              title={isAuthenticated ? "Rate this post" : "Login to rate"}
            >
              ★
            </span>
          );
        })}
      </div>

      <span className="rating-text">
        {totalRatings > 0
          ? `${averageRating.toFixed(1)} ★`
          : hasRated
            ? "You've already rated this post"
            : "No rating"}
      </span>

      {totalRatings > 0 && (
        <span className="total-ratings">
          ({totalRatings} rating{totalRatings > 1 ? "s" : ""})
        </span>
      )}

      {floatingValue && (
        <span className="floating-plus">
          {floatingValue}
        </span>
      )}
    </div>
  );
}