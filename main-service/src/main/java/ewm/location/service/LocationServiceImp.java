package ewm.location.service;

import ewm.exceptions.NotFoundException;
import ewm.location.dto.LocationDto;
import ewm.location.dto.LocationMapper;
import ewm.location.model.Location;
import ewm.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationServiceImp implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public LocationDto getLocationById(Long locationId) {
        return LocationMapper
                .toDto(locationRepository
                        .findById(locationId)
                        .orElseThrow(() -> new NotFoundException("Location with ID " + locationId + " not found")));
    }

    @Override
    public Location postLocation(LocationDto locationDto) {
        return locationRepository.save(LocationMapper.toLocation(locationDto));
    }
}
