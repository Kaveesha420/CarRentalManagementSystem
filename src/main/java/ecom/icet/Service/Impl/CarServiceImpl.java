package ecom.icet.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.icet.Model.Dto.CarDto;
import ecom.icet.Model.Entity.Car;
import ecom.icet.Repository.CarRepository;
import ecom.icet.Service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ObjectMapper objectMapper;

    @Override
    public CarDto addCar(CarDto carDto) {
        Car car = objectMapper.convertValue(carDto, Car.class);
        Car savedCar = carRepository.save(car);
        return objectMapper.convertValue(savedCar, CarDto.class);
    }

    @Override
    public List<CarDto> getAllCars() {
        List<Car> carList = carRepository.findAll();
        List<CarDto> carDtoList = new ArrayList<>();

        for(Car car: carList){
            carDtoList.add(objectMapper.convertValue(carList, CarDto.class));
        }
        return carDtoList;
    }

    @Override
    public CarDto getCarById(Long id) {
        Optional<Car> car = carRepository.findById(id);
        return car.map(value -> objectMapper.convertValue(value, CarDto.class)).orElse(null);
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public CarDto updateCar(Long id, CarDto carDto) {
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
            return objectMapper.convertValue(updatedCar, CarDto.class);
        }
        return null;
    }
}
