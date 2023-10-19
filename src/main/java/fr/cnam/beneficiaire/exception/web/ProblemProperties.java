package fr.cnam.beneficiaire.exception.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

/**
 * Classe de configuration de la gestion des réponses REST et/ou SOAP suite à une exception remontée de la couche Controller REST ou SOAP.
 * <p>
 * Cette classe charge les propriétés <code>sk.problem.*</code>.
 *
 * @author Sopra Steria @ CNAM DDST / PER / Pole Back
 */
@ConfigurationProperties(prefix = "sk.problem")
@Validated
public class ProblemProperties {

    // === Membres ===

    /**
     * Map des type de problème -> donnée de la réponse à renvoyer
     */
    @NotEmpty
    private final Map<String, ResponseData> responses;

    /**
     * Autres options REST
     */
    @Valid
    private final RestOptions restOptions;

    /**
     * Autres options SOAP
     */
    @Valid
    private final SoapOptions soapOptions;

    // === Constructeur(s) ===

    public ProblemProperties(Map<String, ResponseData> responses,
            @DefaultValue RestOptions restOptions,
            @DefaultValue SoapOptions soapOptions) {
        this.responses = responses;
        this.restOptions = restOptions;
        this.soapOptions = soapOptions;
    }


    // === Getters / Setters ===

    @NonNull
    public Map<String, ResponseData> getResponses() {
        return responses;
    }


    @NonNull
    public RestOptions getRestOptions() {
        return restOptions;
    }

    @NonNull
    public SoapOptions getSoapOptions() {
        return soapOptions;
    }

    // === Méthodes utilitaires ===

    /**
     * Vérifie que la configuration est cohérentes pour les expositions REST:<br>
     * - toutes les réponses configurées ont bien la partie rest.status<br>
     * - si sk.problem.rest-options.forward-rest-4xx-errors.enabled est à true, alors la liste
     * sk.problem.rest-options.forward-rest-4xx-errors.status ne doit pas être nulle
     *
     * @throws IllegalStateException en cours de non conformité
     */
    public void checkConfigurationForRest() {

        responses.forEach((pb, resp) -> {
            if (resp.getRest() == null) {
                throw new IllegalStateException("La propriété sk.problem.responses['" + pb + "'].rest.status est manquante");
            }

            HttpStatus status = resp.getRest().getStatus();

            if (status == null || !status.isError()) {
                throw new IllegalStateException("Le status sk.problem.responses['" + pb
                        + "'].rest.status n'est pas un status HTTP correct ou correspondant à une erreur 4XX ou 5XX");
            }
        });

        ForwardRestClientErrors forward4XX = restOptions.getForwardRestClientErrors();

        if (forward4XX.isEnabled() && !forward4XX.getStatus().isEmpty()) {
            forward4XX.getStatus().forEach(status -> {
                if (status == null || !status.is4xxClientError()) {
                    throw new IllegalStateException("La liste sk.problem.rest-options.forward-rest-client-errors.status est incorrecte: "
                            + " elle ne doit contenir que des status de la famille 4XX");
                }
            });
        }
    }

    /**
     * Vérifie que la configuration est cohérentes pour les expositions SOAP:<br>
     * - toutes les réponses configurées ont bien la partie soap<br>
     *
     * @throws IllegalStateException en cours de non conformité
     */
    public void checkConfigurationForSoap() {

        responses.forEach((pb, resp) -> {
            if (resp.getSoap() == null) {
                throw new IllegalStateException("Configuration sk.problem.responses['" + pb + "'].soap.* manquante");
            }
        });
    }

    // === Inner classes ===

    @Validated
    public static class ResponseData {

        /**
         * Les propriétés code, message etc. pour la réponse à retourner pour ce problème
         */
        @Valid
        private final Properties properties;

        /**
         * Les propriétés de la réponse spécifique à la réponse REST
         */
        @Valid
        private final Rest rest;

        /**
         * Les propriétés de la réponse spécifique à la réponse SOAP
         */
        @Valid
        private final Soap soap;

        public ResponseData(@DefaultValue Properties properties, Rest rest, Soap soap) {
            this.properties = properties;
            this.rest = rest;
            this.soap = soap;
        }

        @NonNull
        public Properties getProperties() {
            return properties;
        }

        public Rest getRest() {
            return rest;
        }

        public Soap getSoap() {
            return soap;
        }
    }

    @Validated
    public static class Properties {

        /**
         * Code détaillé de l'erreur à renvoyer dans la réponse JSON, ou dans l'attribut code du Detail de la Soap Fault
         */
        @NotBlank
        private final String code;

        /**
         * Message détaillé de l'erreur à renvoyer dans la réponse JSON, ou dans l'élément de Detail de la Soap Fault
         */
        @NotBlank
        private final String message;

        public Properties(String code, String message) {
            this.code = code;
            this.message = message;
        }

        @NonNull
        public String getCode() {
            return code;
        }

