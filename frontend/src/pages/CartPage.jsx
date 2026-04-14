import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { createOrder } from '../api/watchApi';
import './CartPage.css';

const USER_ID = 'user-1';
const PLACEHOLDER_IMG = 'https://placehold.co/120x90/161616/c9a84c?text=AVEX';

const CartPage = () => {
  const { items, updateQuantity, removeItem, clearCart, totalPrice } = useCart();
  const navigate = useNavigate();
  const [orderLoading, setOrderLoading] = useState(false);
  const [orderSuccess, setOrderSuccess] = useState(false);
  const [orderError, setOrderError] = useState(null);

  const formatPrice = (p) =>
    new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(p);

  const handleCheckout = async () => {
    setOrderLoading(true);
    setOrderError(null);
    try {
      await createOrder(USER_ID);
      clearCart();
      setOrderSuccess(true);
    } catch {
      setOrderError('Unable to process order. Please ensure the backend is running on port 8080.');
    } finally {
      setOrderLoading(false);
    }
  };

  if (orderSuccess) {
    return (
      <div className="cart-page container">
        <div className="cart-page__success">
          <span className="cart-page__success-icon">✓</span>
          <h2>Order Placed Successfully!</h2>
          <p>Thank you for your purchase. Your timepiece is on its way.</p>
          <Link to="/" className="btn btn-primary" style={{ marginTop: 24 }}>
            Return Home
          </Link>
        </div>
      </div>
    );
  }

  if (items.length === 0) {
    return (
      <div className="cart-page container">
        <div className="cart-page__empty">
          <span className="cart-page__empty-icon">◎</span>
          <h2>Your Cart is Empty</h2>
          <p>Discover our collection of extraordinary timepieces.</p>
          <Link to="/collection" className="btn btn-primary" style={{ marginTop: 24 }}>
            Browse Collection
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="cart-page container">
      <div className="cart-page__header">
        <h1 className="section-title">
          Your <span>Cart</span>
        </h1>
        <div className="divider" />
        <p className="section-subtitle">{items.length} {items.length === 1 ? 'item' : 'items'}</p>
      </div>

      <div className="cart-page__layout">
        <div className="cart-page__items">
          {items.map((item) => (
            <div key={item.watchId} className="cart-item">
              <div className="cart-item__img-wrap">
                <img
                  src={item.imageUrl || PLACEHOLDER_IMG}
                  alt={item.name}
                  className="cart-item__img"
                  onError={(e) => { e.currentTarget.src = PLACEHOLDER_IMG; }}
                />
              </div>

              <div className="cart-item__info">
                <p className="cart-item__brand">{item.brand}</p>
                <h3 className="cart-item__name">
                  <Link to={`/watch/${item.watchId}`}>{item.name}</Link>
                </h3>
                <p className="cart-item__unit-price">{formatPrice(item.price)} each</p>
              </div>

              <div className="cart-item__controls">
                <div className="cart-item__quantity">
                  <button
                    className="qty-btn"
                    onClick={() => updateQuantity(item.watchId, item.quantity - 1)}
                    aria-label="Decrease"
                  >
                    −
                  </button>
                  <span className="qty-value">{item.quantity}</span>
                  <button
                    className="qty-btn"
                    onClick={() => updateQuantity(item.watchId, item.quantity + 1)}
                    aria-label="Increase"
                  >
                    +
                  </button>
                </div>
                <p className="cart-item__subtotal">{formatPrice(item.price * item.quantity)}</p>
                <button
                  className="btn btn-danger cart-item__remove"
                  onClick={() => removeItem(item.watchId)}
                  aria-label="Remove item"
                >
                  Remove
                </button>
              </div>
            </div>
          ))}

          <button className="btn btn-ghost cart-page__clear-btn" onClick={clearCart}>
            Clear Cart
          </button>
        </div>

        <div className="cart-page__summary">
          <h3 className="cart-page__summary-title">Order Summary</h3>

          <div className="cart-page__summary-rows">
            {items.map((item) => (
              <div key={item.watchId} className="summary-row">
                <span className="summary-row__label">
                  {item.name} × {item.quantity}
                </span>
                <span className="summary-row__value">
                  {formatPrice(item.price * item.quantity)}
                </span>
              </div>
            ))}
          </div>

          <div className="cart-page__summary-divider" />

          <div className="summary-row summary-row--total">
            <span>Total</span>
            <span>{formatPrice(totalPrice)}</span>
          </div>

          {orderError && <div className="error-banner" style={{ marginTop: 16 }}>{orderError}</div>}

          <button
            className="btn btn-primary cart-page__checkout-btn"
            onClick={handleCheckout}
            disabled={orderLoading}
          >
            {orderLoading ? 'Processing…' : 'Place Order'}
          </button>

          <Link to="/collection" className="btn btn-ghost cart-page__continue-btn">
            ← Continue Shopping
          </Link>
        </div>
      </div>
    </div>
  );
};

export default CartPage;
