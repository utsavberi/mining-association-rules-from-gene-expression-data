package parser;

import java.util.HashSet;

import ruleMiner.AssociationRule;

/**
 * @author Manpreet
 *
 */
public class Template2Runner implements TemplateRunner{

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
	    	//System.out.println(associationRule);
	    	rulecompliesToTemplate = true;	    
	    }
	    	return rulecompliesToTemplate;
	}

	private int getSize(HashSet body) {
		// TODO Auto-generated method stub
	return body.size();	
	}

}
