#!/bin/bash

while true
do
    clear
    echo "=== Monitoring Dashboard ==="
    echo "Time: $(date)"
    echo ""

    echo "Application Health:"
    curl -s "http://localhost:8080/monitoring/health" 2>/dev/null | head -20

    echo ""
    echo "Recent Application Logs:"
    tail -5 logs/application.log 2>/dev/null || echo "No logs yet"

    sleep 10
done