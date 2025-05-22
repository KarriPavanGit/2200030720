package com.question1.avgcal.avgcal.services;

import java.net.URI;
import java.util.*;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.question1.avgcal.avgcal.models.ResponsePayload;

@Service
public class NumberServiceImpl implements NumberService {

    private static final int WINDOW_SIZE = 10;
    private static final String BEARER_TOKEN ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiZXhwIjoxNzQ3ODk1NzgxLCJpYXQiOjE3NDc4OTU0ODEsImlzcyI6IkFmZm9yZG1lZCIsImp0aSI6IjA0NzhiN2M0LTExODEtNDVkZi05YWI1LTM0ODlhNmUxMTAyNyIsInN1YiI6IjIyMDAwMzA3MjBjc2VoQGdtYWlsLmNvbSJ9LCJlbWFpbCI6IjIyMDAwMzA3MjBjc2VoQGdtYWlsLmNvbSIsIm5hbWUiOiJwYXZhbiBrYXJyaSIsInJvbGxObyI6IjIyMDAwMzA3MjAiLCJhY2Nlc3NDb2RlIjoiYmVUSmpKIiwiY2xpZW50SUQiOiIwNDc4YjdjNC0xMTgxLTQ1ZGYtOWFiNS0zNDg5YTZlMTEwMjciLCJjbGllbnRTZWNyZXQiOiJ0a3VyRHpZS0hHeFFwQk1aIn0.A1S59QWVggSN79aeEcokK5nEHNrCL5lhljrOBXBNPpU";
    public final Map<String, String> endpoints = Map.of(
        "p", "http://20.244.56.144/evaluation-service/primes",
        "f", "http://20.244.56.144/evaluation-service/fibo",
        "e", "http://20.244.56.144/evaluation-service/even",
        "r", "http://20.244.56.144/evaluation-service/rand"
    );

    private final List<Integer> window = new ArrayList<>();
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ResponsePayload processrequest(String type) {
        List<Integer> prevWindow = new ArrayList<>(window);
        List<Integer> fetched = fetchNumbers(type);

        if (fetched == null) fetched = new ArrayList<>();  

        for (int num : fetched) {
            if (!window.contains(num)) {
                if (window.size() == WINDOW_SIZE) {
                    window.remove(0);
                }
                window.add(num);
            }
        }

        double sum = 0.0;
        for (int num : window) {
            sum += num;
        }

        double avg = window.isEmpty() ? 0 : Math.round((sum / window.size()) * 100.0) / 100.0;

        return new ResponsePayload(prevWindow, new ArrayList<>(window), fetched, avg);
    }

    private List<Integer> fetchNumbers(String type) {
        if (!endpoints.containsKey(type)) {
            System.err.println("Invalid type: " + type);
            return new ArrayList<>();
        }

        try {
            URI uri = URI.create(endpoints.get(type));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + BEARER_TOKEN);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                Map.class
            );

            Map<String, Object> body = response.getBody();

            if (body != null && body.containsKey("numbers")) {
                List<?> rawList = (List<?>) body.get("numbers");
                List<Integer> numbers = new ArrayList<>();

                for (Object obj : rawList) {
                    if (obj instanceof Number) {
                        numbers.add(((Number) obj).intValue());
                    }
                }

                return numbers;
            }
        } catch (Exception e) {
            System.err.println("Error fetching numbers from endpoint: " + e.getMessage());
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
