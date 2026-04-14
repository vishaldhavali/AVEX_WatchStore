import { createContext, useContext, useReducer, useEffect } from 'react';

const CartContext = createContext(null);

const STORAGE_KEY = 'avex_cart';

const loadFromStorage = () => {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : [];
  } catch {
    return [];
  }
};

const cartReducer = (state, action) => {
  switch (action.type) {
    case 'ADD_ITEM': {
      const existing = state.find((i) => i.watchId === action.payload.watchId);
      if (existing) {
        return state.map((i) =>
          i.watchId === action.payload.watchId
            ? { ...i, quantity: i.quantity + action.payload.quantity }
            : i
        );
      }
      return [...state, action.payload];
    }
    case 'UPDATE_QUANTITY': {
      if (action.payload.quantity <= 0) {
        return state.filter((i) => i.watchId !== action.payload.watchId);
      }
      return state.map((i) =>
        i.watchId === action.payload.watchId
          ? { ...i, quantity: action.payload.quantity }
          : i
      );
    }
    case 'REMOVE_ITEM':
      return state.filter((i) => i.watchId !== action.payload.watchId);
    case 'CLEAR_CART':
      return [];
    default:
      return state;
  }
};

export const CartProvider = ({ children }) => {
  const [items, dispatch] = useReducer(cartReducer, [], loadFromStorage);

  useEffect(() => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(items));
  }, [items]);

  const addItem = (watch, quantity = 1) => {
    dispatch({
      type: 'ADD_ITEM',
      payload: {
        watchId: watch.id,
        name: watch.name,
        brand: watch.brand,
        price: watch.price,
        imageUrl: watch.imageUrl,
        quantity,
      },
    });
  };

  const updateQuantity = (watchId, quantity) => {
    dispatch({ type: 'UPDATE_QUANTITY', payload: { watchId, quantity } });
  };

  const removeItem = (watchId) => {
    dispatch({ type: 'REMOVE_ITEM', payload: { watchId } });
  };

  const clearCart = () => {
    dispatch({ type: 'CLEAR_CART' });
  };

  const totalItems = items.reduce((sum, i) => sum + i.quantity, 0);
  const totalPrice = items.reduce((sum, i) => sum + i.price * i.quantity, 0);

  return (
    <CartContext.Provider
      value={{ items, addItem, updateQuantity, removeItem, clearCart, totalItems, totalPrice }}
    >
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const ctx = useContext(CartContext);
  if (!ctx) throw new Error('useCart must be used within CartProvider');
  return ctx;
};
