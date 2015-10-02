import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Scanner;


public class Driver {

	

	/**
	 * removes the row id, appends each item with Gene(i) and outputs to a file
	 * @param input input filename
	 * @param output file converted to an array list
	 */
	public static ArrayList<String[]> extractFile(String input){
		Scanner scan ;
		ArrayList<String[]> lst = new ArrayList<String[]>();
		try {
			scan = new Scanner(new FileReader(input));
			while(scan.hasNextLine()){
				String line = scan.nextLine();
				String []tokens = line.split(",");
				StringBuilder outLine = new StringBuilder();
				for(int i = 1; i< tokens.length; i++){
					if(tokens[i].toLowerCase().startsWith("up") 
							|| tokens[i].toLowerCase().startsWith("down")){
						outLine.append("Gene"+i+"_"+tokens[i]+",");
					}
					else{
						outLine.append(tokens[i]+",");
					}
				}
				lst.add(outLine.toString().split(","));
			}
			scan.close();
			return lst;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static void main(String[] args) {
	
		extractFile("association-rule-test-data.csv");
		//String [][] dataset = fileToDataset("input.csv",100,103);
		PrintStream out;
		try {
			double [] arr = {.70,.60,.50};//,.40,.30};
			double minConfidence = .70;
			for(double t : arr){
				out = new PrintStream(new FileOutputStream("out_frequentItemSets_"+((int)(t*100))+".txt"));
				System.setOut(out);
				long startTime = System.currentTimeMillis();
				AssociationRuleMiner apriori = new AssociationRuleMiner(extractFile("association-rule-test-data.csv","input.csv"),t);
				apriori.mine();
				for(HashMap<HashSet<String>,Integer> str : apriori.getFrequentItemSets()){
					for(Entry<HashSet<String>,Integer> entry : str.entrySet()){
						MyUtils.println(entry.toString());
					}
					
				}
				out = new PrintStream(new FileOutputStream("out_associationRules_"+((int)(t*100))+".txt"));
				System.setOut(out);
				ArrayList<AssociationRule> filtered = apriori.getAsociationRules(); 
				for(AssociationRule rule : filtered){
//					if( ((rule.body.contains("Gene1_UP") || rule.body.contains("Gene10_Down"))||
//							(rule.head.contains("Gene1_UP") || rule.head.contains("Gene10_Down"))))
						MyUtils.println(rule+",");
				}
				long endTime   = System.currentTimeMillis();
				double totalTime = endTime - startTime;
				System.out.println("total time taken :"+totalTime+"millis"+ "="+totalTime/1000+"sec="+totalTime/60000+"min");}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 
		
	}

}
