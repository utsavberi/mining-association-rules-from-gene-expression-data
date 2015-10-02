import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * AssociationRuleMiner generates the frequent item sets and association 
 * rules from a dataset
 * 
 * @author utsav
 */

public class AssociationRuleMiner {
	private String[][] dataset;
	private int minSup;
	private ArrayList<HashMap<HashSet<String>, Integer>> Ck; 
	private ArrayList<HashMap<HashSet<String>, Integer>> Lk; 
	private int totalRows;
	
	private ArrayList<AssociationRule> associationRules ;
	private ArrayList<HashMap<HashSet<String>, Integer>>  frequentItemSets;
	
	/**
	 * @return an arrayList of the frequent candidate sets
	 */
	public  ArrayList<HashMap<HashSet<String>, Integer>> getFrequentItemSets(){
		return frequentItemSets;
	}
	
	/**
	 * Get the association rules filtered by confidence
	 * @param confidence the minimum confidence re4quired
	 * @return an array list of associationList
	 */
	public ArrayList<AssociationRule> getAsociationRules(double confidence){
		ArrayList<AssociationRule> filtered = new ArrayList<AssociationRule>();
		for(AssociationRule rule: associationRules){
			if(rule.confidence>=confidence){
				filtered.add(rule);
			}
		}
		return  filtered;
	}
	
	/**
	 * Get all the association rules
	 * @return an array list of associationList
	 */
	public ArrayList<AssociationRule> getAsociationRules(){
		return  associationRules;
	}
	
	/**
	 * @param dataset an n x n matrix with each cell containing an item. Do not include headers or row ids 
	 * @param minSup the minimum support eg .30 means 30% to be used
	 */
	public AssociationRuleMiner(String[][] dataset, double minSup) {
		Ck = new ArrayList<HashMap<HashSet<String>, Integer>>();
		Lk  = new ArrayList<HashMap<HashSet<String>, Integer>>();
		this.dataset = dataset;
		this.totalRows = dataset.length;
		this.minSup = (int) (Math.ceil(minSup*totalRows));
	}
	
	/**
	 * @param dataset an n x n matrix with each cell containing an item. Do not include headers or row ids 
	 * @param minSup the minimum support eg .30 means 30% to be used
	 */
	public AssociationRuleMiner(ArrayList<String[]> dataset, double minSup) {
		this.dataset = new String[dataset.size()][dataset.get(0).length];
		for( int i = 0; i< dataset.size(); i++)
			this.dataset[i] = dataset.get(i);
		Ck = new ArrayList<HashMap<HashSet<String>, Integer>>();
		Lk  = new ArrayList<HashMap<HashSet<String>, Integer>>();
		this.totalRows = dataset.size();
		this.minSup = (int) (Math.ceil(minSup*totalRows));
	}
	
		
	/**
	 * Mines association rules by first computing the frequentItemSets
	 * and then generating the rules
	 * @return an array list of AssociationRules
	 */
	public ArrayList<AssociationRule> mine() {		

		frequentItemSets = generateFrequentItemSet();
		associationRules = generateAssociationRules(frequentItemSets);
		return  associationRules;
	}

	/**
	 * Generate frequent item sets by iteratively computing the candidate sets
	 * @return Array List of the frequent item set generated from the dataset
	 */
	public ArrayList<HashMap<HashSet<String>, Integer>> generateFrequentItemSet(){
//		MyUtils.println("printing l1");
		HashMap<HashSet<String>, Integer> c = generateC1();
		HashMap<HashSet<String>, Integer> l = generateL(c);
		Ck.add(c);
		Lk.add(l);
//		MyUtils.printHashMap(l);
		int i = 2;
		while(l.size()>0){
			c = generateC(l);
			l = generateL(c);
//			MyUtils.println("printing l"+i++);
			Ck.add(c);
			Lk.add(l);
//			MyUtils.printHashMap(l);
		}
		return Lk;
	}
	
	/**
	 * Generates the association rule based on the frequent item sets
	 * @param frequentItemSets
	 * @return an array list of AssociationRules
	 */
	private ArrayList<AssociationRule> generateAssociationRules(ArrayList<HashMap<HashSet<String>, Integer>> frequentItemSets) {
		ArrayList<AssociationRule> rules = new ArrayList<AssociationRule>();
			for(int i = 1; i < frequentItemSets.size(); i++){
				HashMap<HashSet<String>,Integer> currentK= frequentItemSets.get(i);
				for(HashSet<String> str: currentK.keySet()){
					HashSet<String> I = str;
					HashSet<HashSet<String>> subsets = MyUtils.findAllSubsets(str);
					for(HashSet<String> subset: subsets){
						if(subset.size()==0){continue;}
						//s-> (I-s)
						//support_count(i) / support_count(s)
						int support_i = currentK.get(str);
						
						HashSet<String> s = subset;
						HashSet<String> ii = I;
						HashSet<String> head =  new HashSet<String>(ii);
						head.removeAll(s);
						int support_s = findSupportOf(s);
						if(head.size()>0){
							AssociationRule rule = new AssociationRule(s,head,((double) support_i)/support_s,((double) support_i)/totalRows);
							rules.add(rule);
						}
					}
				}
			}
			return rules;
	}
	
