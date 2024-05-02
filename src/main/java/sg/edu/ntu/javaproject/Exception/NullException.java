package sg.edu.ntu.javaproject.Exception;

public class NullException extends RuntimeException {
    public NullException(String nullValue) {
        super(nullValue + " must be provided");
    }
}
