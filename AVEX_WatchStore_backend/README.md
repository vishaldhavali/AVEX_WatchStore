# AVEX WatchStore - Backend

Spring Boot RESTful API backend for the AVEX WatchStore e-commerce platform.

## 🏗️ Project Setup

### Prerequisites

- Java 25 or higher
- Maven 3.6+
- MySQL 8.0+

### Quick Start

1. **Copy configuration file**:

   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

2. **Update `application.properties`** with your credentials:

   ```properties
   # Database
   spring.datasource.url=jdbc:mysql://localhost:3306/salessavvy
   spring.datasource.username=your_username
   spring.datasource.password=your_password

   # JWT Secret (generate: openssl rand -base64 32)
   jwt.secret=your_strong_secret_key

   # Razorpay API Keys
   RazorKey.ID=your_key_id
   RazorKey.Secret=your_secret

   # Gmail SMTP (use App Password, not main password)
   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_app_password
   ```

3. **Build and run**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

Server runs on: `http://localhost:8080`

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/salesavvy/app/
│   │   ├── Controllers/          # REST API endpoints
│   │   ├── entites/              # Entity models (JPA)
│   │   ├── userRepositories/     # Data access layer
│   │   ├── userServices/         # Business logic
│   │   └── SalessavvyBackendApplication.java
│   │
│   └── resources/
│       ├── application.properties     # (excluded from git)
│       ├── application.properties.example
│       ├── static/               # Static files
│       └── templates/            # HTML templates
│
└── test/java/                   # Unit tests
```

## 🔌 API Endpoints

### Products API

```
GET    /api/products                 # Get all products (optional filter by category)
GET    /api/products/{productId}     # Get product details
```

### Authentication API

```
POST   /api/auth/register            # Register new user
POST   /api/auth/login               # User login
POST   /api/auth/verify-otp          # Verify OTP
```

### Orders API

```
GET    /api/orders                   # Get user orders
POST   /api/orders                   # Create new order
GET    /api/orders/{orderId}         # Get order details
```

### Cart API

```
GET    /api/cart                     # Get cart items
POST   /api/cart                     # Add item to cart
PUT    /api/cart/{itemId}            # Update cart item
DELETE /api/cart/{itemId}            # Remove from cart
```

## 🔐 Environment Variables

Create `application.properties` from `application.properties.example`:

| Variable                     | Description          | Example                                  |
| ---------------------------- | -------------------- | ---------------------------------------- |
| `spring.datasource.url`      | MySQL connection URL | `jdbc:mysql://localhost:3306/salessavvy` |
| `spring.datasource.username` | Database user        | `root`                                   |
| `spring.datasource.password` | Database password    | `your_password`                          |
| `jwt.secret`                 | JWT signing secret   | `generated_random_32_char_string`        |
| `RazorKey.ID`                | Razorpay public key  | `rzp_test_xxxxx`                         |
| `RazorKey.Secret`            | Razorpay secret key  | `xxxxx`                                  |
| `spring.mail.username`       | Gmail address        | `your_email@gmail.com`                   |
| `spring.mail.password`       | Gmail App Password   | `xxxx xxxx xxxx xxxx`                    |

## 🧪 Testing

Run tests with Maven:

```bash
mvn test
```

Test reports available in: `target/surefire-reports/`

## 🏗️ Key Technologies

- **Framework**: Spring Boot 4.0.5
- **Language**: Java 25
- **Database**: MySQL with JPA/Hibernate
- **Authentication**: JWT (JSON Web Token)
- **Payment**: Razorpay SDK
- **Email**: Spring Mail with Gmail SMTP
- **Validation**: Jakarta Validation API

## 🛠️ Building

### Development

```bash
# Build without running
mvn clean install

# Build with tests
mvn clean verify

# Run directly
mvn spring-boot:run
```

### Production

```bash
# Create JAR file
mvn clean package

# Run JAR
java -jar target/AVEX_WatchStore_backend-beta_v1.jar
```

## 📊 Database Setup

1. Create MySQL database:

   ```sql
   CREATE DATABASE salessavvy;
   ```

2. Hibernate will auto-create tables based on entity definitions

3. Database schema includes:
   - Users (with roles)
   - Products
   - Categories
   - Orders
   - Cart Items
   - Product Images

## 🔒 Security Notes

### ⚠️ Never Commit

- `application.properties` with real credentials
- `.env` files
- Private keys

### Configuration Best Practices

- Generate strong JWT secrets: `openssl rand -base64 32`
- Use OAuth app passwords for Gmail (not your main password)
- Keep Razorpay keys secret on backend only
- Implement HTTPS in production
- Use HTTPS for database connections in production
- Review CORS configuration before deploying

### Sensitive File Exclusion

The `.gitignore` file excludes:

```
src/main/resources/application.properties
.env
.env.local
*.sql
```

## 📝 Configuration Examples

### JWT Configuration

```properties
# Generate: openssl rand -base64 32
jwt.secret=your_strong_jwt_secret_key_here_minimum_32_characters
```

### Database Configuration

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/salessavvy
spring.datasource.username=root
spring.datasource.password=your_secure_password
spring.jpa.hibernate.ddl-auto=update  # 'validate' for production
```

### Email Configuration

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_specific_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## 🐛 Troubleshooting

### Database Connection Issues

- Ensure MySQL is running: `mysql -u root -p`
- Check database URL in `application.properties`
- Verify username/password credentials

### Email Not Sending

- Use Gmail App Password, not your main password
- Enable "Less secure app access" or use 2FA with App Passwords
- Check email credentials in `application.properties`

### Port Already in Use

```bash
# Build runs on 8080, change:
server.port=8081
```

## 📖 Documentation

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Razorpay Integration](https://razorpay.com/docs/)
- [JWT Authentication](https://tools.ietf.org/html/rfc7519)

## 👤 Author

**Vishal Dhavali**

## 📄 License

Proprietary - AVEX WatchStore
