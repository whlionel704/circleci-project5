package sg.edu.ntu.javaproject.Exception;

public class AccountNumberIsNotExistException extends RuntimeException {
    public AccountNumberIsNotExistException(Integer accountNumber) {
        super("Account number:" + accountNumber + " is not exists.");
    }
}