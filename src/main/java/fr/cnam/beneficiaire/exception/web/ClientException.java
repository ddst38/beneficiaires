package fr.cnam.beneficiaire.exception.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe d'exception héritant de {@link ApplicationException}, et prévue dans le cas d'erreurs imputables au client de l'application.
 *
 * @author Sopra Steria @ CNAM DDST / PER / Pole Back
 */
@SuppressWarnings({"serial", "squid:S1948"})
public class ClientException extends ApplicationException {

    // === Membres ===

    /** Map de propriétés optionelles libres à sérialiser dans la réponse. */
    private final Map<String, Object> details;

    // === Constructeurs ===

    public ClientException(ProblemType type, Throwable cause, Map<String, Object> details, Object... parameters) {
        super(type, cause, parameters);
        this.details = details == null ? null : new HashMap<>(details);
    }

    public ClientException(ProblemType type, Object... parameters) {
        this(type, null, null, parameters);
    }

    public ClientException(ProblemType type, Map<String, Object> details, Object... parameters) {
        this(type, null, details, parameters);
    }

    public ClientException(ProblemType type, Throwable cause, Object... parameters) {
        this(type, cause, null, parameters);
    }

    // === Getters / Setters ===

    public Map<String, Object> getDetails() {
        return details == null ? null : Collections.unmodifiableMap(details);
    }

}
