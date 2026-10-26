#!/bin/bash
# Exit immediately if a command exits with a non-zero status
set -e

echo "Updating system and installing Java 25, Nginx, & PostgreSQL..."
apt-get update
apt-get install -y openjdk-25-jdk nginx postgresql postgresql-contrib

echo "Configuring PostgreSQL Database..."
systemctl enable postgresql
systemctl start postgresql

# Set the postgres user password to 'postgres' (matches your application.properties)
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'postgres';"

# Create the 'taskmaster' database if it doesn't already exist
sudo -u postgres psql -tc "SELECT 1 FROM pg_database WHERE datname = 'taskmaster'" | grep -q 1 || sudo -u postgres psql -c "CREATE DATABASE taskmaster;"

echo "Creating application directory..."
mkdir -p /opt/taskmaster
chown ubuntu:ubuntu /opt/taskmaster

echo "Configuring Nginx..."
cp deploy/nginx-taskmaster.conf /etc/nginx/sites-available/taskmaster
ln -sf /etc/nginx/sites-available/taskmaster /etc/nginx/sites-enabled/
rm -f /etc/nginx/sites-enabled/default
systemctl restart nginx

echo "Configuring Systemd Service..."
cp deploy/taskmaster.service /etc/systemd/system/
systemctl daemon-reload
systemctl enable taskmaster

echo "EC2 Setup Complete! You can now run deploy.sh from your laptop."