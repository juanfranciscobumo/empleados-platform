package co.com.whatsapp.exceptions;

@SuppressWarnings("serial")
public class WhatsappException extends AssertionError {
	public static final String CONTACT_NO_FOUND = "The contact is not what you expected";
	public static final String MESSAGE_NO_FOUND = "The message is not what you expected";

	public WhatsappException(String message, Throwable cause) {
		super(message, cause);
	}
}