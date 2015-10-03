package parser;

import java.util.HashSet;

import ruleMiner.AssociationRule;

/**
 * @author Manpreet
 *
 */
public class Template2Runner implements TemplateRunner{

	/**
	 * runs the rule against template 2: SizeOf({BODY|HEAD|RULE}) ≥ NUMBER

	 * @param inputQuery the given template
	 * @param associationRule generated through aprori
	 * @returns true if rule complies to template 2 
	 */
	@Override
	public boolean ruleMatchesTemplate(String inputQuery, AssociationRule associationRule) {
		// TODO Auto-generated method stub
		HashSet body = associationRule.body;
		HashSet head = associationRule.head;
	    int sizeOfRulePart = 0;
	    boolean rulecompliesToTemplate = false;
	    int thresholdValue = Integer.parseInt(inputQuery.split(">=")[1].toString().trim());
	    if(inputQuery.contains(Constants.BODY))
	      {
	    	sizeOfRulePart = getSize(body);
	      }
	    else
	    if(inputQuery.contains(Constants.HEAD))
	      {
	    		sizeOfRulePart = getSize(head);
	      }
	    else
	    if(inputQuery.contains(Constants.RULE))
	      {
	    	sizeOfRulePart = getSize(head) + getSize(body);
	      }
	    if(sizeOfRulePart >= thresholdValue)
	      {
	    	rulecompliesToTemplate = true;	    
	      }
	    return rulecompliesToTemplate;
	}

	private int getSize(HashSet body) 
	{
	  return body.size();	
	}

}