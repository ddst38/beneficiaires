package fr.cnam.beneficiaire.api.http;

import fr.cnam.beneficiaire.api.BeneficiaireService;
import fr.cnam.beneficiaire.domain.model.Beneficiaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/beneficiaires")
public class BeneficiairesHttpApi {
    @Autowired
    BeneficiaireService beneficiairesService;

    @GetMapping("/beneficiaire")
    public Mono<Optional<Beneficiaire>> generateGrid(
            @RequestParam Long id){
        return Mono.just(beneficiairesService.readBeneficiaire(id));
    }

}
