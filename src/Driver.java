import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashSet;
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
			out = new PrintStream(new FileOutputStream("frequent30wsetsNprune.txt"));
			System.setOut(out);
			long startTime = System.currentTimeMillis();
			AssociationRuleMiner apriori = new AssociationRuleMiner(dataset,.30);
			apriori.mine();
//			for(AssociationRule rule :new HashSet<AssociationRule>( apriori.mine())){
//				if(rule.confidence>=.70){ 
////						if(rule.body.contains("Gene1_UP")|| rule.body.contains("Gene10_DOWN") 
////						|| rule.head.contains("Gene1_UP")|| rule.head.contains("Gene10_DOWN"))
//					MyUtils.println(rule.toString());
//					}
//			};
			//RULE HAS 1 OF ['Gene1_UP', 'Gene10_Down']
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println("total time taken"+totalTime);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 
		
	}

}
