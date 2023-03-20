package PaymentService.repository;

import PaymentService.entity.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDetails,Long> {


    TransactionDetails findByOrderId(long orderId);
}
