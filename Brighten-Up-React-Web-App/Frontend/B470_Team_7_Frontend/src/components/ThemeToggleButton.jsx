import React, { useState, useEffect } from "react";
import "../styles/ThemeToggleButton.css"; // We'll create this CSS file

const ThemeToggleButton = () => {
  const [theme, setTheme] = useState("light"); // Default theme: "light"
  const [showButton, setShowButton] = useState(false); // Control button visibility

  // Apply theme to the document root
  useEffect(() => {
    document.documentElement.setAttribute("data-theme", theme);
  }, [theme]);

  // Toggle theme between light and dark
  const toggleTheme = () => {
    setTheme((prevTheme) => (prevTheme === "light" ? "dark" : "light"));
  };

  // Handle mouse movement to show/hide the button
  const handleMouseMove = (e) => {
    const windowHeight = window.innerHeight;
    const windowWidth = window.innerWidth;
    const mouseY = e.clientY;
    const mouseX = e.clientX;

    // Show the button if the mouse is in the bottom 10% and right 10% of the screen
    const isInBottom10Percent = mouseY > windowHeight * 0.9;
    const isInRight10Percent = mouseX > windowWidth * 0.9;

    setShowButton(isInBottom10Percent && isInRight10Percent);
  };

  // Add mouse move event listener
  useEffect(() => {
    window.addEventListener("mousemove", handleMouseMove);
    return () => {
      window.removeEventListener("mousemove", handleMouseMove);
    };
  }, []);

  return (
    <button
      className={`theme-toggle-button ${showButton ? "visible" : "hidden"}`}
      onClick={toggleTheme}
      aria-label={theme === "light" ? "Switch to dark mode" : "Switch to light mode"}
    >
      {theme === "light" ? "🌙" : "☀️"}
    </button>
  );
};

export default ThemeToggleButton;