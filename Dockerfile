FROM php:8.4-apache

# Install system dependencies & Composer
RUN apt-get update && apt-get install -y \
    git \
    unzip \
    libpq-dev \
    libzip-dev \
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

# Install composer dependencies inside backend-api and set permissions
RUN cd /var/www/html/backend-api \
    && mkdir -p storage bootstrap/cache \
    && composer install --no-dev --optimize-autoloader \
    && chown -R www-data:www-data storage bootstrap/cache

EXPOSE 80