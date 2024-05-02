package sg.edu.ntu.javaproject.Exception;

public class AccountTypeIsNotExistException extends RuntimeException {
    public AccountTypeIsNotExistException(Integer id) {
        super("Account Type with ID " + id + " is not exist");
    }
}
