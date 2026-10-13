#!/usr/bin/env bash
set -e

# Parse command line arguments
while getopts h:i: flag
do
    case "${flag}" in
        h) HOST=${OPTARG};;
        i) KEY=${OPTARG};;
    esac
done

if [ -z "$HOST" ] || [ -z "$KEY" ]; then
    echo "Usage: ./deploy.sh -h <PUBLIC-IP> -i <PATH-TO-KEY>"
    exit 1
fi

echo "1. Running tests locally..."
./mvnw test

echo "2. Copying code to server..."
# Copy everything EXCEPT target (build output) and .git (history)
rsync -avz -e "ssh -i $KEY -o StrictHostKeyChecking=no" \
    --exclude 'target' \
    --exclude '.git' \
    ./ ubuntu@$HOST:/opt/taskmaster/

echo "3. Building on the server and restarting service..."
ssh -i "$KEY" -o StrictHostKeyChecking=no ubuntu@$HOST << 'EOF'
    cd /opt/taskmaster
    echo "Building project..."
    ./mvnw clean package -DskipTests
    echo "Restarting taskmaster service..."
    sudo systemctl restart taskmaster
EOF

echo "4. Waiting for app to start..."
sleep 15

echo "5. Checking health..."
# Modify this URL if you have a specific /api/health endpoint
STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://$HOST/)

if [ "$STATUS" = "200" ]; then
    echo "✅ Deploy successful! App is running at http://$HOST/"
else
    echo "❌ Deploy failed. HTTP status: $STATUS"
    exit 1
fi