package ecom.icet.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.icet.Model.Dto.CarDto;
import ecom.icet.Model.Entity.Car;
import ecom.icet.Repository.CarRepository;
import ecom.icet.Service.AuditLogService;
import ecom.icet.Service.CarService;
import ecom.icet.Util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;

    private final String uploadDir = "src/main/resources/static/uploads/cars";

    @Override
    public CarDto addCar(CarDto carDto, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            carDto.setImagePath(saveImage(file));
        }

        Car car = objectMapper.convertValue(carDto, Car.class);

        String lastId = carRepository.findLastIdNative();
        car.setId(IdGenerator.generateNextId(lastId, "CAR"));

        Car savedCar = carRepository.save(car);
        auditLogService.logAction("CREATE", "Added New Car: " + savedCar.getId());
        return objectMapper.convertValue(savedCar, CarDto.class);
    }

    private String saveImage(MultipartFile file) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + File.separator + fileName);
        Files.copy(file.getInputStream(), path);
        return "/uploads/cars/" + fileName;
    }

    @Override
    public Page<CarDto> getAllCars(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return carRepository.findAll(pageable).map(car -> objectMapper.convertValue(car, CarDto.class));
    }

    @Override
    public CarDto getCarById(String id) {
        return carRepository.findById(id)
                .map(car -> objectMapper.convertValue(car, CarDto.class))
                .orElse(null);
    }

    @Override
    public void deleteCar(String id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            auditLogService.logAction("DELETE", "Deleted Car ID: " + id);
        }
    }

    @Override
    public CarDto updateCar(String id, CarDto carDto, MultipartFile file) throws IOException {
        Optional<Car> existingCar = carRepository.findById(id);
        if (existingCar.isPresent()) {
            Car car = existingCar.get();
            if (file != null && !file.isEmpty()) {
                car.setImagePath(saveImage(file));
            }
            car.setBrand(carDto.getBrand());
            car.setModel(carDto.getModel());
            car.setFuelType(carDto.getFuelType());
            car.setSeatingCapacity(carDto.getSeatingCapacity());
            car.setPricePerDay(carDto.getPricePerDay());
            car.setStatus(carDto.getStatus());

            car.setCategory(carDto.getCategory());
            car.setTransmission(carDto.getTransmission());

            Car updatedCar = carRepository.save(car);
            return objectMapper.convertValue(updatedCar, CarDto.class);
        }
        return null;
    }
}