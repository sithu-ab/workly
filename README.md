# workly

A sample project management application (API development) that uses Java Spring Boot and MySQL.

## Installation

Copy `.env.example` to `.env` and update the values.

## Run

    ./gradlew bootRun

## Migration Forward

    ./gradlew flywayInfo        # check migration info
    ./gradlew flywayMigrate     # run migration to the latest version

## Migration Backward

    cd tasks
    ./flywayRollback.sh         # to rollback the last migration
    ./flywayRollback.sh V02     # to rollback to a specific version from the last migration
