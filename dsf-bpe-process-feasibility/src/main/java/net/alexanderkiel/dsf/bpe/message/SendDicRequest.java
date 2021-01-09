package net.alexanderkiel.dsf.bpe.message;

import ca.uhn.fhir.context.FhirContext;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.organization.OrganizationProvider;
import org.highmed.dsf.fhir.task.AbstractTaskMessageSend;
import org.highmed.dsf.fhir.task.TaskHelper;

public class SendDicRequest extends AbstractTaskMessageSend {

    public SendDicRequest(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper,
                          OrganizationProvider organizationProvider, FhirContext fhirContext) {
        super(clientProvider, taskHelper, organizationProvider, fhirContext);
    }
}
