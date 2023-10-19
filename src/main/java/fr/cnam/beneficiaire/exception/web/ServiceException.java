package fr.cnam.beneficiaire.exception.web;

/**
 * Classe d'exception héritant de {@link ApplicationException}, et prévue dans le cas d'erreurs imputables au service (l'application - ou
 * les services dont il dépend).
 * <p>
 */
@SuppressWarnings({"serial", "squid:S1948"})
public class ServiceException extends ApplicationException {

    // === Constructeurs ===

    /**
     * Construit une {@link ServiceException} liée au type de problème <code>type</code>, ayec pour cause <code>cause</code>, et avec les
     * paramètres optionnels <code>parameters</code> pour la construction du message final à partir du message paramétré associé à
     * <code>type</code> dans la configuration <code>sk.problem.response.&lt;type&gt;.properties.message</code> .
     *
     * @param type le type de problème
     * @param cause la cause de l'exception
     * @param parameters les paramètres pour le message
     */
    public ServiceException(ProblemType type, Throwable cause, Object... parameters) {
        super(type, cause, parameters);
    }

    /**
     * Construit une {@link ServiceException} liée au type de problème par défaut <code>DEFAULT_SERVICE_PROBLEM</code>, avec pour cause
     * <code>cause</code>.
     *
     * @param cause la cause de l'exception
     */
    public ServiceException(Throwable cause) {
        this(CommonProblemType.DEFAULT_SERVICE_PROBLEM, cause);
    }

    /**
     * Construit une {@link ServiceException} liée au type de problème <code>type</code>, sans cause, et avec les paramètres optionnels
     * <code>parameters</code> pour la construction du message final à partir du message paramétré associé à <code>type</code> dans la
     * configuration <code>sk.problem.response.&lt;type&gt;.properties.message</code> .
     *
     * @param type le type de problème
     * @param cause la cause de l'exception
     */
    public ServiceException(ProblemType type, Object... parameters) {
        this(type, null, parameters);
    }

    /**
     * Construit une {@link ServiceException} liée au type de problème par défaut <code>DEFAULT_SERVICE_PROBLEM</code>, ayec pour cause une
     * {@link RuntimeException} avec pour message <code>message</code>.
     * <p>
     * Attention le message apparaitra dans les log de l'exception, mais n'est pas le message qui sera renvoyé au client. Le message renvoyé
     * au client est celui paramétré et associé à DEFAULT_SERVICE_PROBLEM dans la configuration. Pour les problème techniques on donne le
     * moins d'informations possible au client.
     *
     * @param message le message à associer à la cause de l'exception
     */
    public ServiceException(String message) {
        this(new RuntimeException(message));
    }
}
