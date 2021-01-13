package net.alexanderkiel.dsf.bpe.variables;

public interface ConstantsFeasibility
{
	String VARIABLE_MEASURE = "measure";
	String VARIABLE_LIBRARY = "library";
	String VARIABLE_MEASURE_ID = "measure-id";
	String VARIABLE_MEASURE_REPORT = "measure-report";
	String VARIABLE_MEASURE_REPORT_ID = "measure-report-id";
	String VARIABLE_AGGREGATED_MEASURE_REPORT = "count";

	String CODESYSTEM_FEASIBILITY = "http://alexanderkiel.net/fhir/CodeSystem/feasibility";
	String CODESYSTEM_FEASIBILITY_VALUE_MEASURE_REFERENCE = "measure-reference";
	String CODESYSTEM_FEASIBILITY_VALUE_MEASURE_REPORT_REFERENCE = "measure-report-reference";
	String CODESYSTEM_FEASIBILITY_VALUE_SINGLE_RESULT = "single";
	String CODESYSTEM_FEASIBILITY_VALUE_AGGREGATED_RESULT = "aggregated";

	String EXTENSION_DIC_URI = "http://alexanderkiel.net/fhir/StructureDefinition/dic";
}
