package fr.cnam.beneficiaire.exception.web;

/**
 * Enum des différent types de problème / erreurs standard prévus par le starter-kit.
 * <p>
 * Les différentes valeurs correspondent aux clés de la Map sk.problem.responses du fichier sk-problem-defaults.yml contenu dans le starter
 * sk-starter-exceptions-web.
 * <p>
 * Un projet souhaitant ajouter de nouveaux types de problèmes pourra créer son propre Enum et ajouter les propriétés correspondant à ces
 * nouvelles valeurs sous sk.problem.responses dans son propre fichier application.yml.
 *
 * @author Sopra Steria @ CNAM DDST / PER / Pole Back
 */
public enum CommonProblemType implements ProblemType {

    // @formatter:off
    DEFAULT_CLIENT_PROBLEM,
    DONNEES_INVALIDES,
    DONNEES_INVALIDES_MSG_AVEC_PROBLEMES,
    RESSOURCE_NON_TROUVEE,
    RESSOURCE_X_NON_TROUVEE,
    RESSOURCE_DEJA_EXISTANTE,
    HTTP_METHODE_NON_AUTORISEE,
    HTTP_PARAMETRE_MANQUANT,
    HTTP_TYPE_PARAMETRE_INCORRECT,
    ECHEC_AUTHENTIFICATION,
    DROITS_INSUFFISANTS,

    DEFAULT_SERVICE_PROBLEM,
    ERREUR_INATTENDUE,
    TIMEOUT
    // @formatter:on
}
