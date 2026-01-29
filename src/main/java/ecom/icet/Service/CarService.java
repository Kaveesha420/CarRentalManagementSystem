package ecom.icet.Service;

import ecom.icet.Model.Dto.CarDto;

import java.util.List;

public interface CarService {
    CarDto addCar(CarDto carDto);
    List<CarDto> getAllCars();
    CarDto getCarById(String id);
    void deleteCar(String id);
    CarDto updateCar(String id, CarDto carDto);
}
