package co.com.utest.certification.exceptions;

public class ExceptionRegisterUser extends AssertionError {
    public static final String BUTTON = "the button was not found";

    public ExceptionRegisterUser(String message, Throwable cause) {
        super(message, cause);

    }
}
