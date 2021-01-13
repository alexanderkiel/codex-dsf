package net.alexanderkiel.dsf.bpe.service;

import net.alexanderkiel.dsf.bpe.variables.ConstantsFeasibility;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.highmed.dsf.bpe.delegate.AbstractServiceDelegate;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Meta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

public class StoreMeasureReport extends AbstractServiceDelegate implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(StoreMeasureReport.class);

    public StoreMeasureReport(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper) {
        super(clientProvider, taskHelper);
    }

    @Override
    protected void doExecute(DelegateExecution execution) {
        MeasureReport measureReport = (MeasureReport) execution.getVariable(ConstantsFeasibility.VARIABLE_MEASURE_REPORT);

        IdType measureReportId = storeMeasureReport(measureReport);
        logger.debug("Stored MeasureReport {}", measureReportId);

        execution.setVariable(ConstantsFeasibility.VARIABLE_MEASURE_REPORT_ID, measureReportId.getValue());
    }

    private IdType storeMeasureReport(MeasureReport measureReport) {
        measureReport.setMeta(
                new Meta().setTag(
                        List.of(new Coding()
                                        .setSystem("http://highmed.org/fhir/CodeSystem/authorization-role")
                                        .setCode("LOCAL"),
                                new Coding()
                                        .setSystem("http://highmed.org/fhir/CodeSystem/authorization-role")
                                        .setCode("REMOTE")))
        );
        return getFhirWebserviceClientProvider().getLocalWebserviceClient()
                .withMinimalReturn()
                .create(measureReport);
    }
}
