package fr.cnam.beneficiaire.domain.provider.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import fr.cnam.beneficiaire.domain.model.Beneficiaire;
import fr.cnam.beneficiaire.domain.spi.BeneficiaireRepository;


/**
 * Provider utilisant une base de donnée comme repository, ce provider manipule la base à l'aide du {@link JdbcTemplate} et de requête SQL
 * décritent dans un script SQL et parsé par {@link SqlPropertySourceFactory}
 * <p>
 * Ce provider montre comment effectuer des requêtes en base sur une table "skhd_beneficiaire", la clef primaire de nos bénéficiaires est un
 * ID unique auto-généré par la base à partir d'une séquence.
 * <p>
 * Bien que nous n'utilisons pas d'ID dans l'objet {@link Beneficiaire} de notre exemple, nous souhaitions tout de même montrer comment
 * récupérer l'ID d'un objet lors de la création ou de la lecture d'un bénéficiaire. Pensez à adapter votre code en conséquence !
 * <p>
 * La classe est annotée @{@link Transactional}(propagation = Propagation.MANDATORY) pour imposer l'execution de ces méthodes dans le
 * contexte d'une transaction déjà existante
 *
 * @author Sopra Steria @ CNAM DDST / PER / Pole Back
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class JdbcBeneficiaireRepository implements BeneficiaireRepository, RowMapper<Beneficiaire> {


    /** nom de la table beneficiaire */
    public static final String TABLE_NAME = "skmb_beneficiaire_jdbc";

    /** nom de colonne et de parametre pour l'id technique */
    public static final String COL_ID = "beneficiaire_id";

    /** nom de colonne et de parametre pour le matricule */
    public static final String COL_MATR = "matricule";

    /** nom de colonne et de parametre pour date de naissance */
    public static final String COL_DNAI = "date_naissance";

    /** nom de colonne et de parametre pour le rang */
    public static final String COL_RANG = "rang";

    /** nom de colonne et de parametre pour le nom */
    public static final String COL_NOM = "nom";

    /** nom de colonne et de parametre pour le prénom */
    public static final String COL_PRNM = "prenom";

    //@Value("${usecase.keystore.alias}")
    //private String alias;

    /**
     * Récupération de la requete SQL de creation d'un beneficiaire. L'id technique est automatiquement assigné avec une valeur par défaut
     * basée sur une séquence
     */
    @Value("${usecase.request.create-beneficiaire}")
    private String createBeneficiaireSql;

    /**
     * Récupération de la requete SQL de lecture d'un beneficiaire.
     */
    @Value("${usecase.request.find-beneficiaire.by-id}")
    private String readBeneficiaireSql;

    /**
     * Récupération de la requete SQL de recherche d'un beneficiaire.
     */
    @Value("${usecase.request.find-beneficiaire.by-keys}")
    private String findOneBeneficiaireSql;

    /**
     * Récupération de la requete SQL de recherche de beneficiaires.
     */
    @Value("${usecase.request.find-beneficiaire.by-matricule}")
    private String findAllBeneficiairesSql;

    /**
     * Récupération de la requete SQL de maj d'un beneficiaire par ses clés matricule, date de naissance et rang
     */
    @Value("${usecase.request.update-beneficiaire.by-keys}")
    private String updateBeneficiaireByKeysSql;

    /**
     * Récupération de la requete SQL de maj d'un beneficiaire par son id
     */
    @Value("${usecase.request.update-beneficiaire.by-id}")
    private String updateBeneficiaireByIdSql;

    /**
     * Récupération de la requete SQL de suppression d'un beneficiaire par ses clés matricule, date de naissance et rang
     */
    @Value("${usecase.request.delete-beneficiaire.by-keys}")
    private String deleteBeneficiaireByKeysSql;

    /**
     * Récupération de la requete SQL de suppression d'un beneficiaire par son id.
     */
    @Value("${usecase.request.delete-beneficiaire.by-id}")
    private String deleteBeneficiaireByIdSql;

    /**
     * Le jdbc template à utiliser pour l'execution des requetes SQL
     */
    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Constructeur avec paramètre(s).
     *
     * @param jdbcTemplate le template à utiliser
     */
    @Inject
    public JdbcBeneficiaireRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Beneficiaire createBeneficiaire(Beneficiaire beneficiaire) {
        // création d'un keyholder pour retrouver l'ID généré au besoin
        final KeyHolder idHolder = new GeneratedKeyHolder();

        // ajout des paramètres nommés
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_MATR, beneficiaire.getMatricule())
                .addValue(COL_DNAI, java.sql.Date.valueOf(beneficiaire.getDateNaissance()))
                .addValue(COL_RANG, beneficiaire.getRang())
                .addValue(COL_NOM, beneficiaire.getNom())
                .addValue(COL_PRNM, beneficiaire.getPrenom());

        // execution de la requête et recupération du 'beneficiaire_id' généré
        // attention: pour PostgreSQL le nom de colonne doit être passé en minuscules sinon ne marche pas
        jdbcTemplate.update(createBeneficiaireSql, params, idHolder, new String[] {COL_ID});

        // retourne une nouvelle instance de Beneficiaire avec l'id
        return beneficiaire.withId(idHolder.getKey().longValue());

    }

    /**
     * Catch de {@link EmptyResultDataAccessException} correspondant à un Beneficiaire inconnu car Spring ne gère pas encore l'Optional avec
     * JdbcTemplate : <a href=
     * "https://github.com/spring-projects/spring-framework/issues/17262">https://github.com/spring-projects/spring-framework/issues/17262</a>
     */
    @SuppressWarnings("java:S1166") // On a pas besoin de logger l'exception
    @Override
    public Optional<Beneficiaire> readBeneficiaire(Long id) {
        // affectation des parametres nommés
        final SqlParameterSource params = new MapSqlParameterSource().addValue(COL_ID, id);

        // execution de la requete avec mapping vers Optional<Beneficiaire>
        try {
            return Optional.of(jdbcTemplate.queryForObject(readBeneficiaireSql, params, this));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Catch de {@link EmptyResultDataAccessException} correspondant à un Beneficiaire inconnu car Spring ne gère pas encore l'Optional avec
     * JdbcTemplate : <a href=
     * "https://github.com/spring-projects/spring-framework/issues/17262">https://github.com/spring-projects/spring-framework/issues/17262</a>
     */
    @SuppressWarnings("java:S1166") // On a pas besoin de logger l'exception
    @Override
    public Optional<Beneficiaire> findOneBeneficiaire(String matricule, LocalDate dateNaissance, int rang) {
        // affectation des parametres nommés
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_MATR, matricule)
                .addValue(COL_DNAI, java.sql.Date.valueOf(dateNaissance))
                .addValue(COL_RANG, rang);

        // execution de la requete avec mapping vers Optional<Beneficiaire>
        try {
            return Optional.of(jdbcTemplate.queryForObject(findOneBeneficiaireSql, params, this));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Beneficiaire> findAllBeneficiaires(String matricule) {
        // affectation des parametres nommés
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_MATR, matricule);

        // execution de la requete avec mapping vers List<Beneficiaire>
        return jdbcTemplate.query(findAllBeneficiairesSql, params, this);
    }

    @Override
    public int updateBeneficiaire(Beneficiaire beneficiaire) {

        // affectation des parametres nommés
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_MATR, beneficiaire.getMatricule())
                .addValue(COL_DNAI, java.sql.Date.valueOf(beneficiaire.getDateNaissance()))
                .addValue(COL_RANG, beneficiaire.getRang())
                .addValue(COL_PRNM, beneficiaire.getPrenom())
                .addValue(COL_NOM, beneficiaire.getNom());
        // execution de l'update
        return jdbcTemplate.update(updateBeneficiaireByKeysSql, params);

    }

    @Override
    public int updateBeneficiaire(Long id, Beneficiaire beneficiaire) {
        // affectation des parametres nommés
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, id)
                .addValue(COL_MATR, beneficiaire.getMatricule())
                .addValue(COL_DNAI, java.sql.Date.valueOf(beneficiaire.getDateNaissance()))
                .addValue(COL_RANG, beneficiaire.getRang())
                .addValue(COL_PRNM, beneficiaire.getPrenom())
                .addValue(COL_NOM, beneficiaire.getNom());

        // execution de l'update
        return jdbcTemplate.update(updateBeneficiaireByIdSql, params);
    }

    @Override
    public boolean deleteBeneficiaire(Long id) {
        // ajout des paramètres nommés
        final SqlParameterSource params = new MapSqlParameterSource().addValue(COL_ID, id);

        // exécution de l'update
        return jdbcTemplate.update(deleteBeneficiaireByIdSql, params) == 1;
    }

    @Override
    public boolean deleteBeneficiaire(String matricule, LocalDate dateNaissance, int rang) {
        // ajout des paramètres nommés
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_MATR, matricule)
                .addValue(COL_DNAI, java.sql.Date.valueOf(dateNaissance))
                .addValue(COL_RANG, rang);

        // exécution de l'update
        return jdbcTemplate.update(deleteBeneficiaireByKeysSql, params) == 1;
    }

    /**
     * Mappe une ligne de resultset vers un objet Beneficiaire
     *
     * @param resultSet le resultat de la requete avec le no de ligne courant positionne sur la ligne a mapper
     * @param rownum le no de ligne a mapper (pas utilisé)
     * @return un objet beneficiaire
     * @throws SQLException
     */
    @Override
    public Beneficiaire mapRow(ResultSet rs, int rowNum) throws SQLException {
        // affichage de l'ID des entités lues

        return new Beneficiaire(rs.getLong(COL_ID), rs.getString(COL_MATR), rs.getDate(COL_DNAI).toLocalDate(), rs.getInt(COL_RANG),
                rs.getString(COL_NOM), rs.getString(COL_PRNM));
    }
}
