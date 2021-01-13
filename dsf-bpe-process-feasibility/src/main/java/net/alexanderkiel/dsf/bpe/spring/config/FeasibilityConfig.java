package net.alexanderkiel.dsf.bpe.spring.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import net.alexanderkiel.dsf.bpe.message.SendDicRequest;
import net.alexanderkiel.dsf.bpe.message.SendDicResponse;
import net.alexanderkiel.dsf.bpe.service.AggregateResults;
import net.alexanderkiel.dsf.bpe.service.DownloadFeasibilityResources;
import net.alexanderkiel.dsf.bpe.service.DownloadMeasureReport;
import net.alexanderkiel.dsf.bpe.service.EvaluateMeasure;
import net.alexanderkiel.dsf.bpe.service.SelectRequestTargets;
import net.alexanderkiel.dsf.bpe.service.SelectResponseTarget;
import net.alexanderkiel.dsf.bpe.service.StoreFeasibilityResources;
import net.alexanderkiel.dsf.bpe.service.StoreMeasureReport;
import net.alexanderkiel.dsf.bpe.service.StoreResult;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.organization.OrganizationProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeasibilityConfig {

    @Autowired
    private FhirWebserviceClientProvider fhirClientProvider;

    @Autowired
    @Qualifier("store")
    private IGenericClient storeClient;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private TaskHelper taskHelper;

    @Autowired
    private FhirContext fhirContext;

    //
    // process requestSimpleFeasibility implementations
    //

    @Bean
    public SelectRequestTargets selectRequestTargets() {
        return new SelectRequestTargets(fhirClientProvider, taskHelper, organizationProvider);
    }

    @Bean
    public SendDicRequest sendDicRequest() {
        return new SendDicRequest(fhirClientProvider, taskHelper, organizationProvider, fhirContext);
    }

    @Bean
    public DownloadMeasureReport downloadMeasureReport() {
        return new DownloadMeasureReport(fhirClientProvider, taskHelper, organizationProvider);
    }

    @Bean
    public AggregateResults aggregateResults() {
        return new AggregateResults(fhirClientProvider, taskHelper);
    }

    @Bean
    public StoreResult storeResult() {
        return new StoreResult(fhirClientProvider, taskHelper);
    }

    //
    // process executeSimpleFeasibility implementations
    //

    @Bean
    public DownloadFeasibilityResources downloadFeasibilityResources() {
        return new DownloadFeasibilityResources(fhirClientProvider, taskHelper, organizationProvider);
    }

    @Bean
    public StoreFeasibilityResources storeFeasibilityResources() {
        return new StoreFeasibilityResources(fhirClientProvider, taskHelper, storeClient);
    }

    @Bean
    public EvaluateMeasure evaluateMeasure() {
        return new EvaluateMeasure(fhirClientProvider, taskHelper, storeClient);
    }

    @Bean
    public StoreMeasureReport storeMeasureReport() {
        return new StoreMeasureReport(fhirClientProvider, taskHelper);
    }

    @Bean
    public SelectResponseTarget selectResponseTarget() {
        return new SelectResponseTarget(fhirClientProvider, taskHelper, organizationProvider);
    }

    @Bean
    public SendDicResponse sendDicResponse() {
        return new SendDicResponse(fhirClientProvider, taskHelper, organizationProvider, fhirContext);
    }
}
