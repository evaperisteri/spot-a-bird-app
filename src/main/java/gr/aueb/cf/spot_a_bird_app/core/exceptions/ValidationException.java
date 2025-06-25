package gr.aueb.cf.spot_a_bird_app.core.exceptions;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ValidationException extends Exception {
    //interface που περιέχει τα validation erros μιας φόρμας σε μορφή key-value οπότε προκύπτει response που περιέχει τα λάθη
    private final BindingResult bindingResult;

    public ValidationException(BindingResult bindingResult) {
        super("Validation failed");
        this.bindingResult = bindingResult;
    }
}
