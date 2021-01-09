package net.alexanderkiel.dsf.bpe.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.highmed.dsf.bpe.delegate.AbstractServiceDelegate;
import net.alexanderkiel.dsf.bpe.variables.ConstantsFeasibility;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.springframework.beans.factory.InitializingBean;

public class AggregateResults extends AbstractServiceDelegate implements InitializingBean {

    public AggregateResults(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper) {
        super(clientProvider, taskHelper);
    }

    @Override
    protected void doExecute(DelegateExecution execution) throws Exception {
        Integer sum = (Integer) execution.getVariable(ConstantsFeasibility.VARIABLE_SUM);
        int count = (int) execution.getVariable(ConstantsFeasibility.VARIABLE_COUNT);

        execution.setVariable(ConstantsFeasibility.VARIABLE_SUM, sum == null ? count : sum + count);
    }
}
