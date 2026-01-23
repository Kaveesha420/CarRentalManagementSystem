package ecom.icet.Service;

import ecom.icet.Model.Dto.CarDto;

import java.util.List;

public interface CarService {
    CarDto addCar(CarDto carDto);
    List<CarDto> getAllCars();
    CarDto getCarById(Long id);
    void deleteCar(Long id);
    CarDto updateCar(Long id, CarDto carDto);
}
