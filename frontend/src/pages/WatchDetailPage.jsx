import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { fetchWatch } from '../api/watchApi';
import { useCart } from '../context/CartContext';
import './WatchDetailPage.css';

const PLACEHOLDER_IMG = 'https://placehold.co/600x480/161616/c9a84c?text=AVEX';

const WatchDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { addItem } = useCart();

  const [watch, setWatch] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [added, setAdded] = useState(false);

  useEffect(() => {
    fetchWatch(id)
      .then((data) => {
        setWatch(data);
        setLoading(false);
      })
      .catch(() => {
        setError('Unable to connect to server. Please ensure the backend is running on port 8080.');
        setLoading(false);
      });
  }, [id]);

  const handleAddToCart = () => {
    if (!watch) return;
    addItem(watch, quantity);
    setAdded(true);
    setTimeout(() => setAdded(false), 2500);
  };

  const formatPrice = (p) =>
    new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(p);

  if (loading) return <div className="loading-spinner">Loading</div>;
  if (error) return (
    <div className="detail-page container">
      <div className="error-banner">{error}</div>
      <Link to="/collection" className="btn btn-ghost" style={{ marginTop: 20 }}>← Back to Collection</Link>
    </div>
  );
  if (!watch) return null;

  return (
    <div className="detail-page container">
      <nav className="detail-page__breadcrumb">
        <Link to="/">Home</Link>
        <span>›</span>
        <Link to="/collection">Collection</Link>
        <span>›</span>
        <span>{watch.name}</span>
      </nav>

      <div className="detail-page__layout">
        <div className="detail-page__gallery">
          <div className="detail-page__img-wrap">
            <img
              src={watch.imageUrl || PLACEHOLDER_IMG}
              alt={watch.name}
              className="detail-page__img"
              onError={(e) => { e.currentTarget.src = PLACEHOLDER_IMG; }}
            />
            {watch.featured && <span className="detail-page__featured-badge">Featured</span>}
          </div>
        </div>

        <div className="detail-page__info">
          <span className="detail-page__category">{watch.category}</span>
          <p className="detail-page__brand">{watch.brand}</p>
          <h1 className="detail-page__name">{watch.name}</h1>
          <p className="detail-page__price">{formatPrice(watch.price)}</p>

          {watch.description && (
            <p className="detail-page__description">{watch.description}</p>
          )}

          {watch.stockQuantity !== undefined && (
            <p className={`detail-page__stock ${watch.stockQuantity > 0 ? 'in-stock' : 'out-of-stock'}`}>
              {watch.stockQuantity > 0
                ? `In Stock — ${watch.stockQuantity} available`
                : 'Out of Stock'}
            </p>
          )}

          <div className="detail-page__add-section">
            <div className="detail-page__quantity">
              <button
                className="qty-btn"
                onClick={() => setQuantity((q) => Math.max(1, q - 1))}
                aria-label="Decrease quantity"
              >
                −
              </button>
              <span className="qty-value">{quantity}</span>
              <button
                className="qty-btn"
                onClick={() => setQuantity((q) => Math.min(watch.stockQuantity ?? 99, q + 1))}
                aria-label="Increase quantity"
              >
                +
              </button>
            </div>

            <button
              className={`btn btn-primary detail-page__add-btn ${added ? 'added' : ''}`}
              onClick={handleAddToCart}
              disabled={watch.stockQuantity === 0}
            >
              {added ? '✓ Added to Cart' : 'Add to Cart'}
            </button>
          </div>

          <button
            className="btn btn-ghost detail-page__back-btn"
            onClick={() => navigate(-1)}
          >
            ← Back
          </button>
        </div>
      </div>
    </div>
  );
};

export default WatchDetailPage;
