function ResultsList({ results, hasSearched }) {
  if (!hasSearched) {
    return null;
  }

  if (!results || results.length === 0) {
    return <p>Nenhum resultado encontrado.</p>;
  }

  return (
    <ul>
      {results.map((item, index) => (
        <li key={index} style={{ marginBottom: '1.5rem' }}>
          <h3>{item.title}</h3>
          <p dangerouslySetInnerHTML={{ __html: item.highlightedContent || item.content }} />
          <a href={item.url} target="_blank" rel="noopener noreferrer">
            {item.url}
          </a>
        </li>
      ))}
    </ul>
  );
}

export default ResultsList;
