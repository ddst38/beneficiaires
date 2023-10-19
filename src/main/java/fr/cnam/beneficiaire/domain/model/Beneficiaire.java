package fr.cnam.beneficiaire.domain.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Modèle métier Beneficiaire.
 * <p>
 * Le matricule, la date de naissance et le rang servent d'identifiants métier d'un bénéficiaire.
 *
 * @author Sopra Steria @ CNAM DDST / PER / Pole Back
 */
public class Beneficiaire {

    public static final int MATRICULE_SIZE = 13;
    public static final int RANG_MIN_VALUE = 1;
    public static final int NOM_MIN_SIZE = 1;
    public static final int NOM_MAX_SIZE = 25;
    public static final int PRENOM_MIN_SIZE = 1;
    public static final int PRENOM_MAX_SIZE = 25;

    private final Long id;

    @NotEmpty
    @Size(min = MATRICULE_SIZE, max = MATRICULE_SIZE)
    private final String matricule;

    @NotNull
    private final LocalDate dateNaissance;

    @NotNull
    @Min(value = RANG_MIN_VALUE)
    private final Integer rang;

    @NotEmpty
    @Size(min = NOM_MIN_SIZE, max = NOM_MAX_SIZE)
    private final String nom;

    @NotEmpty
    @Size(min = PRENOM_MIN_SIZE, max = PRENOM_MAX_SIZE)
    private final String prenom;

    /**
     * Constructeur par défaut nécessaire pour les frameworks de désérialisation.<br>
     * On le laisse en private car ne doit pas être utilisé dans le cade applicatif.
     */
    private Beneficiaire() {
        this(null, null, null, null, null, null);
    }

    /**
     * Constructeur avec toutes les propriétés d'un bénéficiaire
     */
    public Beneficiaire(Long id, String matricule, LocalDate dateNaissance, Integer rang, String nom, String prenom) {
        this.id = id;
        this.matricule = matricule;
        this.dateNaissance = dateNaissance;
        this.rang = rang;
        this.nom = nom;
        this.prenom = prenom;
    }

    /**
     * Constructeur par copie d'un bénéficiaire existant
     */
    public Beneficiaire(Beneficiaire beneficiaire) {
        super();
        id = beneficiaire.getId();
        matricule = beneficiaire.getMatricule();
        dateNaissance = beneficiaire.getDateNaissance();
        rang = beneficiaire.getRang();
        nom = beneficiaire.getNom();
        prenom = beneficiaire.getPrenom();
    }

    /**
     * Construit un bénéficiaire sans setter l'id.
     */
    public static Beneficiaire of(String matricule, LocalDate dateNaissance, int rang, String nom, String prenom) {
        return new Beneficiaire(null, matricule, dateNaissance, rang, nom, prenom);
    }

    /**
     * Retourne une instance de {@link Beneficiaire} identique à this mais en y ajoutant un Id
     */
    public Beneficiaire withId(Long id) {
        return new Beneficiaire(id, matricule, dateNaissance, rang, nom, prenom);
    }

    /**
     * Retourne une instance de {@link Beneficiaire} identique à this mais en modifiant le matricule
     */
    public Beneficiaire withMatricule(String matricule) {
        return new Beneficiaire(id, matricule, dateNaissance, rang, nom, prenom);
    }

    /**
     * Retourne une instance de {@link Beneficiaire} identique à this mais en modifiant la date de naissance
     */
    public Beneficiaire withDateNaissance(LocalDate dateNaissance) {
        return new Beneficiaire(id, matricule, dateNaissance, rang, nom, prenom);
    }

    /**
     * Retourne une instance de {@link Beneficiaire} identique à this mais en modifiant le rang
     */
    public Beneficiaire withRang(Integer rang) {
        return new Beneficiaire(id, matricule, dateNaissance, rang, nom, prenom);
    }

    /**
     * Retourne une instance de {@link Beneficiaire} identique à this mais en modifiant le nom
     */
    public Beneficiaire withNom(String nom) {
        return new Beneficiaire(id, matricule, dateNaissance, rang, nom, prenom);
    }

    /**
     * Retourne une instance de {@link Beneficiaire} identique à this mais en modifiant le prénom
     */
    public Beneficiaire withPrenom(String prenom) {
        return new Beneficiaire(id, matricule, dateNaissance, rang, nom, prenom);
    }

    /**
     * Getter de l'id.
     *
     * @return l'id
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter de la dateNaissance.
     *
     * @return La dateNaissance
     */
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Getter du matricule.
     *
     * @return Le matricule
     */
    public String getMatricule() {
        return matricule;
    }

    /**
     * Getter du nom.
     *
     * @return Le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Getter du prenom.
     *
     * @return Le prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Getter du rang.
     *
     * @return Le rang
     */
    public Integer getRang() {
        return rang;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Beneficiaire other)) {
            return false;
        }
        return Objects.equals(dateNaissance, other.dateNaissance) && Objects.equals(matricule, other.matricule)
                && Objects.equals(rang, other.rang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateNaissance, matricule, rang);
    }

    @Override
    public String toString() {
        return String.format("Beneficiaire[id=%d, matricule=%s, dateNaissance=%s, rang=%d, nom=%s, prenom=%s]",
                id, matricule, dateNaissance, rang, nom, prenom);
    }

}
