package ecom.icet.Repository;

import ecom.icet.Model.Entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car,String> {
    @Query(value = "SELECT id FROM car ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String findLastIdNative();

    List<Car> findByStatus(String status);
    List<Car> findByBrand(String brand);

    //Soft Delete
    Car findFirstByOrderByIdDesc();
}
