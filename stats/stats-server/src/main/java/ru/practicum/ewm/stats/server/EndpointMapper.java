package ru.practicum.ewm.stats.server;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.server.model.EndpointHit;

@UtilityClass
public class EndpointMapper {

    public EndpointHit dtoToHit(EndpointHitDto dto) {
        return new EndpointHit(
                dto.getId(),
                dto.getApp(),
                dto.getUri(),
                dto.getIp(),
                dto.getTimestamp()
        );
    }
}
