import { useState } from 'react';
import SearchBar from './assets/SearchBar';
import ResultsList from './assets/ResultsList';
import Pagination from './assets/Pagination';
import './App.css'
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

  const fetchResults = async (query, page) => {
    try {
      setCurrentQuery(query);
      setCurrentPage(page);
      setSuggestions([]);
      setHasSearched(true);

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
      console.error('Erro ao buscar resultados:', error);
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

  return (
    <div className="app-container">
      <div className={`header-container ${hasSearched ? 'header-searched' : ''}`}>
        <img
          src={hasSearched ? logoCompacta : logo}
          alt="Logo"
          className="logo"
        />
        <SearchBar onSearch={fetchResults} />
      </div>


      {/* Filter Button */}
      <button
        className="filter-toggle-button"
        onClick={() => setShowFilters(!showFilters)}
      >
        {showFilters ? 'Hide Filters' : 'Show Filters'}
      </button>

      {/* Filters Section - now conditionally rendered */}
      {showFilters && (
        <div className="filters">
          <div className="filter-group">
            <label htmlFor="limitSelect">Limite de caracteres por resultado:</label>
            <select id="limitSelect" value={charLimit} onChange={handleCharLimitChange}>
              <option value={150}>150</option>
              <option value={300}>300</option>
              <option value={500}>500</option>
            </select>
          </div>

          <div className="filter-group">
            <label htmlFor="itemsPerPageSelect">Itens por página:</label>
            <select id="itemsPerPageSelect" value={itemsPerPage} onChange={handleItemsPerPageChange}>
              <option value={10}>10</option>
              <option value={20}>20</option>
              <option value={30}>30</option>
            </select>
          </div>
        </div>
      )}

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

      {/* Sugestões */}
      {hasSearched && results.length === 0 && suggestions.length > 0 && (
        <div className="suggestions-box">
          <p>Nenhum resultado encontrado para "{currentQuery}". Você quis dizer:</p>
          <ul>
            {suggestions.map((suggestion, index) => (
              <li key={index}>
                <a onClick={() => fetchResults(suggestion, 1)}>{suggestion}</a>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}


export default App;