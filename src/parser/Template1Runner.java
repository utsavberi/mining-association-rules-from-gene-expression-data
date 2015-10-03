package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ruleMiner.AssociationRule;
/**
 * @author Manpreet
 *
 */
public class Template1Runner implements TemplateRunner {

	/**
	 * runs the rule against template 1: {RULE|BODY|HEAD} HAS ({ANY|NUMBER|NONE}) OF (ITEM1, ITEM2, ..., ITEMn)

	 * @param inputQuery the given template
	 * @param associationRule generated through aprori
	 * @returns true if rule complies to template 1 
	 */
	@Override
	public boolean ruleMatchesTemplate(String inputQuery, AssociationRule associationRule) {
		// TODO Auto-generated method stub
		boolean rulecompliesToTemplate = false;
		int sizeOfRulePart;
		int thresholdValue;
		String rulePart = inputQuery.split("\\s+")[0];
		int startOfLeftBrace = inputQuery.indexOf('(');
		int startOfRightBrace = inputQuery.indexOf(')');
		String geneStringFromRule = inputQuery.substring(startOfLeftBrace + 1, startOfRightBrace);
		sizeOfRulePart = getTotalNumberOfItemSets(rulePart, geneStringFromRule, associationRule);
		if (inputQuery.contains(Constants.ANY)) 
		  {
			if (sizeOfRulePart > 0) 
			  {
				System.out.println(associationRule);
				rulecompliesToTemplate = true;
			  }
          } 
		 else if (inputQuery.contains(Constants.NONE)) 
		 {
			if (sizeOfRulePart == 0)
				rulecompliesToTemplate = true;
		 } 
		 else if (!inputQuery.contains(Constants.ANY) || !inputQuery.contains(Constants.NONE))
		 {
			thresholdValue = Integer.parseInt(inputQuery.split("\\s+")[2]);
		  	if (sizeOfRulePart == thresholdValue) 
		  	  {
				rulecompliesToTemplate = true;
			  }
		}
		return rulecompliesToTemplate;
	}

	private int getTotalNumberOfItemSets(String rulePart, String geneString, AssociationRule associationRule) {
		
		/**
		 * returns total number of itemsets in the given rulePart input
		 * 
		 * @param rulePart RULE|BODY|HEAD
		 * @param geneString the itemsets present in the template
		 * @param associationRule 
		 * @returns total number of itemsets in the rule based on rulePart 
		 */
		HashSet<String> body = associationRule.body;
		HashSet<String> head = associationRule.head;
		int sizeOfRulePart = 0;

		if (rulePart.equalsIgnoreCase(Constants.BODY))
		{
			sizeOfRulePart = getNumberOfCommonGenes(rulePart, geneString, new ArrayList<String>(body));
		} 
		else if (rulePart.equalsIgnoreCase(Constants.HEAD)) 
		{
			sizeOfRulePart = getNumberOfCommonGenes(rulePart, geneString, new ArrayList<String>(head));
		} 
		else if (rulePart.equalsIgnoreCase(Constants.RULE)) 
		{
			ArrayList<String> body_part = new ArrayList<String>(body);
			ArrayList<String> head_part = new ArrayList<String>(head);
			body_part.addAll(head_part);
			sizeOfRulePart = getNumberOfCommonGenes(rulePart, geneString, body_part);
		}
		return sizeOfRulePart;

	}
	/**
	 * matches the number of itemsets in the rule and template and fetches common itemsets size
	 * 
	 * @param rulePart RULE|BODY|HEAD
	 * @param geneString the itemsets present in the template
	 * @param actualRuleParts set containing all the itemsets in the target association rule 
	 * @returns total number of itemsets in the rule based on rulePart 
	 */
	private int getNumberOfCommonGenes(String rulePart, String queryGeneString, List<String> actualRuleParts) {
		int count = 0;
		String[] orig = null, target = null;
		if (queryGeneString.contains(", "))
			orig = queryGeneString.split(", ");
		else
			orig = new String[] { queryGeneString };
		List<String> origList = new ArrayList<String>(Arrays.asList(orig));
		
		HashMap<String, Integer> mapOfGeneAndCount = getMap(actualRuleParts);
		for(String queryGene: origList)
		{
			if(mapOfGeneAndCount.containsKey(queryGene))
			{
				count+=mapOfGeneAndCount.get(queryGene);
			}
				
		}
		return count;
	}

	private HashMap<String, Integer> getMap(List<String> actualRuleGenes) {
		// TODO Auto-generated method stub
		ArrayList<String> genesInRule = (ArrayList<String>) actualRuleGenes;
		HashMap<String,Integer> hashMap = new HashMap<String, Integer>();
		int count;
		for(String gene: genesInRule)
		{
			if(hashMap.containsKey(gene))
				{count = hashMap.get(gene);
			    count++;
			    hashMap.put(gene, count);
				}
			else
				hashMap.put(gene, 1);
		}
		return hashMap;
	}

	private String formatString(String rule) {
		
		String bodyWithoutLeftBrace = rule.replace("(", "");
		String bodyWithoutRightBrace = bodyWithoutLeftBrace.replace(")", "");

		return bodyWithoutRightBrace;
	}

}