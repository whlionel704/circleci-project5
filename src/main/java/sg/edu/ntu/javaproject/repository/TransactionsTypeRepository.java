package sg.edu.ntu.javaproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.ntu.javaproject.entity.TransactionsType;

public interface TransactionsTypeRepository extends JpaRepository<TransactionsType, Integer> {

}
