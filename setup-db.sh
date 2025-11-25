#!/usr/bin/env bash
# Simple helper to load the schema into MySQL
# Usage:
#   ./setup-db.sh -u root -p password -h localhost -P 3306

set -euo pipefail

DB_USER="root"
DB_PASS=""
DB_HOST="localhost"
DB_PORT=3306

while [[ $# -gt 0 ]]; do
  case $1 in
    -u|--user) DB_USER="$2"; shift 2;;
    -p|--password) DB_PASS="$2"; shift 2;;
    -h|--host) DB_HOST="$2"; shift 2;;
    -P|--port) DB_PORT="$2"; shift 2;;
    *) echo "Unknown option: $1"; exit 1;;
  esac
done

if ! command -v mysql >/dev/null 2>&1; then
  echo "mysql client isn't installed or not in PATH. Please install MySQL client and try again." >&2
  exit 2
fi

echo "Applying create_database.sql to $DB_HOST:$DB_PORT as $DB_USER"
MYSQL_PWD="$DB_PASS" mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" < create_database.sql

echo "Done. If you haven't created db.properties yet, copy db.properties.example and update it to match your MySQL credentials."
