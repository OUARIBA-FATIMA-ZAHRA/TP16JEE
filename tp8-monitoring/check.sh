#!/bin/bash

echo "=== Service Status Check ==="
echo ""

check_service() {
    local url=$1
    local name=$2
    if curl -s -o /dev/null -w "%{http_code}" "$url" | grep -q "200\|302"; then
        echo "$name: RUNNING"
    else
        echo "$name: DOWN"
    fi
}

check_service "http://localhost:8080/monitoring/health" "Application"
check_service "http://localhost:9090/-/healthy" "Prometheus"
check_service "http://localhost:3000/api/health" "Grafana"

echo ""
echo "=== Recent Logs ==="
docker-compose logs --tail=10