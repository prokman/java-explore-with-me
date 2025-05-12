package ewm.location.service;

import ewm.location.dto.LocationDto;
import ewm.location.model.Location;

public interface LocationService {

    LocationDto getLocationById(Long locationId);

    Location postLocation(LocationDto locationDto);

}
