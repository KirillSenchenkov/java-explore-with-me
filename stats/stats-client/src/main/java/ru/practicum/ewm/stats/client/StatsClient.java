package ru.practicum.ewm.stats.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.stats.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {

    @Value("${server.url}")
    String serverUrl;

    public StatsClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> createHit(EndpointHitDto dto) {
        ResponseEntity<Object> response = rest.postForEntity(serverUrl + "/hit", dto, Object.class);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }

    public ResponseEntity<Object> getStats(List<String> uris, LocalDateTime start, LocalDateTime end, Boolean unique) {
        Map<String, Object> params = new HashMap<>(Map.of(
                "uris", uris,
                "start", start,
                "end", end,
                "unique", unique));

        ResponseEntity<Object> response = rest.postForEntity(serverUrl + "/hit", params, Object.class);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }
}
