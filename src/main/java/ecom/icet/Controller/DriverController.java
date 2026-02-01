package ecom.icet.Controller;

import ecom.icet.Model.Dto.DriverDto;
import ecom.icet.Service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/driver")
@CrossOrigin
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping("/add")
    public ResponseEntity<DriverDto> addDriver(@Valid @RequestBody DriverDto driverDto){
        return ResponseEntity.ok(driverService.addDriver(driverDto));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DriverDto>> getAllDrivers(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(driverService.getAllDrivers(page, size));
    }

    @GetMapping("/getAvailable")
    public ResponseEntity<List<DriverDto>> getAvailableDrivers(){
        return ResponseEntity.ok(driverService.getAvailableDrivers());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DriverDto> updateDriver(@PathVariable String id,@Valid @RequestBody DriverDto driverDto){
        return ResponseEntity.ok(driverService.updateDriver(id, driverDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDriver(@PathVariable String id){
        driverService.deleteDriver(id);
        return ResponseEntity.ok("Driver Deleted Successful.");
    }

}
