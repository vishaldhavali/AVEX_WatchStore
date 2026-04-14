import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { CartProvider } from './context/CartContext';
import Header from './components/Header';
import Footer from './components/Footer';
import HomePage from './pages/HomePage';
import CollectionPage from './pages/CollectionPage';
import WatchDetailPage from './pages/WatchDetailPage';
import CartPage from './pages/CartPage';
import './App.css';

function App() {
  return (
    <BrowserRouter>
      <CartProvider>
        <div className="app">
          <Header />
          <main className="main-content">
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/collection" element={<CollectionPage />} />
              <Route path="/watch/:id" element={<WatchDetailPage />} />
              <Route path="/cart" element={<CartPage />} />
              <Route path="*" element={
                <div className="container" style={{ padding: '80px 24px', textAlign: 'center' }}>
                  <h2 style={{ fontFamily: 'var(--font-primary)', fontSize: '3rem', color: 'var(--color-accent)' }}>404</h2>
                  <p style={{ color: 'var(--color-text-muted)', marginTop: 12 }}>Page not found.</p>
                </div>
              } />
            </Routes>
          </main>
          <Footer />
        </div>
      </CartProvider>
    </BrowserRouter>
  );
}

export default App;
