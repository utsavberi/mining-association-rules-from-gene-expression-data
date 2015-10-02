package parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ruleMiner.AssociationRule;


public class AssociationRuleParser 
{
	/**
	 * @param args
	 * @throws IOException
	 */
//	public static void main(String args[]) throws IOException{
//		InputReader inputReader = new InputReader();
////		List<String> associationRules = inputReader.getSampleData("D:\\Study\\Semester 3\\Data Mining\\HW2\\association_rules.txt");
//		//String inputQuery = "SizeOf(HEAD) >= 2";
////		String inputQuery = "BODY HAS 1 of (Gene2_Down, Gene1_UP) OR BODY HAS 1 of (Gene1_UP)";
//		ArrayList<String> outputAssciationRules = new ArrayList<String>();
//		String returnedAssociationRule;
//		/*for(String associationRule : associationRules)
//		{
//			if(runParser(inputQuery,associationRule))
//			outputAssciationRules.add(associationRule);
//		}
//	*/	if(outputAssciationRules!=null || !outputAssciationRules.isEmpty())
//		printRules(outputAssciationRules);
//	}

//	private static void printRules(ArrayList<String> outputAssciationRules) {
//		// TODO Auto-generated method stub
//		for(String associationRule : outputAssciationRules)
//		{
//			System.out.println(associationRule);
//		}
//		
//	}

	/**
	 * @param inputQuery
	 * @param associationRule
	 * @return
	 */
	/**
	 * @param inputQuery
	 * @param associationRule
	 * @return
	 */
	public boolean runParser(String inputQuery, AssociationRule associationRule) {
		// TODO Auto-generated method stub
		//System.out.println("run");
		TemplateRunner templateRunner;
		boolean ruleCompliesToTemplate = false;
//		String outputGeneRules = new String();
		if(inputQuery.contains(Constants.AND)||inputQuery.contains(Constants.OR))
		{
			templateRunner = new Template3Runner();
			ruleCompliesToTemplate = templateRunner.ruleMatchesTemplate(inputQuery,associationRule);
		}
		else
		if(inputQuery.contains(Constants.SIZE_OF))
		{
			templateRunner = new Template2Runner();
			ruleCompliesToTemplate = templateRunner.ruleMatchesTemplate(inputQuery,associationRule);
		}
		else
		if(inputQuery.contains(Constants.HAS))
		{
			templateRunner = new Template1Runner();
			ruleCompliesToTemplate = templateRunner.ruleMatchesTemplate(inputQuery,associationRule);
		}
		//System.out.println("return from run parser for " +inputQuery+ " and rule "+associationRule.body.toString()+ " "+associationRule.head.toString()+" Result->"+ruleCompliesToTemplate);
		return ruleCompliesToTemplate;
	}
}
