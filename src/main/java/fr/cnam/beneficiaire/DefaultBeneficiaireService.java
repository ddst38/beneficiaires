package fr.cnam.beneficiaire;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import jakarta.inject.Inject;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import fr.cnam.beneficiaire.exception.web.ClientException;
import fr.cnam.beneficiaire.exception.web.CommonProblemType;
import fr.cnam.beneficiaire.api.BeneficiaireService;
import fr.cnam.beneficiaire.domain.model.Beneficiaire;
import fr.cnam.beneficiaire.domain.spi.BeneficiaireRepository;


/**
 * Implémentation de l'interface {@link BeneficiaireService}.
 *
 * Partie métier de l'application, c'est ici que doivent se retrouver les règles de gestion.
 * <p>
 * C'est dans cette couche que les transactions sont démarrées: la classe est annotée {@link @Transactional} afin qu'elle soit appliquée à
 * toutes les méthodes.
 *
 * @author Sopra Steria @ CNAM DDST / PER / Pole Back
 */
@Service
@Transactional
public class DefaultBeneficiaireService implements BeneficiaireService {

    /**
     * Reposiory de persistence des {@link Beneficiaire}
     */
    private final BeneficiaireRepository repository;


    /**
     * Constructeur avec paramètre
     *
     * @param repository Le repository de persistence des {@link Beneficiaire}
     */
    @Inject
    public DefaultBeneficiaireService(BeneficiaireRepository repository) {
        this.repository = repository;
    }

    @Override
    public Beneficiaire createBeneficiaire(Beneficiaire beneficiaire) {
        return repository.createBeneficiaire(beneficiaire.withId(null));
    }

    @Override
    public Optional<Beneficiaire> readBeneficiaire(Long id) {
        //random retour pour variabiliser les tests
        Random r = new Random();
        int low = 1;
        int high = 8;
        Long result = (long)(r.nextInt(high-low) + low);
        Optional<Beneficiaire> optBenefRead = repository.readBeneficiaire(result);
        if (optBenefRead.isPresent()) {
            return Optional.of(optBenefRead.get());
        }
        return optBenefRead;
    }

    @Override
    public Optional<Beneficiaire> findOneBeneficiaire(String matricule,
            LocalDate dateNaissance, int rang) {
        Optional<Beneficiaire> optBenefRead = repository.findOneBeneficiaire(matricule, dateNaissance, rang);
        if (optBenefRead.isPresent()) {
            return Optional.of(optBenefRead.get());
        }
        return optBenefRead;
    }

    @Override
    public List<Beneficiaire> findAllBeneficiaires(String matricule) {
        return repository.findAllBeneficiaires(matricule);
    }

    @Override
    public Beneficiaire updateBeneficiaire(Beneficiaire beneficiaire) {

        if (0 == repository.updateBeneficiaire(beneficiaire.withId(null))) {
            throw new ClientException(CommonProblemType.RESSOURCE_NON_TROUVEE);
        }
        // retourne le bénéficiaire avec ses valeurs à jour après l'update
        return findOneBeneficiaire(beneficiaire.getMatricule(), beneficiaire.getDateNaissance(),
                beneficiaire.getRang()).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public Beneficiaire updateBeneficiaire(Long id, Beneficiaire beneficiaire) {

        if (0 == repository.updateBeneficiaire(id, beneficiaire.withId(null))) {
            throw new ClientException(CommonProblemType.RESSOURCE_NON_TROUVEE);
        }

        // Le bénéficaire passé en paramètre écrasera totalement celui en base, inutile d'aller le rechercher
        return beneficiaire.withId(id);
    }

    @Override
    public boolean deleteBeneficiaire(Long id) {
        return repository.deleteBeneficiaire(id);
    }

    @Override
    public boolean deleteBeneficiaire(String matricule, LocalDate dateNaissance, int rang) {
        return repository.deleteBeneficiaire(matricule, dateNaissance, rang);
    }




}
