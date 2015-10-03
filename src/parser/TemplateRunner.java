package parser;

import ruleMiner.AssociationRule;

/**
 * The interface runs the association rule against the templates to 
 * check if rule belongs to template
 * @author Manpreet 
 */
interface TemplateRunner {
	/**
	 * @param inputQuery
	 * @param associationRule
	 * @return boolean value
	 */
	
	public boolean ruleMatchesTemplate(String inputQuery, AssociationRule associationRule);
}