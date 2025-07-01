import { useState } from 'react';
import "./SearchBar.css";

export default function SearchBar({ onSearch }) {
  const [query, setQuery] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSearch(query, 1); 
  };

  return (
    <form onSubmit={handleSubmit} className="search-bar">
      <input
        type="text"
        className="search-input"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder="Enter your search query..."
      />
      <button type="submit" className="search-button">Search</button>
    </form>
  );
}