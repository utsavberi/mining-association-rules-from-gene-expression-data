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
	 * @param output output filename
	 */
	public static void extractFile(String input, String output){
		Scanner scan ;
		PrintWriter writer ;
		try {
			scan = new Scanner(new FileReader(input));
			String newLine = System.getProperty("line.separator");
			writer = new PrintWriter(output);
			
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
				
				String out = outLine.toString().substring(0,outLine.length()-1)+newLine;
				writer.write(out);
			}
			scan.close();
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Loads a file to an n x m matrix
	 * @param filename
	 * @param rows
	 * @param cols
	 * @return n x m matrix 
	 */
	public static String[][] fileToDataset(String filename, int rows, int cols){
		String[][] dataset = new String[rows][cols];
		Scanner scan;
		try{
			scan = new Scanner(new FileReader(filename));
			for(int i= 0; scan.hasNextLine(); i++){
				dataset[i] = scan.nextLine().split(",");
			}
			scan.close();
		}catch(FileNotFoundException ex){
			ex.printStackTrace();

		}
		
		return dataset;
	}
	
	public static void main(String[] args) {
	
		extractFile("association-rule-test-data.csv","input.csv");
		String [][] dataset = fileToDataset("input.csv",100,103);
		PrintStream out;
		try {
			double [] arr = {.50};//{.70,.60,.50,.40,.30};
			double minConfidence = .70;
			for(double t : arr){
//				out = new PrintStream(new FileOutputStream("out_frequentItemSets_"+((int)(t*100))+".txt"));
//				System.setOut(out);
				long startTime = System.currentTimeMillis();
				AssociationRuleMiner apriori = new AssociationRuleMiner(dataset,t);
				apriori.mine();
				ArrayList<AssociationRule> filtered = apriori.getAsociationRules(.70); 
				for(AssociationRule rule : filtered){
					if( ((rule.body.contains("Gene1_UP") || rule.body.contains("Gene10_Down"))||
							(rule.head.contains("Gene1_UP") || rule.head.contains("Gene10_Down"))))
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
