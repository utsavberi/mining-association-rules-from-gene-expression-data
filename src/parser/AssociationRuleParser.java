package parser;

import ruleMiner.AssociationRule;

/**
 * @author Manpreet
 *
 */
public class AssociationRuleParser {
	/**
	 * 
	 * takes the input query and association rule as param and decides which
	 * template to call based on inputquery If the rule complies with the given
	 * inputquery, this method returns true
	 * 
	 * @param inputQuery
	 * @param associationRule
	 * @return boolean value
	 */
	public boolean runParser(String inputQuery, AssociationRule associationRule) 
	{
		TemplateRunner templateRunner;
		boolean ruleCompliesToTemplate = false;
		String outputGeneRules = new String();
		if (inputQuery.contains(Constants.AND) || inputQuery.contains(Constants.OR))
		  {
			templateRunner = new Template3Runner();
			ruleCompliesToTemplate = templateRunner.ruleMatchesTemplate(inputQuery, associationRule);
		  }
		else if (inputQuery.contains(Constants.SIZE_OF)) 
		  {
			templateRunner = new Template2Runner();
			ruleCompliesToTemplate = templateRunner.ruleMatchesTemplate(inputQuery, associationRule);
		  } 
		else if (inputQuery.contains(Constants.HAS)) 
		  {
			templateRunner = new Template1Runner();
			ruleCompliesToTemplate = templateRunner.ruleMatchesTemplate(inputQuery, associationRule);
		  }
		return ruleCompliesToTemplate;
	}
}

