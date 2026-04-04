package ecom.icet.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.icet.Model.Dto.DriverDto;
import ecom.icet.Model.Entity.Driver;
import ecom.icet.Repository.DriverRepository;
import ecom.icet.Service.AuditLogService;
import ecom.icet.Service.DriverService;
import ecom.icet.Util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final ObjectMapper mapper;
    private final AuditLogService auditLogService;

    @Override
    public DriverDto addDriver(DriverDto driverDto) {
        Driver driver = mapper.convertValue(driverDto, Driver.class);

        Driver lastDriver = driverRepository.findFirstByOrderByIdDesc();
        String lastId = (lastDriver != null) ? lastDriver.getId() : null;
        driver.setId(IdGenerator.generateNextId(lastId, "DRV"));

        Driver savedDriver = driverRepository.save(driver);
        auditLogService.logAction("CREATE", "Added new Driver: " + savedDriver.getName());
        return mapper.convertValue(savedDriver, DriverDto.class);
    }

    @Override
    public List<DriverDto> getAllDrivers(int page,int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Driver> driverPage = driverRepository.findAll(pageable);
        List<DriverDto> driverDto = new ArrayList<>();

        for (Driver driver : driverPage){
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
    public DriverDto updateDriver(String id, DriverDto driverDto) {
        Optional<Driver> exsistingDriver = driverRepository.findById(id);
        if (exsistingDriver.isPresent()){
            Driver driver = exsistingDriver.get();

            driver.setName(driverDto.getName());
            driver.setStatus(driverDto.getStatus());
            driver.setContactNo(driverDto.getContactNo());
            driver.setLicenseNo(driverDto.getLicenseNo());
            Driver updateDriver = driverRepository.save(driver);
            auditLogService.logAction("UPDATE", "Updated Driver info: " + id);
            return mapper.convertValue(updateDriver, DriverDto.class);
        }
        return null;
    }

    @Override
    public void deleteDriver(String id) {
        if (driverRepository.existsById(id)) {
            driverRepository.deleteById(id);

            auditLogService.logAction("DELETE", "Deleted Driver: " + id);
        }
    }
}
