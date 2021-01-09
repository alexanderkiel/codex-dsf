package net.alexanderkiel.dsf.bpe.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.highmed.dsf.bpe.delegate.AbstractServiceDelegate;
import net.alexanderkiel.dsf.bpe.variables.ConstantsFeasibility;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.MeasureReport;
import org.springframework.beans.factory.InitializingBean;

public class StoreMeasureReport extends AbstractServiceDelegate implements InitializingBean {

    public StoreMeasureReport(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper) {
        super(clientProvider, taskHelper);
    }

    @Override
    protected void doExecute(DelegateExecution execution) {
        MeasureReport measureReport = (MeasureReport) execution.getVariable(ConstantsFeasibility.VARIABLE_MEASURE_REPORT);

        Bundle transactionResponse = storeMeasureReport(measureReport);

        execution.setVariable(ConstantsFeasibility.VARIABLE_MEASURE_REPORT_ID,
                extractMeasureReportId(transactionResponse));
    }

    private Bundle storeMeasureReport(MeasureReport measureReport) {
        Bundle bundle = new Bundle().setType(Bundle.BundleType.TRANSACTION);
        bundle.addEntry().setResource(measureReport).getRequest()
                .setMethod(Bundle.HTTPVerb.POST).setUrl("MeasureReport");
        return getFhirWebserviceClientProvider().getLocalWebserviceClient().withMinimalReturn().postBundle(bundle);
    }

    private String extractMeasureReportId(Bundle transactionResponse) {
        return transactionResponse.getEntryFirstRep().getResponse().getLocation();
    }
}
