package parser;

import java.util.Stack;

import ruleMiner.AssociationRule;


/**
 * @author Manpreet
 *
 */
public class Template3Runner implements TemplateRunner{

	/* (non-Javadoc)
	 * @see TemplateRunner#ruleMatchesTemplate(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean ruleMatchesTemplate(String inputQuery, AssociationRule associationRule) {
		// TODO Auto-generated method stub
		AssociationRuleParser associationRuleParser =new AssociationRuleParser();
		Stack<Boolean> stack = new Stack<Boolean>();
		boolean ruleCompliesWithTemplate = false;
		if(inputQuery.contains(Constants.AND) || inputQuery.contains(Constants._AND) )
		{
			String[] templates = inputQuery.split("AND");
			for(String template: templates)
			{
				System.out.println("Template "+ template.trim());
				
				stack.push (associationRuleParser.runParser(template.trim(), associationRule));
			
			}
			
				 if(stack.pop() && stack.pop())
					 ruleCompliesWithTemplate = true; 
				
		}
		else
			if(inputQuery.contains(Constants.OR))
			{
				String[] templates = inputQuery.split("OR");
				for(String template: templates)
				{
					stack.push (associationRuleParser.runParser(template.trim(), associationRule));
				
				}
				
					 if(stack.pop() || stack.pop())
						 ruleCompliesWithTemplate = true; 
			}
		return ruleCompliesWithTemplate;
	}

}
