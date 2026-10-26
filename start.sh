#!/usr/bin/env bash
die() {
    echo "ERROR: $1" >&2
    exit 1
}

REQUIRED_JAVA_MAJOR=17

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"


# java check
command java -version >/dev/null 2>&1 \
    || die "Java is not installed. Get it from https://www.java.com (version ${REQUIRED_JAVA_MAJOR} or newer)."

JAVA_VERSION_STRING=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
JAVA_MAJOR_VERSION=$(echo "$JAVA_VERSION_STRING" | awk -F. '{print $1}')
if [ "$JAVA_MAJOR_VERSION" -lt "$REQUIRED_JAVA_MAJOR" ]; then
    die "Java version $REQUIRED_JAVA_MAJOR or newer is required. Found version $JAVA_VERSION_STRING."
fi


# maven check
if [ ! -f "./mvnw" ]; then
    die "Maven wrapper (./mvnw) not found. Are you in the right directory?"
fi

chmod +x ./mvnw

./mvnw clean package -DskipTests || die "Build failed."


#db check
pg_isready -q || echo "Warning: Local PostgreSQL not detected"

# Open SSH Tunnel to EC2 Database
echo "Opening SSH tunnel to EC2 database..."
# Use StrictHostKeyChecking=no to prevent it from hanging on yes/no prompts
ssh -f -N -o StrictHostKeyChecking=no -i ./seckey.pem -L 5433:localhost:5432 ubuntu@32.185.224.45

# Find the PID so we can kill it later
TUNNEL_PID=$(pgrep -f "ssh -f -N -o StrictHostKeyChecking=no -i ./seckey.pem -L 5433:localhost:5432")

# Setup a trap to kill the tunnel when the script exits
trap "echo 'Closing SSH tunnel...'; kill $TUNNEL_PID 2>/dev/null" EXIT

# Give the tunnel a brief moment to establish
sleep 2

#jar check
JAR_FILE=$(ls target/*.jar 2>/dev/null | head -n 1)

echo ""
echo "===================================================="
echo "Application starting!"
echo "URL: http://localhost:8080"
echo "===================================================="
echo ""

# Start the application
java -jar "$JAR_FILE"