package co.com.test.youtube.exceptions;

@SuppressWarnings("serial")
public class YouTubeError extends AssertionError {
	public static final String NOT_FOUND_VIDEO = "VIDEO DID NOT HAVE THE SPECIFIED  DURATION";
	public static final String THE_TEXT_WAS_NOT_FOUND = "THE TEXT WAS NOT FOUND";
	public YouTubeError(String message, Throwable cause) {
		super(message, cause);

	}
}
