package ru.practicum.ewm.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.location.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByLatAndLon(Float lat, Float lon);
}
