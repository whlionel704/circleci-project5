package sg.edu.ntu.javaproject.Exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(Integer balance, Integer amount) {
        super("Insufficient balance to withdraw:" + amount + " with your current balance of:" + balance);
    }

}
