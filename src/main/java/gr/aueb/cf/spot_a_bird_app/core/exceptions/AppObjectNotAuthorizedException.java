package gr.aueb.cf.spot_a_bird_app.core.exceptions;

public class AppObjectNotAuthorizedException extends AppGenericException {

    private static final String DEFAULT_CODE = "NotAuthorized";

    public AppObjectNotAuthorizedException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
