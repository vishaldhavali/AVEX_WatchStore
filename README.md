# AVEX Watch Store

A full-stack e-commerce watch store application built with **React** (frontend) and **Java Spring Boot** (backend).

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React 18 + Vite, React Router v7, Context API |
| Backend | Java 17, Spring Boot 3.2, Spring Data JPA |
| Database | H2 (in-memory, development) |
| Styling | Plain CSS with CSS variables (dark luxury theme) |

---

## Project Structure

```
AVEX_WatchStore/
├── frontend/          # React application (Vite)
│   └── src/
│       ├── api/       # API service functions
│       ├── components/# Header, Footer, WatchCard, CategoryFilter
│       ├── context/   # CartContext (React Context + localStorage)
│       └── pages/     # Home, Collection, WatchDetail, Cart
└── backend/           # Spring Boot application
    └── src/main/java/com/avex/watchstore/
        ├── controller/# REST controllers (Watch, Cart, Order)
        ├── model/     # JPA entities (Watch, CartItem, Order, OrderItem)
        ├── repository/# Spring Data JPA repositories
        ├── service/   # Business logic layer
        ├── config/    # CORS configuration
        └── exception/ # Global exception handler
```

---

## Getting Started

### Prerequisites

- **Node.js** 18+ and npm
- **Java** 17+ and Maven 3.8+

### 1. Start the Backend

```bash
cd backend
mvn spring-boot:run
```

The API server starts on **http://localhost:8080**.

- H2 Console (database viewer): http://localhost:8080/h2-console  
  JDBC URL: `jdbc:h2:mem:avexdb`, Username: `sa`, Password: *(leave empty)*

### 2. Start the Frontend

```bash
cd frontend
npm install
npm run dev
```

The React app starts on **http://localhost:5173**.

---

## API Endpoints

### Watches

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/watches` | List all watches (supports `?category=` and `?featured=true`) |
| `GET` | `/api/watches/{id}` | Get single watch by ID |
| `POST` | `/api/watches` | Create a new watch |
| `PUT` | `/api/watches/{id}` | Update a watch |
| `DELETE` | `/api/watches/{id}` | Delete a watch |

### Cart

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/cart/{userId}` | Get all cart items for a user |
| `POST` | `/api/cart/{userId}/items` | Add item `{watchId, quantity}` |
| `PUT` | `/api/cart/{userId}/items/{itemId}` | Update item quantity `{quantity}` |
| `DELETE` | `/api/cart/{userId}/items/{itemId}` | Remove item from cart |
| `DELETE` | `/api/cart/{userId}` | Clear entire cart |

### Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/orders` | Create order from cart `{userId}` |
| `GET` | `/api/orders/{userId}` | Get order history for user |
| `GET` | `/api/orders/{userId}/{orderId}` | Get specific order details |

---

## Features

- **Product Catalog** — 12 pre-loaded watches across Luxury, Sport, Casual, and Smart categories
- **Featured Watches** — Highlighted picks shown on the home page hero section
- **Category Filtering** — Filter the collection by watch category
- **Shopping Cart** — Add/remove items, adjust quantities, persisted via localStorage
- **Checkout Flow** — Place orders; stock is automatically decremented on purchase
- **Dark Luxury Theme** — Black & gold color palette with smooth hover effects
- **Responsive Design** — Works on desktop and mobile screens
- **Error Handling** — Graceful error messages when the backend is unavailable

---

## Watch Brands (Sample Data)

| Brand | Category | Price Range |
|-------|----------|-------------|
| Rolex | Luxury | $9,550 |
| Omega | Luxury | $5,200–$6,300 |
| TAG Heuer | Luxury | $3,800 |
| Tissot | Casual | $750 |
| Seiko | Sport | $275–$450 |
| Casio | Sport | $99 |
| Apple | Smart | $399 |
| Samsung | Smart | $299 |
| Fossil | Casual | $95–$129 |

---

## Build for Production

```bash
# Frontend
cd frontend
npm run build      # Output in frontend/dist/

# Backend
cd backend
mvn clean package  # Output in backend/target/*.jar
java -jar target/avex-watchstore-*.jar
```