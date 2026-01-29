package ecom.icet.Controller;

import ecom.icet.Model.Dto.CarDto;
import ecom.icet.Service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
@CrossOrigin
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping("/add")
    public ResponseEntity<CarDto> addCar(@Valid @RequestBody CarDto carDto){
        return ResponseEntity.ok(carService.addCar(carDto));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CarDto>> getAllCars(){
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable String id){
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable String id,@Valid @RequestBody CarDto carDto){
        return ResponseEntity.ok(carService.updateCar(id, carDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable String id){
        carService.deleteCar(id);
        return ResponseEntity.ok("Car Deleted Successfully");
    }
}
