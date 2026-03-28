#!/bin/bash

echo "Starting load tests..."

for i in {1..50}
do
    duration=$((RANDOM % 1000 + 100))
    echo "Request $i: processing for $duration ms"
    curl -s "http://localhost:8080/api/process/$duration" > /dev/null

    if [ $((i % 10)) -eq 0 ]; then
        echo "Simulating error..."
        curl -s "http://localhost:8080/api/simulate-error" > /dev/null
    fi

    sleep 0.2
done

echo "Load testing completed"