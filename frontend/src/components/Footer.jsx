import { Link } from 'react-router-dom';
import './Footer.css';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer__inner container">
        <div className="footer__brand">
          <span className="footer__logo">AVEX</span>
          <p className="footer__tagline">Timepieces of Distinction</p>
          <p className="footer__desc">
            Curating the world's finest timepieces for the discerning collector.
            Every watch tells a story of craftsmanship and elegance.
          </p>
        </div>

        <div className="footer__col">
          <h4 className="footer__heading">Shop</h4>
          <ul className="footer__links">
            <li><Link to="/collection">All Watches</Link></li>
            <li><Link to="/collection?category=LUXURY">Luxury</Link></li>
            <li><Link to="/collection?category=SPORT">Sport</Link></li>
            <li><Link to="/collection?category=SMART">Smart</Link></li>
          </ul>
        </div>

        <div className="footer__col">
          <h4 className="footer__heading">Support</h4>
          <ul className="footer__links">
            <li><Link to="/cart">My Cart</Link></li>
            <li><a href="mailto:support@avex.com">Contact Us</a></li>
          </ul>
        </div>
      </div>

      <div className="footer__bottom">
        <p className="footer__copy">
          &copy; {new Date().getFullYear()} AVEX Watch Store. All rights reserved.
        </p>
      </div>
    </footer>
  );
};

export default Footer;
