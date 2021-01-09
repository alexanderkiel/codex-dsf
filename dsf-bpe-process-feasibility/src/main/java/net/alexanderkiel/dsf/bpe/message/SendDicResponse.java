package net.alexanderkiel.dsf.bpe.message;

import ca.uhn.fhir.context.FhirContext;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import net.alexanderkiel.dsf.bpe.variables.ConstantsFeasibility;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.organization.OrganizationProvider;
import org.highmed.dsf.fhir.task.AbstractTaskMessageSend;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;

import java.util.stream.Stream;

public class SendDicResponse extends AbstractTaskMessageSend {

    public SendDicResponse(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper,
                           OrganizationProvider organizationProvider, FhirContext fhirContext) {
        super(clientProvider, taskHelper, organizationProvider, fhirContext);
    }

    @Override
    protected Stream<Task.ParameterComponent> getAdditionalInputParameters(DelegateExecution execution) {
        String measureReportId = (String) execution.getVariable(ConstantsFeasibility.VARIABLE_MEASURE_REPORT_ID);

        return Stream.of(getTaskHelper().createInput(ConstantsFeasibility.CODESYSTEM_FEASIBILITY,
                ConstantsFeasibility.CODESYSTEM_FEASIBILITY_VALUE_MEASURE_REPORT_REFERENCE,
                new Reference().setReference(measureReportId)));
    }
}