        @NonNull
        public String getMessage() {
            return message;
        }
    }

    @Validated
    public static class Rest {

        /** le status HTTP à renvoyer */
        @NotNull
        private final HttpStatus status;

        public Rest(HttpStatus status) {
            this.status = status;
        }

        @NonNull
        public HttpStatus getStatus() {
            return status;
        }
    }

    @Validated
    public static class Soap {

        /** le fault code SOAP à renvoyer */
        @NotNull
        private final FaultCode faultCode;

        /** le sub-code SOAP à renvoyer */
        @NotBlank
        private final String subCode;

        /** la severité à renvoye sub-code SOAP à renvoyer */
        @NotNull
        private final Severite severite;

        @NotBlank
        private String reason;

        public Soap(FaultCode faultCode, String subCode, Severite severite, String reason) {

            this.faultCode = faultCode;
            this.subCode = subCode;
            this.severite = severite;
            this.reason = reason;
        }

        @NonNull
        public FaultCode getFaultCode() {
            return faultCode;
        }

        @NonNull
        public String getSubCode() {
            return subCode;
        }

        @NonNull
        public Severite getSeverite() {
            return severite;
        }

        @NonNull
        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public static class RestOptions {

        private final ForwardRestClientErrors forwardRestClientErrors;
        private final TranslateSoapSiramErrors translateSoapSiramErrors;

        public RestOptions(
                @DefaultValue ForwardRestClientErrors forwardRestClientErrors,
                @DefaultValue TranslateSoapSiramErrors translateSoapSiramErrors) {
            this.forwardRestClientErrors = forwardRestClientErrors;
            this.translateSoapSiramErrors = translateSoapSiramErrors;
        }

        @NonNull
        public ForwardRestClientErrors getForwardRestClientErrors() {
            return forwardRestClientErrors;
        }

        public TranslateSoapSiramErrors getTranslateSoapSiramErrors() {
            return translateSoapSiramErrors;
        }
    }

    public static class ForwardRestClientErrors {

        /**
         * Indique si les erreurs HTTP type 4XX reçues de service REST distants doivent être transmises telles que au client dès lors
         * qu'elle contiennent un array errors ou problems dont le 1er élément contient au moins <code>id</code> et <code>code</code> et
         * optionnellement <code>message</code>.
         */
        private boolean enabled = false;

        /**
         * La liste des status HTTP lors d'appels à d'autres services REST qui doivent être forwadés tels que au client, si l'option est
         * activée. Seuls les status Http de la série 4XX sont autorisés ici.
         */
        private final List<HttpStatus> status = new ArrayList<>();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @NonNull
        public List<HttpStatus> getStatus() {
            return status;
        }
    }

    public static class TranslateSoapSiramErrors {

        /**
         * Indique si la traduction des Soap Fault de type Sender reçues, en réponses REST pour certains codes erreur (code = valeur de
         * l'attribut code de la l'élément Detail / urn:simram:Erreur des Soap Fault conformes ESPOIR / DESIR / Cadre Interop CNAM)
         */
        private boolean enabled = false;

        /**
         * La {@link Map} des status HTTP à renvoyer en réponse REST pour un code donné reçue dans une Soap Fault conforme ESPOIR / DESIR /
         * Cadre Interop CNAM.
         */
        private final Map<String, HttpStatus> status = new HashMap<>();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @NonNull
        public Map<String, HttpStatus> getStatus() {
            return status;
        }
    }

    public static class SoapOptions {

        private final ForwardSoapSiramErrors forwardSoapSiramErrors;

        public SoapOptions(
                @DefaultValue ForwardSoapSiramErrors forwardSoapSiramErrors) {
            this.forwardSoapSiramErrors = forwardSoapSiramErrors;
        }

        @NonNull
        public ForwardSoapSiramErrors getForwardSoapSiramErrors() {
            return forwardSoapSiramErrors;
        }
    }

    public static class ForwardSoapSiramErrors {

        /**
         * Indique si les Soap Fault de type Sender reçues de service SOAP distants doivent être transmises telles que au client pour
         * certains codes erreur (code = valeur de l'attribut code de la l'élément Detail / urn:simram:Erreur des Soap Fault conformes
         * ESPOIR / DESIR / Cadre Interop CNAM)
         */
        private boolean enabled = false;

        /**
         * La liste des codes erreur lors d'appels à d'autres services SOAP qui doivent être forwadés tels que au client, si l'option est
         * activée. Seuls les erreurs SOAP de type Sender seront forwardées.
         */
        private final List<String> codes = new ArrayList<>();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @NonNull
        public List<String> getCodes() {
            return codes;
        }
    }

    public enum FaultCode {
        Sender, Receiver; // NOSONAR - On veut respecter la case des faultCode standard SOAP
    }

    public enum Severite {
        erreur, fatale, avertissement; // NOSONAR - On veut respecter la case des severités
    }
}
