package ecom.icet.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.icet.Model.Dto.DriverDto;
import ecom.icet.Model.Entity.Driver;
import ecom.icet.Repository.DriverRepository;
import ecom.icet.Service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final ObjectMapper mapper;

    @Override
    public DriverDto addDriver(DriverDto driverDto) {
        Driver driver = mapper.convertValue(driverDto, Driver.class);
        Driver savedDriver = driverRepository.save(driver);
        return mapper.convertValue(savedDriver, DriverDto.class);
    }

    @Override
    public List<DriverDto> getAllDrivers() {
        List<Driver> allDrivers = driverRepository.findAll();
        List<DriverDto> driverDto = new ArrayList<>();
        for (Driver driver: allDrivers){
            driverDto.add(mapper.convertValue(driver, DriverDto.class));
        }
        return driverDto;
    }

    @Override
    public List<DriverDto> getAvailableDrivers() {
        List<Driver> availableDrivers = driverRepository.findByStatus("AVAILABLE");
        List<DriverDto> dtoList = new ArrayList<>();
        for (Driver driver:availableDrivers){
            dtoList.add(mapper.convertValue(availableDrivers, DriverDto.class));
        }
        return dtoList;
    }

    @Override
    public DriverDto updateDriver(Long id, DriverDto driverDto) {
        Optional<Driver> exsistingDriver = driverRepository.findById(id);
        if (exsistingDriver.isPresent()){
            Driver driver = exsistingDriver.get();

            driver.setName(driverDto.getName());
            driver.setStatus(driverDto.getStatus());
            driver.setContactNo(driverDto.getContactNo());
            driver.setLicenseNo(driverDto.getLicenseNo());

            Driver updateDriver = driverRepository.save(driver);
            return mapper.convertValue(updateDriver, DriverDto.class);
        }
        return null;
    }

    @Override
    public void deleteDriver(Long id) {
        driverRepository.deleteById(id);
    }
}
