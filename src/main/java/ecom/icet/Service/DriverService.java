package ecom.icet.Service;

import ecom.icet.Model.Dto.DriverDto;

import java.util.List;

public interface DriverService {
    DriverDto addDriver(DriverDto driverDto);
    List<DriverDto> getAllDrivers();
    List<DriverDto> getAvailableDrivers();
    DriverDto updateDriver(Long id, DriverDto driverDto);
    void deleteDriver(Long id);
}
