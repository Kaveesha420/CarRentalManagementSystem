package ecom.icet.Controller;

import ecom.icet.Model.Dto.DriverDto;
import ecom.icet.Service.DriverService;
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
    public ResponseEntity<DriverDto> addDriver(@RequestBody DriverDto driverDto){
        return ResponseEntity.ok(driverService.addDriver(driverDto));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DriverDto>> getAllDrivers(){
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @GetMapping("/getAvailable")
    public ResponseEntity<List<DriverDto>> getAvailableDrivers(){
        return ResponseEntity.ok(driverService.getAvailableDrivers());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DriverDto> updateDriver(@PathVariable Long id,@RequestBody DriverDto driverDto){
        return ResponseEntity.ok(driverService.updateDriver(id, driverDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDriver(@PathVariable Long id){
        driverService.deleteDriver(id);
        return ResponseEntity.ok("Driver Deleted Successful.");
    }

}
