const API_BASE = 'http://localhost:8080/api';

const handleResponse = async (res) => {
  if (!res.ok) {
    const text = await res.text().catch(() => 'Unknown error');
    throw new Error(`HTTP ${res.status}: ${text}`);
  }
  return res.json();
};

export const fetchWatches = (params = {}) => {
  const query = new URLSearchParams(params).toString();
  return fetch(`${API_BASE}/watches${query ? `?${query}` : ''}`)
    .then(handleResponse);
};

export const fetchWatch = (id) =>
  fetch(`${API_BASE}/watches/${id}`).then(handleResponse);

export const fetchCart = (userId) =>
  fetch(`${API_BASE}/cart/${userId}`).then(handleResponse);

export const addToCart = (userId, watchId, quantity) =>
  fetch(`${API_BASE}/cart/${userId}/items`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ watchId, quantity }),
  }).then(handleResponse);

export const updateCartItem = (userId, itemId, quantity) =>
  fetch(`${API_BASE}/cart/${userId}/items/${itemId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ quantity }),
  }).then(handleResponse);

export const removeFromCart = (userId, itemId) =>
  fetch(`${API_BASE}/cart/${userId}/items/${itemId}`, {
    method: 'DELETE',
  }).then(handleResponse);

export const clearCart = (userId) =>
  fetch(`${API_BASE}/cart/${userId}`, {
    method: 'DELETE',
  }).then(handleResponse);

export const createOrder = (userId) =>
  fetch(`${API_BASE}/orders`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ userId }),
  }).then(handleResponse);
