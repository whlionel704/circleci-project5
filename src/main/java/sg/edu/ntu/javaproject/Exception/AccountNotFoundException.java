package sg.edu.ntu.javaproject.Exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Integer id) {
        super("Could not find account with id: " + id);
    }

}
