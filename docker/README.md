# ME-SEUM PostgreSQL Docker Setup

This Docker setup provides a PostgreSQL database for the ME-SEUM Museum Management System that works across all operating systems (Windows, macOS, Linux).

## Prerequisites

- Docker and Docker Compose installed on your system
- No need for local PostgreSQL installation

## Quick Start

1. **Start the database:**
   ```bash
   docker-compose up -d
   ```

2. **Check if it's running:**
   ```bash
   docker-compose ps
   ```

3. **View logs:**
   ```bash
   docker-compose logs postgres
   ```

## Database Connection Details

- **Host:** `localhost`
- **Port:** `5433`
- **Database:** `me_seum`
- **Username:** `meseum_user` (or `postgres`)
- **Password:** `NathanGalung_SQL_1965`
- **JDBC URL:** `jdbc:postgresql://localhost:5433/me_seum`

## Database Administration

The setup includes Adminer (web-based database admin tool):

- **Web Interface:** http://localhost:8080
- **Server:** `postgres`
- **Username:** `meseum_user`
- **Password:** `NathanGalung_SQL_1965`
- **Database:** `me_seum`

## Common Commands

### Start the database
```bash
docker-compose up -d
```

### Stop the database
```bash
docker-compose down
```

### Stop and remove all data (fresh start)
```bash
docker-compose down -v
```

### View logs
```bash
docker-compose logs -f postgres
```

### Connect via psql
```bash
docker-compose exec postgres psql -U meseum_user -d me_seum
```

### Backup database
```bash
docker-compose exec postgres pg_dump -U meseum_user me_seum > backup.sql
```

### Restore database
```bash
cat backup.sql | docker-compose exec -T postgres psql -U meseum_user -d me_seum
```

## What's Included

- **PostgreSQL 15 Alpine** - Lightweight and fast
- **Database Schema** - All tables for ME-SEUM system
- **Sample Data** - Pre-populated with test data
- **Adminer** - Web-based database management
- **Persistent Storage** - Data survives container restarts
- **Health Checks** - Automatic monitoring

## Troubleshooting

### Port already in use
If port 5432 is already in use, modify the port mapping in `docker-compose.yml`:
```yaml
ports:
  - "5433:5432"  # Use port 5433 instead
```

### Permission issues on Linux
```bash
sudo chown -R $USER:$USER docker/
```

### Reset everything
```bash
docker-compose down -v
docker-compose up -d
```

## For Your Java Application

Update your `application.properties` or database configuration:

```properties
# PostgreSQL configuration
spring.datasource.url=jdbc:postgresql://localhost:5433/me_seum
spring.datasource.username=meseum_user
spring.datasource.password=NathanGalung_SQL_1965
spring.datasource.driver-class-name=org.postgresql.Driver
```

## Cross-Platform Compatibility

This Docker setup works on:
- ✅ Windows (with Docker Desktop)
- ✅ macOS (with Docker Desktop)
- ✅ Linux (with Docker Engine)

Your friends can use the same commands regardless of their operating system!
