package fr.cnam.beneficiaire.exception.web;

import java.util.UUID;

/**
 * Classe parent de toute les exceptions instanciées dans le starter-kit Web Service.
 * <p>
 * Les projets basés sur ce starter-kit sont encouragés à n'utiliser que des exceptions héritant de celle-ci afin de bénéficier des
 * mécanismes de gestion d'erreur par défaut implémentés dans le starter-kit, notamment la transformation de ces exceptions en réponses JSON
 * et SOAP Fault, sans avoir à redéfinir eux-mêmes des classes ControllerAdvice et/ou intercepteurs SOAP / CXF.
 *
 * @author Sopra Steria @ CNAM DDST / PER / Pole Back
 */
@SuppressWarnings({"serial", "squid:S1948"})
public abstract class ApplicationException extends RuntimeException {

    // === Membres ===

    /**
     * L'identifiant du type de problème remonté par cette exception.
     * <p>
     * doit correspondre à une des clés de la Map sk.problem.responses. pour que les handlers d'exception REST / SOAP fournis en standard
     * par le starter-kit puissent déterminer les attributs de la réponse à renvoyer au client.
     */
    private final ProblemType type;

    /**
     * Un identifiant unique (UUID) permettant d'identifier une occurence précise d'une exception dans les log.
     */
    private final String id = generateUniqueId();

    /**
     * Liste optionnelle de paramètres dans le cas où le/les messages associés à ce type de problème sont variabilisés selon la syntaxe de
     * {@link String#format(String, Object...)}
     * <p>
     * Attention ces paramètres ne sont pas utilisés pour formatter le message (optionnel) interne de l'exception.
     */
    private final Object[] parameters;

    // === Constructeurs ===

    public ApplicationException(ProblemType type, Object... parameters) {
        this(type, null, parameters);
    }

    public ApplicationException(ProblemType type, Throwable cause, Object... parameters) {
        super(generateExceptionMessage(type, cause), cause);
        this.type = type;
        this.parameters = parameters == null ? null : parameters.clone();
    }

    // === Getters / Setters ===

    public ProblemType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Object[] getParameters() {
        return parameters == null ? null : parameters.clone();
    }

    // === Utilitaires ===

    /**
     * Génère et retourne un ID unique d'erreur.
     */
    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    private static String generateExceptionMessage(ProblemType type, Throwable cause) {
        return cause == null ? type.name() : type.name() + " - " + cause.toString();
    }
}
