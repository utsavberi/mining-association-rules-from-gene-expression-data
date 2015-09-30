import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class MyUtils{
	public static String ListJoin(LinkedList<String> arr,String seperator){
		String ret = "";
		for(String str : arr){
			ret += str+",";
		}
		return ret.substring(0, ret.length()-1);
	}
	public static void println(String msg){
		System.out.println(msg);
	}
	public static void print(String msg){
		System.out.print(msg);
	}
	public static void printMatrix(String[][] mat){
		for(int i=0; i< mat.length; i++){
			for(int j = 0; j< mat[0].length; j++){
				print(mat[i][j]+" ");
			}
			println("");
		}
	}
	public static void printHashMap(HashMap<HashSet<String>,Integer> map){
		for(HashSet<String> s : map.keySet()){
			System.out.println(s+" : "+map.get(s));
		}
	}
	public static boolean isStringInArray(String str, String[] arr){
		for(String chk : arr){
			if(str.equals(chk)){return true;}
		}
		
		return false;
	}
	public static boolean isArrayInArray(String[] arr1, String[] arr2){
		for(int i = 0; i< arr1.length; i++ ){
			if(isStringInArray(arr1[i],arr2)== false) return false; 
		}
		return true;
		
	}
	
	public static ArrayList<ArrayList<String>> findAllSubsets(List<String> originalSet){
		ArrayList<ArrayList<String>> sets = new ArrayList<ArrayList<String>>();
	    if (originalSet.isEmpty()) {
	    	sets.add(new ArrayList<String>());
	    	return sets;
	    }
	    String head = originalSet.get(0);
	    ArrayList<String> rest = new ArrayList<String>(originalSet.subList(1, originalSet.size())); 
	    for (ArrayList<String> set : findAllSubsets(rest)) {
	    	ArrayList<String> newSet = new ArrayList<String>();
	    	newSet.add(head);
	    	newSet.addAll(set);
	    	sets.add(newSet);
	    	sets.add(set);
	    }		
	    return sets;
	}
	public static HashSet<HashSet<String>> findAllnItemSubsets(HashSet<String> arr,int n){
		HashSet<HashSet<String>> ret = new HashSet<HashSet<String>>();
//		for(ArrayList<String> ar: findAllSubsets(Arrays.asList(arr))){
		for(HashSet<String> ar: powerSet(arr)){
			if(ar.size()==n)ret.add(ar);
		}
		return ret;
	}
	public static HashSet<HashSet<String>> powerSet(HashSet<String> originalSet) {
		HashSet<HashSet<String>> sets = new HashSet<HashSet<String>>();
	    if (originalSet.isEmpty()) {
	    	sets.add(new HashSet<String>());
	    	return sets;
	    }
	    List<String> list = new ArrayList<String>(originalSet);
	    String head = list.get(0);
	    HashSet<String> rest = new HashSet<String>(list.subList(1, list.size())); 
	    for (HashSet<String> set : powerSet(rest)) {
	    	HashSet<String> newSet = new HashSet<String>();
	    	newSet.add(head);
	    	newSet.addAll(set);
	    	sets.add(newSet);
	    	sets.add(set);
	    }		
	    return sets;
	}
}