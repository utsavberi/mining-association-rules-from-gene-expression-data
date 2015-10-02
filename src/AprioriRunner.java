import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSpinner;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JList;


public class AprioriRunner {

	private JFrame frame;
	private JTextField filepathTxt;
	AssociationRuleMiner apriori;
	private JTextField searchRuleTxt;
	/**
	 * removes the row id, appends each item with Gene(i) and outputs to a file
	 * @param input input filename
	 * @param output output filename
	 */
	public static int[] extractFile(String input, String output){
		Scanner scan ;
		PrintWriter writer ;
		try {
			scan = new Scanner(new FileReader(input));
			String newLine = System.getProperty("line.separator");
			writer = new PrintWriter(output);
			int x = 0;
			int y = 0;
			//scan.nextLine();
			while(scan.hasNextLine()){
				String line = scan.nextLine();
				String []tokens = line.split(",");
				y = tokens.length;
				StringBuilder outLine = new StringBuilder();
				x++;
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
			int dim [] = {x,y+1};
			return dim;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
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
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AprioriRunner window = new AprioriRunner();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AprioriRunner() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 810, 506);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		filepathTxt = new JTextField();
		filepathTxt.setText("association-rule-test-data.csv");
		filepathTxt.setBounds(106, 37, 682, 19);
		frame.getContentPane().add(filepathTxt);
		filepathTxt.setColumns(10);
		
		JLabel lblFilepath = new JLabel("filepath");
		lblFilepath.setBounds(12, 39, 70, 15);
		frame.getContentPane().add(lblFilepath);
		
		
		
		JSpinner supportPercentSpinner = new JSpinner();
		supportPercentSpinner.setModel(new SpinnerNumberModel(50, 20, 100, 1));
		supportPercentSpinner.setBounds(106, 63, 50, 20);
		frame.getContentPane().add(supportPercentSpinner);
		
		JLabel lblSupport = new JLabel("Support (%)");
		lblSupport.setBounds(12, 66, 93, 15);
		frame.getContentPane().add(lblSupport);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(12, 12, 70, 15);
		frame.getContentPane().add(lblStatus);
		
		JLabel statusValLbl = new JLabel("ready");
		statusValLbl.setForeground(Color.GREEN);
		statusValLbl.setBounds(104, 12, 638, 15);
		frame.getContentPane().add(statusValLbl);
		
		supportPercentSpinner.setValue(50);
		
		
		
		JPanel rulePane = new JPanel();
		rulePane.setBounds(12, 113, 786, 326);
		frame.getContentPane().add(rulePane);
		GridBagLayout gbl_rulePane = new GridBagLayout();
		gbl_rulePane.columnWidths = new int[]{0, 106, 484, 0, 0};
		gbl_rulePane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_rulePane.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_rulePane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		rulePane.setLayout(gbl_rulePane);
		rulePane.hide();
		
		JLabel lblEnterRuleTemplate = new JLabel("Enter Rule Template");
		GridBagConstraints gbc_lblEnterRuleTemplate = new GridBagConstraints();
		gbc_lblEnterRuleTemplate.anchor = GridBagConstraints.WEST;
		gbc_lblEnterRuleTemplate.insets = new Insets(0, 0, 5, 5);
		gbc_lblEnterRuleTemplate.gridx = 1;
		gbc_lblEnterRuleTemplate.gridy = 0;
		rulePane.add(lblEnterRuleTemplate, gbc_lblEnterRuleTemplate);
		
		searchRuleTxt = new JTextField();
		GridBagConstraints gbc_searchRuleTxt = new GridBagConstraints();
		gbc_searchRuleTxt.gridwidth = 2;
		gbc_searchRuleTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchRuleTxt.insets = new Insets(0, 0, 5, 5);
		gbc_searchRuleTxt.gridx = 1;
		gbc_searchRuleTxt.gridy = 1;
		rulePane.add(searchRuleTxt, gbc_searchRuleTxt);
		searchRuleTxt.setColumns(10);
		
		JButton btnGo = new JButton("GO");
		GridBagConstraints gbc_btnGo = new GridBagConstraints();
		gbc_btnGo.insets = new Insets(0, 0, 5, 0);
		gbc_btnGo.gridx = 3;
		gbc_btnGo.gridy = 1;
		rulePane.add(btnGo, gbc_btnGo);
		
		JList outList = new JList();
		GridBagConstraints gbc_outList = new GridBagConstraints();
		gbc_outList.insets = new Insets(0, 0, 5, 0);
		gbc_outList.gridheight = 5;
		gbc_outList.gridwidth = 3;
		gbc_outList.fill = GridBagConstraints.BOTH;
		gbc_outList.gridx = 1;
		gbc_outList.gridy = 2;
		rulePane.add(outList, gbc_outList);
		
		JLabel lblConfidence = new JLabel("Confidence (%)");
		lblConfidence.setBounds(188, 66, 105, 15);
		frame.getContentPane().add(lblConfidence);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(70, null, 100, 1));
		spinner.setBounds(296, 63, 50, 20);
		frame.getContentPane().add(spinner);
		
		JButton btnLoadFileAnd = new JButton("Load file and Mine");
		btnLoadFileAnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusValLbl.setText("Mining");
				int [] dim = extractFile("association-rule-test-data.csv","input.csv");
				String [][] dataset = fileToDataset("input.csv",dim[0],dim[1]);
				double sup = (new Integer(supportPercentSpinner.getValue().toString()))/(double)100;
				apriori = new AssociationRuleMiner(dataset, sup);
				apriori.mine();
				String outFile = "UI_out_frequentItemSets_"+(sup)+".txt";
				try {
					System.setOut(new PrintStream(new FileOutputStream(outFile)));
					int i = 0 ;
					for(HashMap<HashSet<String>,Integer> str : apriori.getFrequentItemSets()){
						MyUtils.println("*****************Frequent item set "+i+"***************");
						for(Entry<HashSet<String>,Integer> entry : str.entrySet()){
							MyUtils.println(entry.toString());
							i++;
						}
						
					}
					rulePane.show();
					statusValLbl.setForeground(Color.GREEN);
					statusValLbl.setText("Total :"+i+" candidates. Output stored to "+outFile );
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLoadFileAnd.setBounds(376, 61, 187, 25);
		frame.getContentPane().add(btnLoadFileAnd);
	}
}
