package ecom.icet.Service;

import ecom.icet.Model.Dto.CarDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CarService {
    CarDto addCar(CarDto carDto);
    Page<CarDto> getAllCars(int page, int size);
    CarDto getCarById(String id);
    void deleteCar(String id);
    CarDto updateCar(String id, CarDto carDto);
}
