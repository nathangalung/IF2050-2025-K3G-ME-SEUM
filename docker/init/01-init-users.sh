#!/bin/bash
# Database initialization script for ME-SEUM PostgreSQL Docker container

echo "========================================"
echo "ME-SEUM Database Initialization"
echo "========================================"

# Create the database schema
echo "Creating database schema..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- Create postgres user with the same password for compatibility
    DO \$\$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'postgres') THEN
            CREATE USER postgres WITH SUPERUSER PASSWORD 'NathanGalung_SQL_1965';
        ELSE
            ALTER USER postgres WITH PASSWORD 'NathanGalung_SQL_1965';
        END IF;
    END
    \$\$;

    -- Grant all privileges to meseum_user
    GRANT ALL PRIVILEGES ON DATABASE $POSTGRES_DB TO $POSTGRES_USER;
    GRANT ALL ON SCHEMA public TO $POSTGRES_USER;
    GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO $POSTGRES_USER;
    GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO $POSTGRES_USER;
    GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO $POSTGRES_USER;

    -- Also grant to postgres user for compatibility
    GRANT ALL PRIVILEGES ON DATABASE $POSTGRES_DB TO postgres;
    GRANT ALL ON SCHEMA public TO postgres;
EOSQL

echo "Database initialization completed successfully!"
