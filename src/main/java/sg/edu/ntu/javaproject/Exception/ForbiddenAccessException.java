package sg.edu.ntu.javaproject.Exception;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException() {
        super("access is denied");
    }
}
