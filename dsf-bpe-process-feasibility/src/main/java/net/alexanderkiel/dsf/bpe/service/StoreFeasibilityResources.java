package net.alexanderkiel.dsf.bpe.service;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.highmed.dsf.bpe.delegate.AbstractServiceDelegate;
import net.alexanderkiel.dsf.bpe.variables.ConstantsFeasibility;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Measure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;

public class StoreFeasibilityResources extends AbstractServiceDelegate implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(StoreFeasibilityResources.class);

    private final IGenericClient storeClient;

    public StoreFeasibilityResources(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper,
                                     IGenericClient storeClient) {
        super(clientProvider, taskHelper);

        this.storeClient = storeClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        Objects.requireNonNull(storeClient, "storeClient");
    }

    @Override
    protected void doExecute(DelegateExecution execution) {
        Measure measure = (Measure) execution.getVariable(ConstantsFeasibility.VARIABLE_MEASURE);
        Library library = (Library) execution.getVariable(ConstantsFeasibility.VARIABLE_LIBRARY);

        Bundle transactionResponse = storeResources(measure, library);

        execution.setVariable(ConstantsFeasibility.VARIABLE_MEASURE_ID, extractMeasureId(transactionResponse));
    }

    private Bundle storeResources(Measure measure, Library library) {
        logger.info("Store Measure `{}` and Library `{}`", measure.getId(), library.getUrl());

        Bundle bundle = new Bundle().setType(Bundle.BundleType.TRANSACTION);
        bundle.addEntry().setResource(measure).getRequest()
                .setMethod(Bundle.HTTPVerb.POST).setUrl("Measure");
        bundle.addEntry().setResource(library).getRequest()
                .setMethod(Bundle.HTTPVerb.POST).setUrl("Library");
        return storeClient.transaction().withBundle(bundle).execute();
    }

    private String extractMeasureId(Bundle transactionResponse) {
        return new IdType(transactionResponse.getEntryFirstRep().getResponse().getLocation()).getIdPart();
    }
}
