import { useState } from 'react';
import SearchBar from './assets/SearchBar';
import ResultsList from './assets/ResultsList';
import Pagination from './assets/Pagination';
import './App.css'; // Mantenha o CSS
import logo from './assets/images/LogoPrinc-FundoPreto.png';
import logoCompacta from './assets/images/LogoPrinc-FundoPreto.png';

function App() {
  const [results, setResults] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [currentQuery, setCurrentQuery] = useState('');
  const [charLimit, setCharLimit] = useState(300);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [suggestions, setSuggestions] = useState([]);
  const [hasSearched, setHasSearched] = useState(false);
  const [showFilters, setShowFilters] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [showPopup, setShowPopup] = useState(false);

  const fetchResults = async (query, page) => {
    if (!query.trim()) {
      setErrorMessage('Please fill in the search field before searching.');
      setShowPopup(true);
      setTimeout(() => setShowPopup(false), 4000);
      return;
    }

    try {
      setCurrentQuery(query);
      setCurrentPage(page);
      setSuggestions([]);
      setHasSearched(true);
      setShowFilters(false); // Fecha o dropdown ao buscar

      const res = await fetch('http://localhost:8080/v1/search', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          q: query,
          p: page,
          cl: charLimit,
          itemsPerPage: itemsPerPage
        }),
      });

      const data = await res.json();
      console.log(data);
      setResults(data.resultsList || []);
      setTotalPages(Math.ceil((data.totalResults || 0) / (data.itemsPerPage || itemsPerPage)));

      if ((data.resultsList?.length || 0) === 0 && data.suggestions) {
        setSuggestions(data.suggestions);
      }
    } catch (error) {
      console.error('Error fetching results:', error);
    }
  };

  const handlePageChange = (page) => {
    fetchResults(currentQuery, page);
  };

  const handleCharLimitChange = (e) => {
    const newLimit = parseInt(e.target.value);
    setCharLimit(newLimit);
    if (currentQuery) {
      fetchResults(currentQuery, 1);
    }
  };

  const handleItemsPerPageChange = (e) => {
    const newItemsPerPage = parseInt(e.target.value);
    setItemsPerPage(newItemsPerPage);
    if (currentQuery) {
      fetchResults(currentQuery, 1);
    }
  };

  const handleFilterButtonClick = () => {
    setShowFilters(!showFilters);
  };

  // Obtenha o ano atual dinamicamente
  const currentYear = new Date().getFullYear();

  return (
    <div className="app-container">
      {showPopup && (
        <div className="custom-popup">
          {errorMessage}
        </div>
      )}

      <div className={`header-container ${hasSearched ? 'header-searched' : ''}`}>
        <a href="/">
          <img
            src={hasSearched ? logoCompacta : logo}
            alt="Logo"
            className="logo"
          />
        </a>
        <div className="search-and-filter-wrapper">
          <SearchBar onSearch={fetchResults} />

          {hasSearched && (
            <div className="filter-dropdown-container">
              <button
                className="filter-dropdown-button"
                onClick={handleFilterButtonClick}
              >
                <span>Filters</span>
                <span className={`arrow ${showFilters ? 'up' : 'down'}`}></span>
              </button>

              <div className={`filters-dropdown ${showFilters ? 'show' : ''}`}>
                <div className="filters">
                  <div className="filter-group">
                    <label htmlFor="charLimitSelect">Character limit per result:</label>
                    <select id="charLimitSelect" value={charLimit} onChange={handleCharLimitChange}>
                      <option value={150}>150</option>
                      <option value={300}>300</option>
                      <option value={500}>500</option>
                    </select>
                  </div>
                  <div className="filter-group">
                    <label htmlFor="itemsPerPageSelect">Items per page:</label>
                    <select id="itemsPerPageSelect" value={itemsPerPage} onChange={handleItemsPerPageChange}>
                      <option value={10}>10</option>
                      <option value={20}>20</option>
                      <option value={30}>30</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Resultados */}
      <ResultsList results={results} hasSearched={hasSearched} />

      {/* Paginação */}
      {totalPages > 1 && (
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          onPageChange={handlePageChange}
        />
      )}

      {hasSearched && results.length === 0 && suggestions.length > 0 && (
        <div className="suggestions-box">
          <p> Did you mean...</p>
          <ul>
            {suggestions.map((suggestion, index) => (
              <li key={index}>
                <a onClick={() => fetchResults(suggestion, 1)}>{suggestion}</a>
              </li>
            ))}
          </ul>
        </div>
      )}

      {/* Footer discreto e pequeno diretamente aqui */}
      <footer className="app-footer">
        <p>&copy; {currentYear} Eureka. All rights reserved. Developed by Heloisa Pimentel and Lucas Pessoa.</p>
      </footer>
    </div>
  );
}

export default App;