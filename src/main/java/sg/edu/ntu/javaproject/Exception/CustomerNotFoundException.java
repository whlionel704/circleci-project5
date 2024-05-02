package sg.edu.ntu.javaproject.Exception;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(Integer customer_id){
        super("Could not find the Customer with this customer Id :  "+ customer_id);
    }
    

    
}
