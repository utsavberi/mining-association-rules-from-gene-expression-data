import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class AssociationRuleMiner {

	String[][] dataset;
	int minSup;
	ArrayList<HashMap<String, Integer>> Ck = new ArrayList<HashMap<String, Integer>>();
	ArrayList<HashMap<String, Integer>> Lk = new ArrayList<HashMap<String, Integer>>();
	
	HashMap<String, Integer> c1, c2, c3, c4;
	HashMap<String, Integer> l1, l2, l3, l4;

	public AssociationRuleMiner(String[][] dataset, int minSup) {
		this.dataset = dataset;
		this.minSup = minSup;
		mine();
	}
	
	public HashMap<String,Integer> generateC1(){
		HashMap<String,Integer> c1 = new HashMap<String, Integer>();
		for (int i = 0; i < dataset.length; i++) {
			for (int j = 0; j < dataset[0].length; j++) {
				String item = dataset[i][j];
				c1.put(item, c1.get(item) == null ? 1 : c1.get(item) + 1);
			}
		}
		return c1;
	}
	
	public HashMap<String, Integer> generateL(HashMap<String, Integer> c) {
		HashMap<String, Integer> l = new HashMap<String, Integer>();
		for (String key : c.keySet()) {
			if (c.get(key) >= minSup) {
				l.put(key, c.get(key));
			}
		}
		return l;
	}
	
	public HashMap<String, Integer> generateC(HashMap<String, Integer> l) {
		HashMap<String, Integer> c2 = new HashMap<String, Integer>();
		for (String key : selfJoin(l.keySet())) {
			c2.put(key, countOccurrenceInDataset(key));
		}
		return c2;
	}
	
	public void mine() {
		
		MyUtils.println("printing l1");
		HashMap<String, Integer> c = generateC1();
		HashMap<String, Integer> l = generateL(c);
		Ck.add(c);
		Lk.add(l);
		MyUtils.printHashMap(l);
		
		int i = 2;
		while(l.size()>0){
			c = generateC(l);
			l = generateL(c);
			MyUtils.println("printing l"+i++);
			Ck.add(c);
			Lk.add(l);
			MyUtils.printHashMap(l);
		}
	}

	ArrayList<String> selfJoin(Set<String> keySet) {
		ArrayList<String> join = new ArrayList<String>();
		int k = 0;
		for (String key1 : keySet) {
			k = key1.split(",").length;
		}
		if (k == 1) {
			for (String key1 : keySet) {
				for (String key2 : keySet) {
					if (!key1.equals(key2)) {
						join.add(key1 + "," + key2);
					}
				}
			}
		} else {
			for (String key1 : keySet) {
				for (String key2 : keySet) {
					if (!key1.equals(key2)) {
						// MyUtils.println("checking keys "+key1+" >> "+key2);
						String[] key1Elems = key1.split(",");
						String[] key2Elems = key2.split(",");
						String joinedString = getJoinedStringIfExists(
								key1Elems, key2Elems);
						if (joinedString != null) {
							// MyUtils.println("joinable"+joinedString);
							join.add(joinedString);
						}
					}
				}
			}
		}

		return join;
	}

	private String getJoinedStringIfExists(String[] key1Elems,
			String[] key2Elems) {
		if (key1Elems.length != key2Elems.length)
			return null;
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (String str : key1Elems) {
			map.put(str, map.get(str) == null ? 1 : map.get(str) + 1);
		}
		for (String str : key2Elems) {
			map.put(str, map.get(str) == null ? 1 : map.get(str) + 1);
		}
		ArrayList<String> ones = new ArrayList<String>();
		ArrayList<String> twos = new ArrayList<String>();

		for (Entry<String, Integer> e : map.entrySet()) {
			if (e.getValue() == 2)
				twos.add(e.getKey());
			else if (e.getValue() == 1)
				ones.add(e.getKey());
			else
				return null;
		}
		if (ones.size() == 2) {
			String joined = "";
			for (String str : twos) {
				joined += str + ",";
			}
			joined += ones.get(0) + "," + ones.get(1);
			return joined;
		} else
			return null;
	}

	int countOccurrenceInDataset(String key) {
		String[] keyArr = key.split(",");
		int count = 0;
		for (int i = 0; i < dataset.length; i++) {
			if (MyUtils.isArrayInArray(keyArr, dataset[i]) == true) {
				count++;
			}
		}
		return count;
	}


}