	/**
	 * @param Set of Items
	 * @return support value of the set in the dataset
	 */
	private int findSupportOf(HashSet<String> s) {
		HashMap<HashSet<String>, Integer> l = Lk.get(s.size()-1);
		for(HashSet<String> keyArr:l.keySet()){
			HashSet<String> x = keyArr;
			if(x.equals(s)){
				return l.get(keyArr);
			};
		}
		return 0;
	}
	
	/**
	 * @return HashMap with key as the set of items and value as support
	 */
	private HashMap<HashSet<String>,Integer> generateC1(){
		HashMap<HashSet<String>,Integer> c1 = new HashMap<HashSet<String>, Integer>();

		for (int i = 0; i < dataset.length; i++) {
			for (int j = 0; j < dataset[0].length; j++) {
				HashSet<String> item = new HashSet<String>();
				item.add(dataset[i][j]);
				c1.put(item, c1.get(item) == null ? 1 : c1.get(item) + 1);
			}
		}
		return c1;
	}
	
	/**
	 * @param l the Lk computed in the previous iteration
	 * @return HashMap with key as the set of items and value as support
	 */
	private HashMap<HashSet<String>, Integer> generateC(HashMap<HashSet<String>, Integer> l) {
		HashMap<HashSet<String>, Integer> c2 = new HashMap<HashSet<String>, Integer>();
		ArrayList<HashSet<String>> newCandidates = prune(selfJoin(l.keySet()));
		for (HashSet<String> key : newCandidates) c2.put(key, computeSupport(key));
		return c2;
	}
	
	/**
	 * @param c the Ck value generated in the previous iteration
	 * @return HashMap with key as the set of items and value as support
	 */
	private HashMap<HashSet<String>, Integer> generateL(final HashMap<HashSet<String>, Integer> c) {
		HashMap<HashSet<String>, Integer> l = new HashMap<HashSet<String>, Integer>();
		for (HashSet<String> key : c.keySet()) {
			if (c.get(key) >= minSup) {
				l.put(key, c.get(key));
			}
		}
		return l;
	}
		
	/**
	 * Applies the apriori algorithm to prune subtrees
	 * @param candidate ArrayList Set of candidates
	 * @return the pruned array list of candidates
	 */
	private ArrayList<HashSet<String>> prune(ArrayList<HashSet<String>> candidate){
		ArrayList<HashSet<String>> ret = new ArrayList<HashSet<String>>();
		for(int i = 0; i < candidate.size(); i++ ){
			HashSet<HashSet<String>> subsets = MyUtils.findAllnItemSubsets(candidate.get(i),candidate.get(i).size()-1);
			Set<HashSet<String>> prevLk = Lk.get(Lk.size()-1).keySet();
			if(prevLk.containsAll(subsets)){
				ret.add(candidate.get(i));
			}
		}
		return ret;
	}
	
	/**
	 * Joins a set with itself to create a new set
	 * @param keySet
	 * @return new joined set as ArrayList
	 */
	private ArrayList<HashSet<String>> selfJoin(Set<HashSet<String>> keySet) {
		ArrayList<HashSet<String>> join = new ArrayList<HashSet<String>>();
		int k = keySet.iterator().next().size();
		for (HashSet<String> key1 : keySet) {
			for (HashSet<String> key2 : keySet) {
				if (!key1.equals(key2)) {
					if(k==1) {
						HashSet<String> union = new HashSet<String>(key1);
						union.addAll(key2);
						join.add(union);
					}
					else{
						HashSet<String> joinedString = getJoinedStringIfExists(key1, key2);
						if (joinedString != null) join.add(joinedString);
					}
				}
			}
		}
		return join;
	}

	/**
	 * check if key1Elems and key2Elems can be joined and return the joined string
	 * @param key1Elems
	 * @param key2Elems
	 * @return joined String
	 */
	private HashSet<String> getJoinedStringIfExists(HashSet<String> key1Elems,HashSet<String> key2Elems) {
		if (key1Elems.size() != key2Elems.size()) return null;
	
			HashSet<String> joined = new HashSet<String>(key1Elems);
			joined.addAll(key2Elems);
			if(joined.size()==key1Elems.size()+1)
			{
				return joined;
			}
			else return null;
	}

	/**
	 * calculates the number of times the key occurs in the dataset
	 * @param key the item to search for
	 * @return support count (not percentage)
	 */
	int computeSupport(HashSet<String> key) {
		int count = 0;
		for (int i = 0; i < dataset.length; i++) {
			HashSet<String> tmp = new HashSet<String>(Arrays.asList(dataset[i]));
			if(tmp.containsAll(key)) count++;
		}
		return count;
	}
}
