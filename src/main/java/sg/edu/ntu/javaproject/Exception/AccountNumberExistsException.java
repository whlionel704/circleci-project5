package sg.edu.ntu.javaproject.Exception;

public class AccountNumberExistsException extends RuntimeException {
    public AccountNumberExistsException(Integer accountNumber) {
        super("Account number is already exists: " + accountNumber);
    }
}
