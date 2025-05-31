import './ResultsList.css';

function ResultsList({ results, hasSearched }) {
  if (!hasSearched) {
    return null;
  }

  if (!results || results.length === 0) {
    return <p>Nenhum resultado encontrado.</p>;
  }

  return (
    <ul className="results-list">
      {results.map((item, index) => (
        <li key={index} className="result-card">
          <a
            href={item.url}
            target="_blank"
            rel="noopener noreferrer"
            className="card-link"
          >
            <h3>{item.title}</h3>
            <p
              dangerouslySetInnerHTML={{
                __html: item.highlightedContent || item.content,
              }}
            />
          </a>
        </li>
      ))}
    </ul>
  );
}

export default ResultsList;
