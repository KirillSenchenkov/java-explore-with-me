package ru.practicum.ewm.location;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.model.Location;

@UtilityClass
public class LocationMapper {

    public LocationDto locationToDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }

    public Location dtoToLocation(LocationDto locationDto) {
        return new Location(locationDto.getLat(), locationDto.getLon());
    }
}
