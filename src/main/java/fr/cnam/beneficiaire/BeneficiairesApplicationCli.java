package fr.cnam.beneficiaire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Component;

import fr.cnam.beneficiaire.api.BeneficiaireService;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@ConditionalOnNotWebApplication
public class BeneficiairesApplicationCli implements CommandLineRunner {
    @Autowired
    BeneficiaireService beneficiairesService;
    
    @Override
    public void run(String... args) {
        AtomicInteger i = new AtomicInteger();
        Map<Integer, String> argsMap =
                Arrays.stream(args).collect(Collectors.toMap(a -> i.getAndIncrement(), a -> a));
        String id = argsMap.getOrDefault(0, "0125648521354");
        Long lid = Long.parseUnsignedLong(id);
        System.out.println(beneficiairesService.readBeneficiaire(lid));
    }
}