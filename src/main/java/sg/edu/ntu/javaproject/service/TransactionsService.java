package sg.edu.ntu.javaproject.service;

import java.util.ArrayList;

import sg.edu.ntu.javaproject.entity.Transactions;

public interface TransactionsService {
    Transactions withdrawTransaction(Transactions transaction);

    Transactions depositTransaction(Transactions transaction);

    Transactions transferTransaction(Integer accountNo, Transactions transaction);

    Transactions getTransactionsById(Integer id);

    ArrayList<Transactions> getAllTransactions();

    ArrayList<Transactions> getTransactionsByCustomerId(Integer Id);
}
