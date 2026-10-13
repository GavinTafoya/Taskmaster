#!/bin/bash
# Exit immediately if a command exits with a non-zero status
set -e

echo "Updating system and installing Java 17 & Nginx..."
apt-get update
apt-get install -y openjdk-17-jdk nginx

echo "Creating application directory..."
mkdir -p /opt/taskmaster
# Give the default ubuntu user ownership of the directory
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