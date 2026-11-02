package com.homenetworkmonitor.client;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.homenetworkmonitor.models.dto.DnsQueryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Lê o log de consultas DNS do AdGuard Home via a API REST (GET /control/querylog),
 * autenticada por Basic Auth com o usuário/senha de admin configurado no add-on.
 */
@Component
public class AdguardQueryLogClient {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private final String baseUrl;
    private final String authHeader;

    public AdguardQueryLogClient(@Value("${adguard.base-url:}") String baseUrl,
                                  @Value("${adguard.username:}") String username,
                                  @Value("${adguard.password:}") String password) {
        this.baseUrl = baseUrl;
        String credentials = username + ":" + password;
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    public List<DnsQueryDTO> fetchRecentQueries(int limit) {
        if (baseUrl == null || baseUrl.isBlank()) {
            return List.of();
        }

        List<DnsQueryDTO> result = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/control/querylog?limit=" + limit))
                    .timeout(Duration.ofSeconds(10))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return List.of();
            }

            JsonNode root = MAPPER.readTree(response.body());
            for (JsonNode item : root.path("data")) {
                String clientIp = item.path("client").asText(null);
                String domain = item.path("question").path("name").asText(null);
                String timeText = item.path("time").asText(null);
                String reason = item.path("reason").asText("");

                if (clientIp == null || domain == null || timeText == null) {
                    continue;
                }

                LocalDateTime queriedAt = OffsetDateTime.parse(timeText).toLocalDateTime();
                boolean blocked = reason.startsWith("Filtered");

                result.add(new DnsQueryDTO(clientIp, domain, blocked, queriedAt));
            }
        } catch (Exception e) {
            return List.of();
        }
        return result;
    }
}
