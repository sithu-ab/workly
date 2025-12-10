#!/bin/bash

# Example usage
# Rollback last version only: ./flywayRollback.sh
# Rollback from the last version to a specific version: ./flywayRollback.sh V02

# Define directories and variables
ROLLBACK_DIR="../src/main/resources/db/rollback"
START_FROM="$1"
SHOULD_PROCESS=false

# Load environment variables from .env if it exists
ENV_FILE="../.env"
if [ -f "$ENV_FILE" ]; then
  # Automatically export variables from .env
  set -a
  source "$ENV_FILE"
  set +a
fi

# Check if rollback directory exists
if [ ! -d "$ROLLBACK_DIR" ]; then
  echo "Error: Directory $ROLLBACK_DIR does not exist."
  exit 1
fi

# Get list of SQL files sorted alphabetically descending
# 2>/dev/null suppresses error if no files found
FILES=$(ls "$ROLLBACK_DIR"/*.sql 2>/dev/null | sort -r)

if [ -z "$FILES" ]; then
  echo "No SQL files found in $ROLLBACK_DIR"
  exit 0
fi

# If no start argument is provided, process only the first file (latest version)
if [ -z "$START_FROM" ]; then
  echo "No target version specified. Rolling back latest version only."
else
  echo "Targeting rollback down to version: $START_FROM"
fi

echo "Starting Rollback Process..."
echo "Database: ${DB_NAME}"

PROCESSED_COUNT=0

for FILE_PATH in $FILES; do
  FILE_NAME=$(basename "$FILE_PATH")

  echo "Processing $FILE_NAME..."

  # Extract version number from filename (e.g., V01.sql -> 01)
  VERSION=$(echo "$FILE_NAME" | sed -n 's/^V\([0-9]\+\)\.sql$/\1/p')

  if [ -n "$VERSION" ]; then
    # Check if version exists in flyway_schema_history before executing rollback
    COUNT=$(mysql -h "${DB_HOST:-localhost}" -P "${DB_PORT:-3306}" -u "${DB_USER:-root}" -p"${DB_PASS}" "${DB_NAME}" -N -B \
      -e "SELECT COUNT(*) FROM flyway_schema_history WHERE version = '$VERSION';" 2>&1 | grep -v "Warning")

    if [[ "$COUNT" =~ ^[0-9]+$ ]]; then
      if [ "$COUNT" -eq 0 ]; then
        echo "|__ Skipping: Version $VERSION not found in migration history"
        continue
      fi
    else
      echo "|__ Warning: Could not query flyway_schema_history table"
      exit 1
    fi
  fi

  # execute sql to db
  # Using environment variables from .env: DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASS
  mysql -h "${DB_HOST:-localhost}" -P "${DB_PORT:-3306}" -u "${DB_USER:-root}" -p"${DB_PASS}" "${DB_NAME}" < "$FILE_PATH" 2>/dev/null

  # Check execution status
  if [ $? -eq 0 ]; then
    echo "|__ Successfully executed $FILE_NAME"

    if [ -n "$VERSION" ]; then
      # Delete the corresponding entry from flyway_schema_history
      mysql -h "${DB_HOST:-localhost}" -P "${DB_PORT:-3306}" -u "${DB_USER:-root}" -p"${DB_PASS}" "${DB_NAME}" \
        -e "DELETE FROM flyway_schema_history WHERE version = '$VERSION';" 2>/dev/null

      if [ $? -ne 0 ]; then
        echo "|__ Warning: Failed to remove version $VERSION from flyway_schema_history"
      fi
    fi
  else
    echo "|__ Failed to execute $FILE_NAME"
    exit 1
  fi

  PROCESSED_COUNT=$((PROCESSED_COUNT + 1))

  # If no argument provided, stop after first file
  if [ -z "$START_FROM" ]; then
    echo "Rolled back one version. Stopping."
    break
  fi

  # If we just processed the target file, stop the rollback
  if [ -n "$START_FROM" ] && [[ "$FILE_NAME" == "$START_FROM"* ]]; then
    echo "Reached target version $START_FROM. Stopping rollback."
    break
  fi
done

echo "Rollback tasks finished."
