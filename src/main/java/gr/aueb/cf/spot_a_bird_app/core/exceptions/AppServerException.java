package gr.aueb.cf.spot_a_bird_app.core.exceptions;

import lombok.Getter;

@Getter
public class AppServerException extends Exception {
    private final String code;

    public AppServerException(String code, String message) {
        super(message);
        this.code = code;
    }
}
