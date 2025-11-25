#!/usr/bin/env bash
set -euo pipefail

# Helper: compile and run this project using JDK 8
# Usage:
#   ./run-java8.sh [Main|TestDBConnection]
# If JAVA_HOME is set, it's used. Otherwise the default system javac/java are used.

CMD=${1:-Main}

if [[ -n "${JAVA_HOME:-}" ]]; then
  JAVAC="$JAVA_HOME/bin/javac"
  JAVA="$JAVA_HOME/bin/java"
else
  JAVAC="javac"
  JAVA="java"
fi

echo "Using javac: $(command -v ${JAVAC} || echo none)"
echo "Using java:  $(command -v ${JAVA} || echo none)"

if ! command -v ${JAVAC} >/dev/null 2>&1; then
  echo "javac not found. Make sure JAVA_HOME points to JDK8 or javac is in PATH." >&2
  exit 2
fi

mkdir -p bin

CLASSPATH="bin:lib/*"

# Compile all sources targeting Java 8
echo "Compiling (source=1.8 target=1.8)..."
${JAVAC} -source 1.8 -target 1.8 -d bin -cp "lib/*" src/*.java

echo "Running $CMD"
${JAVA} -cp "$CLASSPATH" "$CMD"
