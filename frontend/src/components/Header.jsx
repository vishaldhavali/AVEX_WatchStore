import { useState, useEffect } from 'react';
import { Link, NavLink } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import './Header.css';

const Header = () => {
  const { totalItems } = useCart();
  const [scrolled, setScrolled] = useState(false);
  const [menuOpen, setMenuOpen] = useState(false);

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener('scroll', onScroll);
    return () => window.removeEventListener('scroll', onScroll);
  }, []);

  return (
    <header className={`header${scrolled ? ' header--scrolled' : ''}`}>
      <div className="header__inner container">
        <Link to="/" className="header__logo">
          <span className="header__logo-text">AVEX</span>
          <span className="header__logo-tagline">Timepieces of Distinction</span>
        </Link>

        <button
          className={`header__hamburger${menuOpen ? ' open' : ''}`}
          onClick={() => setMenuOpen((v) => !v)}
          aria-label="Toggle menu"
        >
          <span />
          <span />
          <span />
        </button>

        <nav className={`header__nav${menuOpen ? ' header__nav--open' : ''}`}>
          <NavLink
            to="/"
            end
            className={({ isActive }) => `header__link${isActive ? ' active' : ''}`}
            onClick={() => setMenuOpen(false)}
          >
            Home
          </NavLink>
          <NavLink
            to="/collection"
            className={({ isActive }) => `header__link${isActive ? ' active' : ''}`}
            onClick={() => setMenuOpen(false)}
          >
            Collection
          </NavLink>
          <NavLink
            to="/cart"
            className={({ isActive }) => `header__link header__cart${isActive ? ' active' : ''}`}
            onClick={() => setMenuOpen(false)}
          >
            Cart
            {totalItems > 0 && (
              <span className="header__cart-badge">{totalItems}</span>
            )}
          </NavLink>
        </nav>
      </div>
    </header>
  );
};

export default Header;
