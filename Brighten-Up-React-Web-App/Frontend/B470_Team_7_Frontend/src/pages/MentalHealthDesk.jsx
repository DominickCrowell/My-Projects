import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { Filter } from "bad-words";
import AuthService from "../auth/AuthService";
import { API_BASE_URL } from '../api';
import "../styles/MentalHealthDesk.css"; 
import axios from 'axios'; // Import axios
import axiosRetry from 'axios-retry'; // Import axios-retry

// Configure axios-retry
axiosRetry(axios, {
  retries: 3, // Retry up to 3 times
  retryDelay: (retryCount) => {
    return retryCount * 1000; // Exponential backoff: 1s, 2s, 3s
  },
  retryCondition: (error) => {
    // Retry on network errors or 5xx status codes (e.g., 500 Internal Server Error from the backend)
    return axiosRetry.isNetworkOrIdempotentRequestError(error) || (error.response && error.response.status >= 500);
  },
  onRetry: (retryCount, error) => {
    // Optional: Log retry attempts or update the UI
    console.warn(`Retry attempt ${retryCount} due to error: ${error.message}`);
  },
});

export default function MentalHealthDesk() {
  const [input, setInput] = useState("");
  const [conversation, setConversation] = useState([]);
  const [loading, setLoading] = useState(false);
  const [personality, setPersonality] = useState("mindful"); 
  const navigate = useNavigate();
  const filter = new Filter();
  const cardRef = useRef(null);

  const handleSubmit = async () => {
    const trimmedInput = input.trim();
    const cleanInput = filter.clean(trimmedInput);

    if (trimmedInput !== cleanInput) {
      toast.error("Profanity detected! Please use appropriate language.", {
        className: "custom-toast custom-toast-error",
      });
      return;
    }

    if (!cleanInput.trim()) {
      toast.error("Please enter a message.", {
        className: "custom-toast custom-toast-error",
      });
      return;
    }

    // Add user message to conversation
    const userMessage = { role: "user", content: cleanInput };
    setConversation((prev) => [...prev, userMessage]);
    setInput("");
    setLoading(true);

    try {
      const headers = {
        "Content-Type": "application/json",
        ...AuthService.getAuthHeader(),
      };

      if (!headers.Authorization) {
        toast.error("Please log in to use the Mental Health Desk.", {
          className: "custom-toast custom-toast-error",
        });
        navigate("/login");
        return;
      }

      // Use axios instead of fetch, with retry logic already configured
      const response = await axios.post(
        `${API_BASE_URL}/xai/chat/completions`,
        {
          model: "grok-3-mini-beta",
          messages: conversation.concat(userMessage), // Send conversation history and user message
          max_tokens: 1000,
          personality: personality // Send the selected personality
        },
        { headers }
      );

      const data = response.data; // axios automatically parses JSON

      // No need to check response.ok since axios throws an error for non-2xx status codes
      if (response.status === 401) {
        toast.error("Session expired. Please log in again.", {
          className: "custom-toast custom-toast-error",
        });
        AuthService.logout();
        navigate("/login");
        return;
      } else if (response.status === 429) {
        toast.error("Rate limit exceeded. Please try again later.", {
          className: "custom-toast custom-toast-error",
        });
        setConversation((prev) => prev.slice(0, -1)); // Remove failed user message
        return;
      }

      // Parse the response from the proxy controller
      try {
        const aiResponse = JSON.parse(data.data); // Extract the xAI API response from the "data" field

        if (!aiResponse.choices || !aiResponse.choices[0] || !aiResponse.choices[0].message) {
          throw new Error("Invalid response format: Missing choices or message");
        }

        const aiMessage = {
          role: "assistant",
          content: aiResponse.choices[0].message.content,
        };

        setConversation((prev) => [...prev, aiMessage]);
        toast.success("Response received!", {
          className: "custom-toast custom-toast-success",
        });
      } catch (parseError) {
        console.error("Error parsing AI response:", parseError);
        throw new Error("Failed to parse AI response: " + parseError.message);
      }
    } catch (error) {
      console.error("Request Failed:", error); // Debug: Log any errors
      let errorMessage = error.message || "Failed to get response.";
      
      // Customize the error message based on the error type
      if (error.response) {
        // Server responded with a status code outside 2xx
        if (error.response.status === 429) {
          errorMessage = "Rate limit exceeded. Please try again later.";
        } else if (error.response.status === 500) {
          errorMessage = "Server error. Please try again later.";
        }
      } else if (error.request) {
        // Request was made but no response was received (e.g., network error)
        errorMessage = "Network error. Please check your connection and try again.";
      }

      toast.error(errorMessage, {
        className: "custom-toast custom-toast-error",
      });
      setConversation((prev) => prev.slice(0, -1)); // Remove failed user message
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
    <div className="mhd-input-page" onClick={handleOutsideClick}>
      {/* Background Overlay */}
      <div className="mhd-overlay" aria-hidden="true"></div>
  
      {/* Main Dialog Container */}
      <div className="mhd-full-quote-display" ref={cardRef} role="dialog" aria-label="Mental Health Desk">
        <div className="mhd-input-quote-box">
          {/* Personality Selector */}
          <div className="mhd-personality-selector">
            <label htmlFor="personality-select">Agent Personality: </label>
            <select
              id="personality-select"
              value={personality}
              onChange={(e) => setPersonality(e.target.value)}
              aria-label="Select agent personality"
            >
              <option value="mindful">Mindful Instructor</option>
              <option value="roleModel">Role Model</option>
              <option value="drillSergeant">Trainer</option>
            </select>
          </div>

          {/* Conversation History Section */}
          <div className="mhd-conversation-display">
            {conversation.length === 0 ? (
              <div className="mhd-conversation-placeholder">
                Welcome to the mental health desk.
              </div>
            ) : (
              conversation.map((msg, index) => (
                <div
                  key={index}
                  className={`mhd-message ${
                    msg.role === "user" ? "mhd-user-message" : "mhd-ai-message"
                  }`}
                >
                  <span className="mhd-message-content">{msg.content}</span>
                </div>
              ))
            )}
          </div>
  
          {/* Input Area Section */}
          <textarea
            className="mhd-quote-input"
            placeholder="I am your personal health desk agent. Ask me anything."
            value={input}
            onChange={(e) => {
              if (e.target.value.length <= 500) {
                setInput(e.target.value);
              }
            }}
            aria-label="Enter your message for the Mental Health Desk"
            maxLength={500}
          />
  
          {/* Footer Section (Buttons and Info in One Row) */}
          <div className="mhd-footer-row">
            {/* Button Row */}
            <div className="mhd-button-row">
              <button
                className="mhd-submit-btn"
                onClick={handleSubmit}
                disabled={loading}
                aria-label={loading ? "Processing message" : "Send message"}
              >
                <span className="mhd-submit-btn-text">Send</span>
              </button>
              <button
                className="mhd-clear-btn"
                onClick={() => setConversation([])}
                disabled={conversation.length === 0 || loading}
                aria-label="Clear conversation"
              >
                <span className="mhd-clear-btn-text">Clear</span>
              </button>
            </div>
  
            {/* Submit Row (Author and Char Count) */}
            <div className="mhd-submit-row">
              <span className="mhd-input-quote-author">
                {loading ? "Processing..." : personality === "mindful" ? "Mindful Instructor" : personality === "roleModel" ? "Role Model" : "Trainer"}
              </span>
              <span className="mhd-char-count" aria-live="polite">
                {input.length}/500
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}