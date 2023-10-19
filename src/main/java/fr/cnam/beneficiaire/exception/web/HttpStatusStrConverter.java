package fr.cnam.beneficiaire.exception.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;

/**
 * Classe {@link Converter} permettant de convertir une chaine correspondant à un code (numérique) Http en une instance de
 * {@link HttpStatus} correspondante.
 * <p>
 * Ce Converter est utilisé par Spring pour le mapping de valeurs dans un .yml/.properties vers un objet de type HttpStatus contenu dans une
 * classe annotée {@link ConfigurationProperties}.
 *
 * @author Sopra Steria @ CNAM DDST / PER / Pole Back
 */
@ConfigurationPropertiesBinding
public class HttpStatusStrConverter implements Converter<String, HttpStatus> {

    @Override
    public HttpStatus convert(String source) {

        if (source == null) {
            return null;
        }

        return HttpStatus.valueOf(Integer.valueOf(source));
    }
}
