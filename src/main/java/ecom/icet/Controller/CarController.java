package ecom.icet.Controller;

import ecom.icet.Model.Dto.CarDto;
import ecom.icet.Service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/car")
@CrossOrigin
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;


    @PostMapping(value = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CarDto> addCar(
            @RequestPart("car") @Valid CarDto carDto,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(carService.addCar(carDto, file));
    }

    @PutMapping(value = "/update/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CarDto> updateCar(
            @PathVariable String id,
            @RequestPart("car") @Valid CarDto carDto,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(carService.updateCar(id, carDto, file));
    }

    @GetMapping("/getAll")
    public ResponseEntity<Page<CarDto>> getAllCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(carService.getAllCars(page, size));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable String id) {
        carService.deleteCar(id);
        return ResponseEntity.ok("Car Deleted Successfully");
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable String id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }
}