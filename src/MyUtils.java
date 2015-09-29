import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
	public static void printHashMap(HashMap<String,Integer> map){
		for(String s : map.keySet()){
			System.out.println("{"+s+"} : "+map.get(s));
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
	public static ArrayList<ArrayList<String>> findAllnItemSubsets(String[] arr,int n){
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		for(ArrayList<String> ar: findAllSubsets(Arrays.asList(arr))){
			if(ar.size()==n)ret.add(ar);
		}
		return ret;
	}
}