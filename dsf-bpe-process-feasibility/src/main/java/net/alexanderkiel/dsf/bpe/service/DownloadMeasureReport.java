package net.alexanderkiel.dsf.bpe.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.highmed.dsf.bpe.delegate.AbstractServiceDelegate;
import net.alexanderkiel.dsf.bpe.variables.ConstantsFeasibility;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.organization.OrganizationProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.highmed.fhir.client.FhirWebserviceClient;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;
import java.util.Optional;

public class DownloadMeasureReport extends AbstractServiceDelegate implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(DownloadMeasureReport.class);

    private final OrganizationProvider organizationProvider;

    public DownloadMeasureReport(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper,
                                 OrganizationProvider organizationProvider) {
        super(clientProvider, taskHelper);
        this.organizationProvider = organizationProvider;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Objects.requireNonNull(organizationProvider, "organizationProvider");
    }

    @Override
    protected void doExecute(DelegateExecution execution) throws Exception {
        Task task = getCurrentTaskFromExecutionVariables();

        IdType measureReportId = getMeasureReportId(task);
        FhirWebserviceClient client = getWebserviceClient(measureReportId);
        MeasureReport measureReport = client.read(MeasureReport.class, measureReportId.getIdPart());

        int count = measureReport.getGroupFirstRep().getPopulationFirstRep().getCount();
        execution.setVariable(ConstantsFeasibility.VARIABLE_COUNT, count);
    }

    private IdType getMeasureReportId(Task task) {
        Optional<Reference> measureRef = getTaskHelper()
                .getFirstInputParameterReferenceValue(task, ConstantsFeasibility.CODESYSTEM_FEASIBILITY,
                        ConstantsFeasibility.CODESYSTEM_FEASIBILITY_VALUE_MEASURE_REPORT_REFERENCE);
        if (measureRef.isPresent()) {
            return new IdType(measureRef.get().getReference());
        } else {
            logger.error("Task {} is missing the measure report reference.", task.getId());
            throw new RuntimeException("Missing measure report reference.");
        }
    }

    private FhirWebserviceClient getWebserviceClient(IdType researchStudyId) {
        if (researchStudyId.getBaseUrl() == null || researchStudyId.getBaseUrl()
                .equals(getFhirWebserviceClientProvider().getLocalBaseUrl())) {
            return getFhirWebserviceClientProvider().getLocalWebserviceClient();
        } else {
            return getFhirWebserviceClientProvider().getRemoteWebserviceClient(researchStudyId.getBaseUrl());
        }
    }
}