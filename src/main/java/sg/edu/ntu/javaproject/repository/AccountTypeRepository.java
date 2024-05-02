package sg.edu.ntu.javaproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.ntu.javaproject.entity.AccountType;

public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {

}