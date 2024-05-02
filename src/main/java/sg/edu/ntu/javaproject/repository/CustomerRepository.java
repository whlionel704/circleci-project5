package sg.edu.ntu.javaproject.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.ntu.javaproject.entity.Customers;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Integer> {
    Customers findByCustomerEmail(String customerEmail);
}
