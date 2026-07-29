FROM php:8.4-apache

# Install system dependencies, Composer, and Node.js for Vite assets
RUN apt-get update && apt-get install -y \
    git \
    unzip \
    curl \
    libpq-dev \
    libzip-dev \
    && curl -fsSL https://deb.nodesource.com/setup_20.x | bash - \
    && apt-get install -y nodejs \
    && docker-php-ext-install pdo pdo_mysql pdo_pgsql zip

# Enable Apache Rewrite Module
RUN a2enmod rewrite

# Configure Apache DocumentRoot to point to Laravel's public directory inside backend-api
RUN sed -i 's!/var/www/html!/var/www/html/backend-api/public!g' /etc/apache2/sites-available/000-default.conf

# Add Directory permissions for Apache
RUN echo '<Directory /var/www/html/backend-api/public>\n\
    Options Indexes FollowSymLinks\n\
    AllowOverride All\n\
    Require all granted\n\
</Directory>' >> /etc/apache2/apache2.conf

# Get Composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

# Set working directory
WORKDIR /var/www/html

# Copy all project files
COPY . .

# Set up environment with forced HTTPS asset URL, build assets, and cache configs
RUN cd /var/www/html/backend-api \
    && cp .env.example .env \
    && sed -i 's/DB_CONNECTION=mysql/DB_CONNECTION=sqlite/' .env \
    && echo "APP_URL=https://uniforum-laravel-backend.onrender.com" >> .env \
    && echo "ASSET_URL=https://uniforum-laravel-backend.onrender.com" >> .env \
    && touch database/database.sqlite \
    && mkdir -p storage/framework/sessions storage/framework/views storage/framework/cache bootstrap/cache \
    && composer install --no-dev --optimize-autoloader --no-scripts \
    && npm install \
    && npm run build \
    && php artisan key:generate --force \
    && php artisan migrate --force \
    && php artisan config:clear \
    && php artisan view:clear \
    && chown -R www-data:www-data /var/www/html/backend-api \
    && chmod -R 775 storage bootstrap/cache

EXPOSE 80