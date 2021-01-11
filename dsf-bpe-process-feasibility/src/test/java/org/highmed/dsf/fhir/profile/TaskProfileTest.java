package org.highmed.dsf.fhir.profile;

import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.ValidationResult;
import org.highmed.dsf.fhir.validation.ResourceValidator;
import org.highmed.dsf.fhir.validation.ResourceValidatorImpl;
import org.highmed.dsf.fhir.validation.ValidationSupportRule;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Task.TaskIntent;
import org.hl7.fhir.r4.model.Task.TaskStatus;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TaskProfileTest {

    @ClassRule
    public static final ValidationSupportRule validationRule = new ValidationSupportRule(Arrays.asList(
            "highmed-task-base-0.4.0.xml", "codex-task-request-simple-feasibility.xml",
            "codex-task-execute-simple-feasibility.xml", "codex-task-single-dic-result-simple-feasibility.xml"),
            Arrays.asList("authorization-role-0.4.0.xml", "bpmn-message-0.4.0.xml", "feasibility.xml"),
            Arrays.asList("authorization-role-0.4.0.xml", "bpmn-message-0.4.0.xml", "feasibility.xml"));

    private static final Logger logger = LoggerFactory.getLogger(TaskProfileTest.class);

    private final ResourceValidator resourceValidator = new ResourceValidatorImpl(validationRule.getFhirContext(),
            validationRule.getValidationSupport());

    @Test
    public void testTaskRequestSimpleFeasibilityValid() {
        Task task = createValidTaskRequestSimpleFeasibility();

        ValidationResult result = resourceValidator.validate(task);
        ValidationSupportRule.logValidationMessages(logger, result);

        assertEquals(0, result.getMessages().stream().filter(m -> ResultSeverityEnum.ERROR.equals(m.getSeverity())
                || ResultSeverityEnum.FATAL.equals(m.getSeverity())).count());
    }

    private Task createValidTaskRequestSimpleFeasibility() {
        Task task = new Task();
        task.getMeta()
                .addProfile("http://alexanderkiel.net/fhir/StructureDefinition/codex-task-request-simple-feasibility");
        task.setInstantiatesUri("http://highmed.org/bpe/Process/requestSimpleFeasibility/0.1.0");
        task.setStatus(TaskStatus.REQUESTED);
        task.setIntent(TaskIntent.ORDER);
        task.setAuthoredOn(new Date());
        task.getRequester().setType("Organization").getIdentifier()
                .setSystem("http://highmed.org/fhir/NamingSystem/organization-identifier").setValue("Test_DIC_1");
        task.getRestriction().addRecipient().setType("Organization").getIdentifier()
                .setSystem("http://highmed.org/fhir/NamingSystem/organization-identifier").setValue("Test_DIC_1");

        task.addInput().setValue(new StringType("requestSimpleFeasibilityMessage")).getType().addCoding()
                .setSystem("http://highmed.org/fhir/CodeSystem/bpmn-message").setCode("message-name");
        task.addInput().setValue(new Reference("Measure/" + UUID.randomUUID())).getType().addCoding()
                .setSystem("http://alexanderkiel.net/fhir/CodeSystem/feasibility").setCode("measure-reference");

        return task;
    }

    @Test
    public void testTaskExecuteSimpleFeasibilityValid() {
        Task task = createValidTaskExecuteSimpleFeasibility();

        ValidationResult result = resourceValidator.validate(task);
        ValidationSupportRule.logValidationMessages(logger, result);

        assertEquals(0, result.getMessages().stream().filter(m -> ResultSeverityEnum.ERROR.equals(m.getSeverity())
                || ResultSeverityEnum.FATAL.equals(m.getSeverity())).count());
    }

    private Task createValidTaskExecuteSimpleFeasibility() {
        Task task = new Task();
        task.getMeta()
                .addProfile("http://alexanderkiel.net/fhir/StructureDefinition/codex-task-execute-simple-feasibility");
        task.setInstantiatesUri("http://highmed.org/bpe/Process/executeSimpleFeasibility/0.1.0");
        task.setStatus(TaskStatus.REQUESTED);
        task.setIntent(TaskIntent.ORDER);
        task.setAuthoredOn(new Date());
        task.getRequester().setType("Organization").getIdentifier()
                .setSystem("http://highmed.org/fhir/NamingSystem/organization-identifier").setValue("Test_DIC_1");
        task.getRestriction().addRecipient().setType("Organization").getIdentifier()
                .setSystem("http://highmed.org/fhir/NamingSystem/organization-identifier").setValue("Test_DIC_2");

        task.addInput().setValue(new StringType("executeSimpleFeasibilityMessage")).getType().addCoding()
                .setSystem("http://highmed.org/fhir/CodeSystem/bpmn-message").setCode("message-name");
        task.addInput().setValue(new StringType(UUID.randomUUID().toString())).getType().addCoding()
                .setSystem("http://highmed.org/fhir/CodeSystem/bpmn-message").setCode("business-key");
        task.addInput().setValue(new StringType(UUID.randomUUID().toString())).getType().addCoding()
                .setSystem("http://highmed.org/fhir/CodeSystem/bpmn-message").setCode("correlation-key");
        task.addInput().setValue(new Reference("Measure/" + UUID.randomUUID())).getType().addCoding()
                .setSystem("http://alexanderkiel.net/fhir/CodeSystem/feasibility").setCode("measure-reference");

        return task;
    }

    @Test
    public void testTaskSingleDicResultSimpleFeasibilityResultValid() {
        Task task = createValidTaskSingleDicResultSimpleFeasibility();

        ValidationResult result = resourceValidator.validate(task);
        ValidationSupportRule.logValidationMessages(logger, result);

        assertEquals(0, result.getMessages().stream().filter(m -> ResultSeverityEnum.ERROR.equals(m.getSeverity())
                || ResultSeverityEnum.FATAL.equals(m.getSeverity())).count());
    }

    private Task createValidTaskSingleDicResultSimpleFeasibility() {
        Task task = new Task();
        task.getMeta().addProfile(
                "http://alexanderkiel.net/fhir/StructureDefinition/codex-task-single-dic-result-simple-feasibility");
        task.setInstantiatesUri("http://highmed.org/bpe/Process/computeSimpleFeasibility/0.1.0");
        task.setStatus(TaskStatus.REQUESTED);
        task.setIntent(TaskIntent.ORDER);
        task.setAuthoredOn(new Date());
        task.getRequester().setType("Organization").getIdentifier()
                .setSystem("http://highmed.org/fhir/NamingSystem/organization-identifier").setValue("Test_DIC_1");
        task.getRestriction().addRecipient().setType("Organization").getIdentifier()
                .setSystem("http://highmed.org/fhir/NamingSystem/organization-identifier").setValue("Test_ZARS");

        task.addInput().setValue(new StringType("resultSingleDicSimpleFeasibilityMessage")).getType().addCoding()
                .setSystem("http://highmed.org/fhir/CodeSystem/bpmn-message").setCode("message-name");
        task.addInput().setValue(new StringType(UUID.randomUUID().toString())).getType().addCoding()
                .setSystem("http://highmed.org/fhir/CodeSystem/bpmn-message").setCode("business-key");
        task.addInput().setValue(new StringType(UUID.randomUUID().toString())).getType().addCoding()
                .setSystem("http://highmed.org/fhir/CodeSystem/bpmn-message").setCode("correlation-key");
        task.addInput().setValue(new Reference("MeasureReport/" + UUID.randomUUID())).getType().addCoding()
                .setSystem("http://alexanderkiel.net/fhir/CodeSystem/feasibility").setCode("measure-report-reference");

        return task;
    }
}
