#!/bin/bash

echo "Building and starting Spring Boot Monitoring Application..."

mvn clean package -DskipTests

docker-compose down
docker-compose build
docker-compose up -d

echo "Waiting for services to start..."
sleep 15

echo ""
echo "Services started:"
echo "Application: http://localhost:8080"
echo "Prometheus: http://localhost:9090"
echo "Grafana: http://localhost:3000 (admin/admin)"
echo ""
echo "Showing logs (Ctrl+C to exit)..."
docker-compose logs -f