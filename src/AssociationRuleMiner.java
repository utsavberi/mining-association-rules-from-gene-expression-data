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
		c1 = new HashMap<String, Integer>();
		l1 = new HashMap<String, Integer>();
		c2 = new HashMap<String, Integer>();
		l2 = new HashMap<String, Integer>();
		c3 = new HashMap<String, Integer>();
		l3 = new HashMap<String, Integer>();
		c4 = new HashMap<String, Integer>();
		l4 = new HashMap<String, Integer>();
		mine();
		// MyUtils.printMatrix(dataset);
	}

	public void generateL(HashMap<String, Integer> c, HashMap<String, Integer> l,
			int minSup) {
		for (String key : c.keySet()) {
			if (c.get(key) >= minSup) {
				l.put(key, c.get(key));
			}
		}
	}

	public void mine() {
		for (int i = 0; i < dataset.length; i++) {
			for (int j = 0; j < dataset[0].length; j++) {
				String item = dataset[i][j];
				c1.put(item, c1.get(item) == null ? 1 : c1.get(item) + 1);
			}
		}
		generateL(c1, l1, minSup);
		// printHashMap(c1);
		MyUtils.printHashMap(l1);
		MyUtils.println("c2----");

		c2 = generateC2(l1);
		generateL(c2, l2, minSup);
		// printHashMap(c2);
		MyUtils.printHashMap(l2);

		MyUtils.println("c3---");
		c3 = generateC3(l2);
		generateL(c3, l3, minSup);
		MyUtils.printHashMap(l3);

		MyUtils.println("c4---");
		c4 = generateC4(l3);
		generateL(c4, l4, minSup);
		// printHashMap(c4);
		MyUtils.println("c4 l");
		MyUtils.printHashMap(l4);

	}

	ArrayList<String> selfJoin(Set<String> keySet) {
		ArrayList<String> join = new ArrayList<String>();// [c.size()*c.size()];
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

	public HashMap<String, Integer> generateC2(HashMap<String, Integer> c) {
		HashMap<String, Integer> c2 = new HashMap<String, Integer>();
		for (String key : selfJoin(c.keySet())) {
			c2.put(key, countOccurrenceInDataset(key));
		}
		return c2;
	}

	public HashMap<String, Integer> generateC3(HashMap<String, Integer> c) {
		HashMap<String, Integer> c3 = new HashMap<String, Integer>();
		for (String key : selfJoin(c.keySet())) {
			c3.put(key, countOccurrenceInDataset(key));
		}
		return c3;
	}

	public HashMap<String, Integer> generateC4(HashMap<String, Integer> c) {
		HashMap<String, Integer> c4 = new HashMap<String, Integer>();
		for (String key : selfJoin(c.keySet())) {
			c4.put(key, countOccurrenceInDataset(key));
		}
		return c4;
	}

}
