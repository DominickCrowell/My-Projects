import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import EmojiPicker from "emoji-picker-react";
import "../styles/InputQuoteBox.css";
import { Filter } from "bad-words";
import AuthService from "../auth/AuthService";
import { API_BASE_URL } from '../api';


export default function InputQuote() {
  const [quote, setQuote] = useState("");
  const [loading, setLoading] = useState(false);
  const [showEmojiPicker, setShowEmojiPicker] = useState(false);
  const navigate = useNavigate();
  const filter = new Filter();
  const cardRef = useRef(null);

  const handleSubmit = async () => {
    const trimmedQuote = quote.trim();
    const cleanQuote = filter.clean(trimmedQuote);

    if (trimmedQuote !== cleanQuote) {
      toast.error("Profanity detected! Please remove inappropriate words.", {
        className: "custom-toast custom-toast-error",
      });
      return;
    }

    if (!cleanQuote.trim()) {
      toast.error("Please enter a valid quote.", {
        className: "custom-toast custom-toast-error",
      });
      return;
    }

    try {
      setLoading(true);
      const payload = { post: cleanQuote };
      const headers = {
        "Content-Type": "application/json",
        ...AuthService.getAuthHeader(),
      };

      if (!headers.Authorization) {
        toast.error("Please log in to submit a quote.", {
          className: "custom-toast custom-toast-error",
        });
        navigate("/login");
        return;
      }

      const response = await fetch(`${API_BASE_URL}/userposts`, {
        method: "POST",
        headers,
        body: JSON.stringify(payload),
      });

      const contentType = response.headers.get("content-type");
      const responseText = await response.text();

      if (!response.ok) {
        if (response.status === 401) {
          toast.error("Session expired. Please log in again.", {
            className: "custom-toast custom-toast-error",
          });
          AuthService.logout();
          navigate("/login");
          return;
        } else if (response.status === 400) {
          toast.error(responseText || "Duplicate post detected.", {
            className: "custom-toast custom-toast-error",
          });
          return;
        }
        throw new Error(`Server error: ${response.status}\n${responseText}`);
      }

      if (contentType && contentType.includes("application/json")) {
        toast.success("Quote submitted successfully!", {
          className: "custom-toast custom-toast-success",
        });
        setQuote("");
        navigate("/");
      } else {
        toast.error("Unexpected response format from server.", {
          className: "custom-toast custom-toast-error",
        });
      }
    } catch (error) {
      toast.error(error.message || "Failed to submit quote.", {
        className: "custom-toast custom-toast-error",
      });
    } finally {
      setLoading(false);
    }
  };

  const handleOutsideClick = (e) => {
    if (cardRef.current && !cardRef.current.contains(e.target)) {
      navigate("/");
    }
  };

  const handleEmojiClick = (emojiObject) => {
    const newQuote = quote + emojiObject.emoji;
    if (newQuote.length <= 500) {
      setQuote(newQuote);
    } else {
      toast.warn("Character limit reached!", {
        className: "custom-toast custom-toast-warn",
      });
    }
    setShowEmojiPicker(false);
  };

  return (
    <div className="input-page" onClick={handleOutsideClick}>
      <div className="overlay"></div>
      <div className="full-quote-display" ref={cardRef}>
        <div className="input-quote-box">
          <textarea
            className="quote-input"
            placeholder="Say something nice..."
            value={quote}
            onChange={(e) => {
              if (e.target.value.length <= 500) {
                setQuote(e.target.value);
              }
            }}
          />
          <div className="button-row">
            <button
              className="emoji-btn"
              onClick={() => setShowEmojiPicker(!showEmojiPicker)}
              type="button"
            >
              😊
            </button>
            <button
              className="submit-btn"
              onClick={handleSubmit}
              disabled={loading}
            >
              <span className="submit-btn-text">Submit</span>
            </button>
          </div>
          {showEmojiPicker && (
            <div className="emoji-picker-container">
              <EmojiPicker
                onEmojiClick={handleEmojiClick}
                theme="dark"
                height={350}
                width="100%"
                skinTonesDisabled={true}
              />
            </div>
          )}
        </div>
        <div className="submit-row">
          <div className="input-quote-author">
            {loading ? "Please Wait..." : "Say Something Nice!"}
          </div>
          <div className="char-count">{quote.length}/500</div>
        </div>
      </div>
    </div>
  );
}