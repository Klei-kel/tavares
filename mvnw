#!/usr/bin/env sh
MAVEN_PROJECTBASEDIR="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_DIR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper"
WRAPPER_JAR="$WRAPPER_DIR/maven-wrapper.jar"
WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
  mkdir -p "$WRAPPER_DIR"
  echo "Downloading Maven Wrapper..."
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$WRAPPER_URL" -o "$WRAPPER_JAR"
  else
    wget -q "$WRAPPER_URL" -O "$WRAPPER_JAR"
  fi
fi

exec java -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" -jar "$WRAPPER_JAR" "$@"
