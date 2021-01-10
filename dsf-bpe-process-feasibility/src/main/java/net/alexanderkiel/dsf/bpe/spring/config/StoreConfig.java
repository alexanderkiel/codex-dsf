package net.alexanderkiel.dsf.bpe.spring.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreConfig {

    @Autowired
    private FhirContext fhirContext;

    @Value("${net.alexanderkiel.dsf.bpe.store.url:foo}")
    private String storeUrl;

    @Bean
    @Qualifier("store")
    public IGenericClient storeClient() {
        return fhirContext.newRestfulGenericClient(storeUrl);
    }
}
