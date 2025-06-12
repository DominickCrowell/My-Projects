import React, { useEffect, useState } from "react";
import QuoteBox from "../components/QuoteBox";
import { API_BASE_URL } from '../api';

export default function Home() {
  const [quotes, setQuotes] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchQuotes = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/userposts`);
      const data = await response.json();
      setQuotes(data);
    } catch (err) {
      console.error("Error loading quotes:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchQuotes();
  }, []);

  return (
    <div className="container py-4">
      <h2 className="my-4 quote-header">Recent Quotes</h2>
      {loading ? (
        <p>Loading quotes...</p>
      ) : quotes.length === 0 ? (
        <p>No quotes posted yet.</p>
      ) : (
        quotes.map((quote) => (
          <QuoteBox
            key={quote.id}
            quote={quote.post}
            postId={quote.id}
            userId={quote.user}
            cumulativeRating={quote.cumulative_rating || 0}
            numOfRatings={quote.num_of_ratings || 0}
            onRatingUpdate={fetchQuotes}
          />
        ))
      )}
    </div>
  );
}