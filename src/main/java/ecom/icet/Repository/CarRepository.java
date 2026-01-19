package ecom.icet.Repository;

import ecom.icet.Model.Entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {
    List<Car> findByStatus(String status);
    List<Car> findByBrand(String brand);
}
