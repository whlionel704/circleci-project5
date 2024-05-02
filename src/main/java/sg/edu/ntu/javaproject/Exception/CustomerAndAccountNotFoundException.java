package sg.edu.ntu.javaproject.Exception;

public class CustomerAndAccountNotFoundException extends RuntimeException {
    public CustomerAndAccountNotFoundException(Integer customerId, Integer accountTypeId) {
        super("Customer ID:" + customerId + " with account type ID:" + accountTypeId + " is not exists");

    }

}
