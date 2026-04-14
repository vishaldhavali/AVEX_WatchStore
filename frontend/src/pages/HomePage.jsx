import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { fetchWatches } from '../api/watchApi';
import WatchCard from '../components/WatchCard';
import './HomePage.css';

const CATEGORIES = [
  { label: 'Luxury', value: 'LUXURY', desc: 'Exceptional craftsmanship for the connoisseur.' },
  { label: 'Sport', value: 'SPORT', desc: 'Precision engineered for peak performance.' },
  { label: 'Casual', value: 'CASUAL', desc: 'Effortless elegance for everyday wear.' },
  { label: 'Smart', value: 'SMART', desc: 'Intelligent timekeeping for the modern era.' },
];

const HomePage = () => {
  const [featured, setFeatured] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchWatches({ featured: true })
      .then((data) => {
        setFeatured(Array.isArray(data) ? data : data.content ?? []);
        setLoading(false);
      })
      .catch(() => {
        setError('Unable to connect to server. Please ensure the backend is running on port 8080.');
        setLoading(false);
      });
  }, []);

  return (
    <div className="home-page">
      {/* Hero */}
      <section className="hero">
        <div className="hero__overlay" />
        <div className="hero__content container">
          <p className="hero__eyebrow">Est. 2024</p>
          <h1 className="hero__title">
            The Art of <span>Time</span>
          </h1>
          <p className="hero__subtitle">
            Discover extraordinary timepieces crafted for those who value
            precision, heritage, and refined taste.
          </p>
          <div className="hero__actions">
            <Link to="/collection" className="btn btn-primary">
              Explore Collection
            </Link>
            <a href="#featured" className="btn btn-secondary">
              Featured Watches
            </a>
          </div>
        </div>
        <div className="hero__scroll-hint">
          <span>Scroll</span>
          <div className="hero__scroll-line" />
        </div>
      </section>

      {/* Featured Watches */}
      <section id="featured" className="section-featured container">
        <div className="section-header">
          <h2 className="section-title">
            Featured <span>Timepieces</span>
          </h2>
          <div className="divider" />
          <p className="section-subtitle">
            Hand-selected from our most prestigious collection
          </p>
        </div>

        {loading && <div className="loading-spinner">Loading</div>}
        {error && <div className="error-banner">{error}</div>}
        {!loading && !error && featured.length === 0 && (
          <p className="home-page__empty">No featured watches available at the moment.</p>
        )}

        {!loading && !error && featured.length > 0 && (
          <div className="watches-grid">
            {featured.slice(0, 4).map((w) => (
              <WatchCard key={w.id} watch={w} />
            ))}
          </div>
        )}

        <div className="section-footer">
          <Link to="/collection" className="btn btn-secondary">
            View Full Collection
          </Link>
        </div>
      </section>

      {/* Categories */}
      <section className="section-categories">
        <div className="container">
          <div className="section-header">
            <h2 className="section-title">
              Shop by <span>Category</span>
            </h2>
            <div className="divider" />
          </div>
          <div className="categories-grid">
            {CATEGORIES.map((cat) => (
              <Link
                key={cat.value}
                to={`/collection?category=${cat.value}`}
                className="category-card"
              >
                <span className="category-card__label">{cat.label}</span>
                <p className="category-card__desc">{cat.desc}</p>
                <span className="category-card__arrow">→</span>
              </Link>
            ))}
          </div>
        </div>
      </section>

      {/* Promise Section */}
      <section className="section-promise container">
        <div className="promise-grid">
          {[
            { icon: '◎', title: 'Authentic Guarantee', desc: 'Every timepiece is certified genuine.' },
            { icon: '⧖', title: 'Expert Curation', desc: 'Carefully selected by watch specialists.' },
            { icon: '⬡', title: 'White Glove Service', desc: 'Concierge support from purchase to delivery.' },
          ].map((item) => (
            <div key={item.title} className="promise-card">
              <span className="promise-card__icon">{item.icon}</span>
              <h3 className="promise-card__title">{item.title}</h3>
              <p className="promise-card__desc">{item.desc}</p>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
};

export default HomePage;
