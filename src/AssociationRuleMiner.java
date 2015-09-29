import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class AssociationRuleMiner {
	//TODO: instead of csv string use HashSet as key for all hashmaps
	private String[][] dataset;
	private int minSup;
	ArrayList<HashMap<String, Integer>> Ck = new ArrayList<HashMap<String, Integer>>();
	ArrayList<HashMap<String, Integer>> Lk = new ArrayList<HashMap<String, Integer>>();
	
	
	public AssociationRuleMiner(String[][] dataset, int minSup) {
		this.dataset = dataset;
		this.minSup = minSup;
	}
	public ArrayList<HashMap<String, Integer>> generateFrequentItemSet(){
//		MyUtils.println("printing l1");
		HashMap<String, Integer> c = generateC1();
		HashMap<String, Integer> l = generateL(c);
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
	public void mine() {		
		ArrayList<HashMap<String, Integer>>  frequentItemSets = generateFrequentItemSet();
		MyUtils.println(generateAssociationRules(frequentItemSets).toString());
	}
	
	private ArrayList<AssociationRule> generateAssociationRules(ArrayList<HashMap<String, Integer>> frequentItemSets) {
		ArrayList<AssociationRule> rules = new ArrayList<AssociationRule>();
			for(int i = 1; i < frequentItemSets.size(); i++){
				HashMap<String,Integer> currentK= frequentItemSets.get(i);
				for(String str: currentK.keySet()){
					String[] I = str.split(",");
					ArrayList<ArrayList<String>> subsets = MyUtils.findAllSubsets(Arrays.asList(I));
					for(ArrayList<String> subset: subsets){
						if(subset.size()==0){continue;}
						//s-> (I-s)
						//support_count(i) / support_count(s)
						int support_i = currentK.get(str);
						
						HashSet<String> s = new HashSet<String>(subset);
						HashSet<String> ii = new HashSet<String>(Arrays.asList(I));
						HashSet<String> head =  new HashSet<String>(ii);
						head.removeAll(s);
						int support_s = findSupportOf(s);
						if(head.size()>0){
							AssociationRule rule = new AssociationRule(s,head,((double) support_i)/support_s);
							rules.add(rule);
						}
					}
				}
			}
			return rules;
	}
	
	
	private int findSupportOf(HashSet<String> s) {
		HashMap<String, Integer> l = Lk.get(s.size()-1);
		for(String key:l.keySet()){
			String [] keyArr  = key.split(",");
			HashSet<String> x = new HashSet<String>(Arrays.asList(keyArr));
			if(x.equals(s)){
				return l.get(key);
			};
		}
		return 0;
	}
	private HashMap<String,Integer> generateC1(){
		HashMap<String,Integer> c1 = new HashMap<String, Integer>();
		for (int i = 0; i < dataset.length; i++) {
			for (int j = 0; j < dataset[0].length; j++) {
				String item = dataset[i][j];
				c1.put(item, c1.get(item) == null ? 1 : c1.get(item) + 1);
			}
		}
		return c1;
	}
	
	private HashMap<String, Integer> generateC(HashMap<String, Integer> l) {
		HashMap<String, Integer> c2 = new HashMap<String, Integer>();
		ArrayList<String> newCandidates = selfJoin(l.keySet());// prune(selfJoin(l.keySet()));
//		ArrayList<String> newCandidates  = prune(selfJoin(l.keySet()));
		prune(selfJoin(l.keySet()));
		for (String key : newCandidates) c2.put(key, computeSupport(key));
		return c2;
	}
	
	private HashMap<String, Integer> generateL(HashMap<String, Integer> c) {
		HashMap<String, Integer> l = new HashMap<String, Integer>();
		for (String key : c.keySet()) {
			if (c.get(key) >= minSup) {
				l.put(key, c.get(key));
			}
		}
		return l;
	}
		
	private boolean subsetItemExistsInPreviousK(ArrayList<String> subsetItem, Set<String> previousK){
		for(String previousKItem: previousK){
			String []subsetItemToArr =new String[subsetItem.size()];
			subsetItemToArr = subsetItem.toArray(subsetItemToArr);
			String []previousKitemtoSplitArr =previousKItem.split(",");
			if(MyUtils.isArrayInArray(subsetItemToArr, previousKitemtoSplitArr) ) return true;
		}
		return false;
	}
	private boolean allSubsetsExists(ArrayList<ArrayList<String>> subsets,Set<String> previousK){
		for(ArrayList<String> subsetItem : subsets){
			if(subsetItemExistsInPreviousK(subsetItem,previousK) == false) return false;
		}
		return true;
	}
	private ArrayList<String> prune(ArrayList<String> candidate){
		ArrayList<String> ret = new ArrayList<String>();
		for(int i = 0; i < candidate.size(); i++ ){
			String [] candItemSplit = candidate.get(i).split(",");
			ArrayList<ArrayList<String>> subsets = MyUtils.findAllnItemSubsets(candItemSplit,candItemSplit.length-1);
			if(allSubsetsExists(subsets,Lk.get(Lk.size()-1).keySet())){
				ret.add(candidate.get(i));
			}
		}
		return ret;
	}
		
	

	private ArrayList<String> selfJoin(Set<String> keySet) {
		ArrayList<String> join = new ArrayList<String>();
		int k = keySet.iterator().next().split(",").length;
		for (String key1 : keySet) {
			for (String key2 : keySet) {
				if (!key1.equals(key2)) {
					if(k==1) join.add(key1 + "," + key2);
					else{
						// MyUtils.println("checking keys "+key1+" >> "+key2);
						String[] key1Elems = key1.split(",");
						String[] key2Elems = key2.split(",");
						String joinedString = getJoinedStringIfExists(key1Elems, key2Elems);
						if (joinedString != null) join.add(joinedString);
					}
				}
			}
		}
		return join;
	}

	private String getJoinedStringIfExists(String[] key1Elems,String[] key2Elems) {
		if (key1Elems.length != key2Elems.length) return null;
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		LinkedList<String> ones = new LinkedList<String>(); 
		LinkedList<String> twos = new LinkedList<String>();

		for (String str : key1Elems) map.put(str, map.get(str) == null ? 1 : map.get(str) + 1);
		for (String str : key2Elems) map.put(str, map.get(str) == null ? 1 : map.get(str) + 1);
		
		for (Entry<String, Integer> e : map.entrySet()) {
			if (e.getValue() == 2) twos.add(e.getKey());
			else if (e.getValue() == 1) ones.add(e.getKey());
			else return null;
		}
		if (ones.size() == 2) {
			String joined = MyUtils.ListJoin(twos,",")+","+MyUtils.ListJoin(ones,",");
			return joined;
		} else return null;
	}

	int computeSupport(String key) {
		String[] keyArr = key.split(",");
		int count = 0;
		for (int i = 0; i < dataset.length; i++) {
			if (MyUtils.isArrayInArray(keyArr, dataset[i]) == true) count++;
		}
		return count;
	}
}
