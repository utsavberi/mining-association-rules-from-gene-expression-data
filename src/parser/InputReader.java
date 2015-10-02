package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class InputReader {

	/**
	 * @param path
	 * @return
	 */
	public List<String> getSampleData(String path)
	{
		BufferedReader br;
		List<String> associationRules=new ArrayList<String>();
		
		try {
			br = new BufferedReader(new FileReader(path));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();//+"";
			associationRules.add(line);
			while (line != null)
			{
				line = br.readLine();
				if(line!=null)
				{
					associationRules.add(line);
				}
			}   	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 finally 
		 {
		 }
		return associationRules;
		
	}
	
}
