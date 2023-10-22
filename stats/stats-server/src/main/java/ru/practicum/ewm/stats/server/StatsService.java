package ru.practicum.ewm.stats.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.server.exception.WrongTimestampException;
import ru.practicum.ewm.stats.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private final StatsRepository repository;

    public EndpointHitDto createHit(EndpointHitDto dto) {
        log.debug("Статистика добавлена в базу данных");
        EndpointHit endpointHit = repository.save(EndpointMapper.dtoToHit(dto));
        return EndpointMapper.hitToDto(endpointHit);
    }

    @Transactional(readOnly = true)
    public List<ViewStats> getStats(List<String> uris, LocalDateTime start, LocalDateTime end, Boolean unique) {
        if (start.isAfter(end)) {
            throw new WrongTimestampException("Ошибка запроса - время начала периода не может быть позже его окончания");
        }
        if (unique) {
            if (uris != null) {
                return repository.findHitsWithUniqueIpWithUris(uris, start, end);
            }
            return repository.findHitsWithUniqueIpWithoutUris(start, end);
        } else {
            if (uris != null) {
                return repository.findAllHitsWithUris(uris, start, end);
            }
            return repository.findAllHitsWithoutUris(start, end);
        }
    }
}
