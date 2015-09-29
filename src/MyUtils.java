import java.util.HashMap;

public class MyUtils{
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
}