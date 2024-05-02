package sg.edu.ntu.javaproject.Exception;

public class AccountTypeIsExistException extends RuntimeException {
    public AccountTypeIsExistException(Integer customerId, Integer accountIdType) {
        super("Account Type:" + accountIdType + " is already exist for this customer:" + customerId);
    }
}
