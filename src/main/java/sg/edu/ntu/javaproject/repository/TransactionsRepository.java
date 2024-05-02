package sg.edu.ntu.javaproject.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.ntu.javaproject.entity.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, Integer> {
    ArrayList<Transactions> findBySourceCustomerId(Integer id);

    ArrayList<Transactions> findByDestinationCustomerId(Integer id);

}
