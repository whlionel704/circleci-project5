package sg.edu.ntu.javaproject.Exception;

public class EmailIsExistException extends RuntimeException {
    public EmailIsExistException(String email) {
        super(email + " is already exist");
    }

}