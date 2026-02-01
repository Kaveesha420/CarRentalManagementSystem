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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;

    @Override
    public CarDto addCar(CarDto carDto) {
        Car car = objectMapper.convertValue(carDto, Car.class);

        Car lastCar = carRepository.findFirstByOrderByIdDesc();
        String lastId = (lastCar != null) ? lastCar.getId() : null;
        car.setId(IdGenerator.generateNextId(lastId, "CAR"));

        Car savedCar = carRepository.save(car);
        auditLogService.logAction("CREATE", "Added new Car: " + savedCar.getBrand() + " " + savedCar.getModel() + " (" + savedCar.getId() + ")");
        return objectMapper.convertValue(savedCar, CarDto.class);
    }

    @Override
    public List<CarDto> getAllCars(int page,int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Car> carPage = carRepository.findAll(pageable);
        List<CarDto> carDtoList = new ArrayList<>();
        for (Car car : carPage.getContent()) {
            carDtoList.add(objectMapper.convertValue(car, CarDto.class));
        }
        return carDtoList;
    }

    @Override
    public CarDto getCarById(String id) {
        Optional<Car> car = carRepository.findById(id);
        return car.map(value -> objectMapper.convertValue(value, CarDto.class)).orElse(null);
    }

    @Override
    public void deleteCar(String id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            auditLogService.logAction("DELETE", "Deleted Car ID: " + id);
        } else {
            throw new IllegalArgumentException("Car not found");
        }
    }

    @Override
    public CarDto updateCar(String id, CarDto carDto) {
        Optional<Car> existingCar = carRepository.findById(id);

        if (existingCar.isPresent()){
            Car carToUpdate = existingCar.get();

            carToUpdate.setBrand(carDto.getBrand());
            carToUpdate.setModel(carDto.getModel());
            carToUpdate.setFuelType(carDto.getFuelType());
            carToUpdate.setSeatingCapacity(carDto.getSeatingCapacity());
            carToUpdate.setPricePerDay(carDto.getPricePerDay());
            carToUpdate.setImagePath(carDto.getImagePath());
            carToUpdate.setStatus(carDto.getStatus());

            Car updatedCar = carRepository.save(carToUpdate);
            auditLogService.logAction("updated","Updated Car ID: " + id);
            return objectMapper.convertValue(updatedCar, CarDto.class);
        }
        return null;
    }
}
