https://security-6502.postman.co/workspace/Ecommerce~6e5bc47d-e198-420c-8297-1b22770e6714/collection/22474564-a5582a99-4513-4651-aa14-c34092019dee?action=share&creator=22474564



-- Roles
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(20) NOT NULL
);

-- Users
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL
);

-- User-Role join table
CREATE TABLE user_role (
    user_id BIGINT NOT NULL REFERENCES users(user_id),
    role_id INTEGER NOT NULL REFERENCES roles(role_id),
    PRIMARY KEY (user_id, role_id)
);

-- Categories
CREATE TABLE categories (
    category_id BIGSERIAL PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL
);

-- Products (uses GenerationType.AUTO → needs a sequence)
CREATE SEQUENCE products_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE products (
    product_id BIGINT NOT NULL DEFAULT nextval('products_seq') PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    description VARCHAR(255) NOT NULL,
    quantity INTEGER,
    price DOUBLE PRECISION NOT NULL DEFAULT 0,
    discount DOUBLE PRECISION NOT NULL DEFAULT 0,
    special_price DOUBLE PRECISION NOT NULL DEFAULT 0,
    category_id BIGINT REFERENCES categories(category_id),
    seller_id BIGINT REFERENCES users(user_id)
);

-- Addresses
CREATE TABLE addresses (
    address_id BIGSERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    building_name VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    pincode VARCHAR(255) NOT NULL,
    user_id BIGINT REFERENCES users(user_id)
);

-- Carts
CREATE TABLE carts (
    cart_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id),
    totalprice DOUBLE PRECISION NOT NULL DEFAULT 0
);

-- Cart Items
CREATE TABLE cart_items (
    cart_item_id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT REFERENCES carts(cart_id),
    product_id BIGINT REFERENCES products(product_id),
    quantity INTEGER,
    discount DOUBLE PRECISION,
    product_price DOUBLE PRECISION
);

-- Payments
CREATE TABLE payments (
    payment_id BIGSERIAL PRIMARY KEY,
    payment_method VARCHAR(255) NOT NULL,
    pg_payment_id VARCHAR(255),
    pg_status VARCHAR(255),
    pg_response_message VARCHAR(255),
    pg_name VARCHAR(255)
);

-- Orders
CREATE TABLE orders (
    order_id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    order_date DATE,
    total_amount DOUBLE PRECISION,
    order_status VARCHAR(255),
    payment_id BIGINT REFERENCES payments(payment_id),
    address_id BIGINT REFERENCES addresses(address_id)
);

-- Order Items
CREATE TABLE order_items (
    order_item_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES products(product_id),
    order_id BIGINT REFERENCES orders(order_id),
    quantity INTEGER,
    discount DOUBLE PRECISION NOT NULL DEFAULT 0,
    ordered_product_price DOUBLE PRECISION NOT NULL DEFAULT 0
);

-- Seed default roles
INSERT INTO roles (role_name) VALUES ('ROLE_USER'), ('ROLE_ADMIN'), ('ROLE_SELLER');
