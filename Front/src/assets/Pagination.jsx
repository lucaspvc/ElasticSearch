import './Pagination.css';

export default function Pagination({ currentPage, totalPages, onPageChange }) {
  const visiblePages = 5;
  const halfVisible = Math.floor(visiblePages / 2);
  let startPage = Math.max(1, currentPage - halfVisible);
  let endPage = Math.min(totalPages, currentPage + halfVisible);

  if (currentPage <= halfVisible) {
    endPage = Math.min(totalPages, visiblePages);
  }

  if (currentPage + halfVisible >= totalPages) {
    startPage = Math.max(1, totalPages - visiblePages + 1);
  }

  const pages = [];

  if (currentPage > 1) {
    pages.push(
      <button key="first" onClick={() => onPageChange(1)}>
        &laquo;
      </button>
    );
  }

  if (startPage > 1) {
    pages.push(<span key="start-ellipsis">...</span>);
  }

  for (let i = startPage; i <= endPage; i++) {
    pages.push(
      <button
        key={i}
        disabled={i === currentPage}
        onClick={() => onPageChange(i)}
      >
        {i}
      </button>
    );
  }

  if (endPage < totalPages) {
    pages.push(<span key="end-ellipsis">...</span>);
  }

  if (currentPage < totalPages) {
    pages.push(
      <button key="last" onClick={() => onPageChange(totalPages)}>
        &raquo;
      </button>
    );
  }

  return <div className="pagination">{pages}</div>;
}
