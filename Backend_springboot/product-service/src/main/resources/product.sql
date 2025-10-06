-- 创建商品表
CREATE TABLE IF NOT EXISTS products (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    category VARCHAR(100),
    stock_quantity INTEGER DEFAULT 0 CHECK (stock_quantity >= 0),
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- 创建分类表
CREATE TABLE IF NOT EXISTS categories (
                                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- 插入示例数据
INSERT INTO categories (name, description) VALUES
                                               ('Electronics', 'Electronic devices and accessories'),
                                               ('Clothing', 'Fashion and apparel'),
                                               ('Books', 'Books and literature'),
                                               ('Home & Garden', 'Home and garden products')
    ON CONFLICT (name) DO NOTHING;

INSERT INTO products (name, description, price, category, stock_quantity) VALUES
                                                                              ('iPhone 15', 'Latest Apple smartphone', 999.99, 'Electronics', 50),
                                                                              ('MacBook Pro', 'Apple laptop computer', 1999.99, 'Electronics', 25),
                                                                              ('T-Shirt', 'Cotton t-shirt', 19.99, 'Clothing', 100),
                                                                              ('Java Programming', 'Learn Java programming', 49.99, 'Books', 30)
    ON CONFLICT DO NOTHING;

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);
CREATE INDEX IF NOT EXISTS idx_products_price ON products(price);
CREATE INDEX IF NOT EXISTS idx_products_name ON products(name);