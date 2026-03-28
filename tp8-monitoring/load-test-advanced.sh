#!/bin/bash

echo "==================================="
echo "Advanced Load Testing"
echo "==================================="
echo ""

BASE_URL="http://localhost:8080"
TOTAL_REQUESTS=200
CONCURRENT=10
ERROR_COUNT=0
SUCCESS_COUNT=0
declare -a RESPONSE_TIMES

echo "Starting load test with $TOTAL_REQUESTS requests..."
echo ""

send_request() {
    local id=$1
    local duration=$((RANDOM % 1500 + 50))
    local start_time=$(date +%s%N)

    response=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/process/$duration")
    local end_time=$(date +%s%N)
    local elapsed=$((($end_time - $start_time) / 1000000))

    RESPONSE_TIMES+=($elapsed)

    if [ "$response" = "200" ]; then
        echo "[$id] Request completed in ${elapsed}ms (processing: ${duration}ms)"
        ((SUCCESS_COUNT++))
    else
        echo "[$id] Request FAILED with status $response"
        ((ERROR_COUNT++))
    fi

    if [ $((id % 20)) -eq 0 ]; then
        curl -s "$BASE_URL/api/simulate-error" > /dev/null
        echo "[$id] Simulated error sent"
    fi
}

for i in $(seq 1 $TOTAL_REQUESTS)
do
    send_request $i &

    if [ $((i % CONCURRENT)) -eq 0 ]; then
        wait
    fi
done

wait

echo ""
echo "==================================="
echo "Load Test Results"
echo "==================================="
echo "Total Requests: $TOTAL_REQUESTS"
echo "Successful: $SUCCESS_COUNT"
echo "Failed: $ERROR_COUNT"
echo "Success Rate: $(echo "scale=2; $SUCCESS_COUNT * 100 / $TOTAL_REQUESTS" | bc)%"
echo ""

if [ ${#RESPONSE_TIMES[@]} -gt 0 ]; then
    total=0
    min=${RESPONSE_TIMES[0]}
    max=${RESPONSE_TIMES[0]}

    for time in "${RESPONSE_TIMES[@]}"; do
        total=$((total + time))
        if [ $time -lt $min ]; then min=$time; fi
        if [ $time -gt $max ]; then max=$time; fi
    done

    avg=$((total / ${#RESPONSE_TIMES[@]}))

    echo "Response Time Statistics:"
    echo "  Min: ${min}ms"
    echo "  Max: ${max}ms"
    echo "  Avg: ${avg}ms"
fi

echo ""
echo "Check Grafana dashboard for detailed metrics:"
echo "http://localhost:3000"