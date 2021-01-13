package net.alexanderkiel.dsf.bpe.service;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import net.alexanderkiel.dsf.bpe.message.SendDicRequest;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.highmed.dsf.bpe.delegate.AbstractServiceDelegate;
import net.alexanderkiel.dsf.bpe.variables.ConstantsFeasibility;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;

public class EvaluateMeasure extends AbstractServiceDelegate implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(EvaluateMeasure.class);

    private final IGenericClient storeClient;

    public EvaluateMeasure(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper,
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
        String measureId = (String) execution.getVariable(ConstantsFeasibility.VARIABLE_MEASURE_ID);

        MeasureReport report = executeEvaluateMeasure(measureId);

        execution.setVariable(ConstantsFeasibility.VARIABLE_MEASURE_REPORT, report);
    }

    private MeasureReport executeEvaluateMeasure(String measureId) {
        logger.debug("Evaluate measure {}", measureId);
        return storeClient.operation().onInstance("Measure/" + measureId).named("evaluate-measure")
                .withParameter(Parameters.class, "periodStart", new DateType(1900, 1, 1))
                .andParameter("periodEnd", new DateType(2100, 1, 1))
                .useHttpGet()
                .returnResourceType(MeasureReport.class)
                .execute();
    }
}
