import { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { fetchWatches } from '../api/watchApi';
import WatchCard from '../components/WatchCard';
import CategoryFilter from '../components/CategoryFilter';
import './CollectionPage.css';

const CollectionPage = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const initialCategory = searchParams.get('category') || 'ALL';

  const [watches, setWatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(initialCategory);
  const [searchQuery, setSearchQuery] = useState('');

  useEffect(() => {
    setLoading(true);
    setError(null);
    const params = {};
    if (selectedCategory !== 'ALL') params.category = selectedCategory;

    fetchWatches(params)
      .then((data) => {
        setWatches(Array.isArray(data) ? data : data.content ?? []);
        setLoading(false);
      })
      .catch(() => {
        setError('Unable to connect to server. Please ensure the backend is running on port 8080.');
        setLoading(false);
      });
  }, [selectedCategory]);

  const handleCategoryChange = (cat) => {
    setSelectedCategory(cat);
    if (cat === 'ALL') {
      setSearchParams({});
    } else {
      setSearchParams({ category: cat });
    }
  };

  const filteredWatches = watches.filter((w) => {
    if (!searchQuery.trim()) return true;
    const q = searchQuery.toLowerCase();
    return (
      w.name?.toLowerCase().includes(q) ||
      w.brand?.toLowerCase().includes(q)
    );
  });

  return (
    <div className="collection-page container">
      <div className="collection-page__header">
        <h1 className="section-title">
          The <span>Collection</span>
        </h1>
        <div className="divider" />
        <p className="section-subtitle">
          {selectedCategory === 'ALL'
            ? 'Every timepiece in our curated selection'
            : `${selectedCategory.charAt(0) + selectedCategory.slice(1).toLowerCase()} collection`}
        </p>
      </div>

      <div className="collection-page__controls">
        <CategoryFilter selected={selectedCategory} onChange={handleCategoryChange} />
        <input
          className="collection-page__search"
          type="text"
          placeholder="Search by name or brand…"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          aria-label="Search watches"
        />
      </div>

      {loading && <div className="loading-spinner">Loading</div>}
      {error && <div className="error-banner">{error}</div>}

      {!loading && !error && filteredWatches.length === 0 && (
        <div className="collection-page__empty">
          <p>No watches found{searchQuery ? ` matching "${searchQuery}"` : ''}.</p>
        </div>
      )}

      {!loading && !error && filteredWatches.length > 0 && (
        <>
          <p className="collection-page__count">
            {filteredWatches.length} {filteredWatches.length === 1 ? 'watch' : 'watches'} found
          </p>
          <div className="watches-grid collection-grid">
            {filteredWatches.map((w) => (
              <WatchCard key={w.id} watch={w} />
            ))}
          </div>
        </>
      )}
    </div>
  );
};

export default CollectionPage;
