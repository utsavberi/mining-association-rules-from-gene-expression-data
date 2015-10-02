package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import ruleMiner.AssociationRule;


/**
 * @author Manpreet
 *
 */
public class Template1Runner implements TemplateRunner {

	@Override
	public boolean ruleMatchesTemplate(String inputQuery, AssociationRule associationRule) {
		// TODO Auto-generated method stub
		boolean rulecompliesToTemplate = false;
		int sizeOfRulePart;
	    int thresholdValue;
	    String outputAssociationRule = new String();
	    String rulePart= inputQuery.split("\\s+")[0];
	    int startOfLeftBrace = inputQuery.indexOf('(');
	    int startOfRightBrace = inputQuery.indexOf(')');
	    String geneStringFromRule = inputQuery.substring(startOfLeftBrace+1, startOfRightBrace);
	    sizeOfRulePart = getTotalNumberOfItemSets(rulePart, geneStringFromRule, associationRule);
	    if(inputQuery.contains(Constants.ANY))
	    {
	    	if(sizeOfRulePart > 0)
	    		{System.out.println(associationRule);
	    		 rulecompliesToTemplate = true;
	    		}
	    		
	    }
	    else
	    	if(inputQuery.contains(Constants.NONE))
		    {
	    		if(sizeOfRulePart == 0)
	    			rulecompliesToTemplate = true;
		    }
	    	else
	    		if(!inputQuery.contains(Constants.ANY)||!inputQuery.contains(Constants.NONE))
	    	{
	    		 thresholdValue = Integer.parseInt(inputQuery.split("\\s+")[2]);
	    		 if(sizeOfRulePart == thresholdValue)
	    		 {
	    	    		//System.out.println(associationRule);
;	    	    		rulecompliesToTemplate = true;
	    		 }  
	        }
		return rulecompliesToTemplate;
	        }

	private int getTotalNumberOfItemSets(String rulePart,String geneString, AssociationRule associationRule ) {
		// TODO Auto-generated method stub
		HashSet<String> body = associationRule.body;
	    HashSet<String> head = associationRule.head;
	    int sizeOfRulePart = 0;
	    
	    if(rulePart.equals(Constants.BODY))
	    {
	    	sizeOfRulePart = getNumberOfCommonGenes(rulePart,geneString, body);
	    }
	    else
	    	if(rulePart.equals(Constants.HEAD))
	    	{
	    		sizeOfRulePart = getNumberOfCommonGenes(rulePart,geneString,head);
	    	}
	    	else
	    		if(rulePart.equals(Constants.RULE))
	    		{
	    			body.addAll(head);
	    			sizeOfRulePart = getNumberOfCommonGenes(rulePart,geneString, body);
	    		}
		return sizeOfRulePart;
	    
	}

	private int getNumberOfCommonGenes(String rulePart, String queryGeneString, HashSet<String> actualRuleParts) {
		// TODO Auto-generated method stub
		String[] orig = null, target = null;
		if(queryGeneString.contains(", "))
		    orig = queryGeneString.split(", ");
		else 
			orig = new String[]{queryGeneString};
		
		 target = actualRuleParts.toArray(new String[actualRuleParts.size()]);
		 List origList = new ArrayList(Arrays.asList(orig));
		 List actList = Arrays.asList(target);
		 origList.retainAll(actList);
		    //System.out.println(origList); 
		return origList.size();
	}

	private String formatString(String rule) {
		// TODO Auto-generated method stub
		String bodyWithoutLeftBrace= rule.replace("(", "");
	    String bodyWithoutRightBrace = bodyWithoutLeftBrace.replace(")", "");
	    
		return bodyWithoutRightBrace;
	}

}
