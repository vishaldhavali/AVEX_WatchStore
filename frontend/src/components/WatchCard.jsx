import { Link } from 'react-router-dom';
import './WatchCard.css';

const PLACEHOLDER_IMG = 'https://placehold.co/400x300/161616/c9a84c?text=AVEX';

const WatchCard = ({ watch }) => {
  const { id, name, brand, price, category, imageUrl, featured } = watch;

  const formatPrice = (p) =>
    new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(p);

  return (
    <Link to={`/watch/${id}`} className="watch-card">
      <div className="watch-card__img-wrap">
        <img
          src={imageUrl || PLACEHOLDER_IMG}
          alt={name}
          className="watch-card__img"
          onError={(e) => { e.currentTarget.src = PLACEHOLDER_IMG; }}
          loading="lazy"
        />
        {featured && <span className="watch-card__badge">Featured</span>}
        <span className="watch-card__category">{category}</span>
      </div>
      <div className="watch-card__body">
        <p className="watch-card__brand">{brand}</p>
        <h3 className="watch-card__name">{name}</h3>
        <p className="watch-card__price">{formatPrice(price)}</p>
      </div>
    </Link>
  );
};

export default WatchCard;
