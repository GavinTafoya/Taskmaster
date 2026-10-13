bash start.sh
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
touch mydatabase.db
if [ ! -f "mydatabase.db" ]; then
    die "Database file mydatabase.db could not be created."
fi

#jar check
JAR_FILE=$(ls target/*.jar 2>/dev/null | head -n 1)

if [ -z "$JAR_FILE" ] || [ ! -f "$JAR_FILE" ]; then
    die "Compiled JAR not found in target/. Build must have failed."
fi

echo ""
echo "===================================================="
echo "Application starting!"
echo "URL: http://localhost:8080"
echo "===================================================="
echo ""

# Start the application
java -jar "$JAR_FILE"