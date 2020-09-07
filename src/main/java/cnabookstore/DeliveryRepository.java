package cnabookstore;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends PagingAndSortingRepository<Delivery, Long>{

    Optional<List<Delivery>> findByOrderByOrderIdAsc(Long orderId);

}