package sg.edu.ntu.javaproject.Exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Integer id) {
        super("Could not find transaction with id: " + id);
    }
}
