package fr.cnam.beneficiaire;


import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
//public class BeneficiairesApplication extends SpringBootServletInitializer  {
public class BeneficiairesApplication {
    public static void main(String[] args) {
        if ("yes".equals(System.getenv("BENEF_CLI"))) {
            SpringApplicationBuilder appBuilder = new SpringApplicationBuilder(BeneficiairesApplicationCli.class);
            appBuilder.web(WebApplicationType.NONE)
                    .bannerMode(Banner.Mode.OFF)
                    .logStartupInfo(false);
            appBuilder.build().run(args);
        } else {
            SpringApplication.run(BeneficiairesApplication.class, args);
        }
    }

}
