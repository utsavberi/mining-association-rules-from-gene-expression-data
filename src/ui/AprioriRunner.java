package ui;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.ListModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.swing.JScrollPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import parser.AssociationRuleParser;
import ruleMiner.AssociationRule;
import ruleMiner.AssociationRuleMiner;
import ruleMiner.MyUtils;
import javax.swing.border.EtchedBorder;


public class AprioriRunner {

	private JFrame frame;
	private JTextField filepathTxt;
	AssociationRuleMiner apriori;
	private JTextField searchRuleTxt;

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

	JLabel statusValLbl;
	private JLabel lblEnterRuleTemplate;
	private JPanel rulePane;
	private JLabel lblFilepath;
	private JSpinner supportPercentSpinner;
	private JLabel lblSupport;
	private JLabel lblStatus;
	private JButton btnChooseFile;
	private JList<AssociationRule> outList;
	String [] testCases = {"RULE HAS ANY of (Gene1_UP)",
			"RULE HAS NONE of (Gene1_UP)",
			"RULE HAS 1 of (Gene1_UP, Gene10_DOWN)",
			"BODY HAS ANY of (Gene1_UP)",
			"BODY HAS NONE of (Gene1_UP)",
			"BODY HAS 1 of (Gene1_UP, Gene10_Down)",
			"HEAD HAS ANY of (Gene1_UP)",
			"HEAD HAS NONE of (Gene1_UP)",
			"HEAD HAS 1 of (Gene1_UP, Gene10_DOWN)",
			"SizeOf(RULE) >= 2"
			};
	private JSpinner confidenceSpinner;
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//testCases = {"lala ala","ieueoe"," kjhnk k"};
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 880, 506);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		filepathTxt = new JTextField();
		filepathTxt.setText("association-rule-test-data.csv");
		filepathTxt.setBounds(106, 37, 557, 19);
		frame.getContentPane().add(filepathTxt);
		filepathTxt.setColumns(10);
		
		lblFilepath = new JLabel("filepath");
		lblFilepath.setBounds(12, 39, 70, 15);
		frame.getContentPane().add(lblFilepath);
		
		
		
		supportPercentSpinner = new JSpinner();
		supportPercentSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				statusValLbl.setForeground(Color.BLUE);
				statusValLbl.setText("Click on 'Load file and Mine' button" );
				
			}
		});
		supportPercentSpinner.setModel(new SpinnerNumberModel(50, 20, 100, 1));
		supportPercentSpinner.setBounds(106, 63, 50, 20);
		frame.getContentPane().add(supportPercentSpinner);
		
		lblSupport = new JLabel("Support (%)");
		lblSupport.setBounds(12, 66, 93, 15);
		frame.getContentPane().add(lblSupport);
		
		lblStatus = new JLabel("Status:");
		lblStatus.setBounds(12, 12, 70, 15);
		frame.getContentPane().add(lblStatus);
		
		 statusValLbl = new JLabel("ready");
		statusValLbl.setForeground(new Color(0, 204, 0));
		statusValLbl.setBounds(104, 12, 638, 15);
		frame.getContentPane().add(statusValLbl);
		
		supportPercentSpinner.setValue(50);
		
		
		
		rulePane = new JPanel();
		rulePane.setBounds(12, 95, 837, 371);
		frame.getContentPane().add(rulePane);
		GridBagLayout gbl_rulePane = new GridBagLayout();
		gbl_rulePane.columnWidths = new int[]{0, 264, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 57, 202, 0};
		gbl_rulePane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_rulePane.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_rulePane.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		rulePane.setLayout(gbl_rulePane);
		rulePane.hide();
		
		JLabel lblTestCases = new JLabel("Run Test Case");
		GridBagConstraints gbc_lblTestCases = new GridBagConstraints();
		gbc_lblTestCases.anchor = GridBagConstraints.WEST;
		gbc_lblTestCases.insets = new Insets(0, 0, 5, 5);
		gbc_lblTestCases.gridx = 1;
		gbc_lblTestCases.gridy = 0;
		rulePane.add(lblTestCases, gbc_lblTestCases);
		
		JButton btn1 = new JButton("1");
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchRuleTxt.setText(testCases[0]);
				runRule();
				
			}
		});
		
		
		
		GridBagConstraints gbc_btn1 = new GridBagConstraints();
		gbc_btn1.insets = new Insets(0, 0, 5, 5);
		gbc_btn1.gridx = 2;
		gbc_btn1.gridy = 0;
		rulePane.add(btn1, gbc_btn1);
		
		JButton button_2 = new JButton("2");
		GridBagConstraints gbc_button_2 = new GridBagConstraints();
		gbc_button_2.insets = new Insets(0, 0, 5, 5);
		gbc_button_2.gridx = 3;
		gbc_button_2.gridy = 0;
		rulePane.add(button_2, gbc_button_2);
		
		JButton button_3 = new JButton("3");
		GridBagConstraints gbc_button_3 = new GridBagConstraints();
		gbc_button_3.insets = new Insets(0, 0, 5, 5);
		gbc_button_3.gridx = 4;
		gbc_button_3.gridy = 0;
		rulePane.add(button_3, gbc_button_3);
		
		JButton button_4 = new JButton("4");
		GridBagConstraints gbc_button_4 = new GridBagConstraints();
		gbc_button_4.insets = new Insets(0, 0, 5, 5);
		gbc_button_4.gridx = 5;
		gbc_button_4.gridy = 0;
		rulePane.add(button_4, gbc_button_4);
		
		JButton button_5 = new JButton("5");
		GridBagConstraints gbc_button_5 = new GridBagConstraints();
		gbc_button_5.insets = new Insets(0, 0, 5, 5);
		gbc_button_5.gridx = 6;
		gbc_button_5.gridy = 0;
		rulePane.add(button_5, gbc_button_5);
		
		JButton button_6 = new JButton("6");
		GridBagConstraints gbc_button_6 = new GridBagConstraints();
		gbc_button_6.insets = new Insets(0, 0, 5, 5);
		gbc_button_6.gridx = 7;
		gbc_button_6.gridy = 0;
		rulePane.add(button_6, gbc_button_6);
		
		JButton button_7 = new JButton("7");
		GridBagConstraints gbc_button_7 = new GridBagConstraints();
		gbc_button_7.insets = new Insets(0, 0, 5, 5);
		gbc_button_7.gridx = 8;
		gbc_button_7.gridy = 0;
		rulePane.add(button_7, gbc_button_7);
		
		JButton button_8 = new JButton("8");
		GridBagConstraints gbc_button_8 = new GridBagConstraints();
		gbc_button_8.insets = new Insets(0, 0, 5, 5);
		gbc_button_8.gridx = 9;
		gbc_button_8.gridy = 0;
		rulePane.add(button_8, gbc_button_8);
		
		JButton button_9 = new JButton("9");
		GridBagConstraints gbc_button_9 = new GridBagConstraints();
		gbc_button_9.insets = new Insets(0, 0, 5, 5);
		gbc_button_9.gridx = 10;
		gbc_button_9.gridy = 0;
		rulePane.add(button_9, gbc_button_9);
		
		JButton button_10 = new JButton("10");
		GridBagConstraints gbc_button_10 = new GridBagConstraints();
		gbc_button_10.insets = new Insets(0, 0, 5, 5);
		gbc_button_10.gridx = 11;
		gbc_button_10.gridy = 0;
		rulePane.add(button_10, gbc_button_10);
		
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchRuleTxt.setText(testCases[1]);
				runRule();
				
			}
		});
		
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchRuleTxt.setText(testCases[2]);
				runRule();
				
			}
		});
		
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchRuleTxt.setText(testCases[3]);
				runRule();
				
			}
		});
		
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchRuleTxt.setText(testCases[4]);
				runRule();
				
			}
		});
		
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchRuleTxt.setText(testCases[5]);
				runRule();
				
			}
		});
		
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchRuleTxt.setText(testCases[6]);
				runRule();
				
			}
		});
		
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchRuleTxt.setText(testCases[0]);
				runRule();
				
			}
		});
		
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchRuleTxt.setText(testCases[7]);
				runRule();
				
			}
		});
		
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchRuleTxt.setText(testCases[8]);
				runRule();
				
			}
		});
		
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchRuleTxt.setText(testCases[9]);
				runRule();
				
			}
		});
		
		
		
		
		JButton btnGo = new JButton("GO");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runRule();
			}
		});
		
		searchRuleTxt = new JTextField();
		searchRuleTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runRule();

			}
		});
		
		lblEnterRuleTemplate = new JLabel("Enter Rule Template");
		GridBagConstraints gbc_lblEnterRuleTemplate = new GridBagConstraints();
		gbc_lblEnterRuleTemplate.anchor = GridBagConstraints.WEST;
		gbc_lblEnterRuleTemplate.insets = new Insets(0, 0, 5, 5);
		gbc_lblEnterRuleTemplate.gridx = 1;
		gbc_lblEnterRuleTemplate.gridy = 1;
		rulePane.add(lblEnterRuleTemplate, gbc_lblEnterRuleTemplate);
		
		GridBagConstraints gbc_searchRuleTxt = new GridBagConstraints();
		gbc_searchRuleTxt.gridwidth = 10;
		gbc_searchRuleTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchRuleTxt.insets = new Insets(0, 0, 5, 5);
		gbc_searchRuleTxt.gridx = 2;
		gbc_searchRuleTxt.gridy = 1;
		rulePane.add(searchRuleTxt, gbc_searchRuleTxt);
		searchRuleTxt.setColumns(10);
		GridBagConstraints gbc_btnGo = new GridBagConstraints();
		gbc_btnGo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGo.insets = new Insets(0, 0, 5, 0);
		gbc_btnGo.gridx = 13;
		gbc_btnGo.gridy = 1;
		rulePane.add(btnGo, gbc_btnGo);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 6;
		gbc_scrollPane.gridwidth = 13;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 2;
		rulePane.add(scrollPane, gbc_scrollPane);
		
		outList = new JList<AssociationRule>();
		outList.setForeground(new Color(0, 0, 0));
		outList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		outList.setBackground(new Color(102, 153, 204));
		scrollPane.setViewportView(outList);
		outList.setValueIsAdjusting(true);
		
		JButton btnSaveToFile = new JButton("Save To File");
		btnSaveToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser saveRuleToFileDlg = new JFileChooser();
				if(saveRuleToFileDlg.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
					try {
						saveModelToFile(outList.getModel(),saveRuleToFileDlg.getSelectedFile());
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}

			private void saveModelToFile(ListModel<AssociationRule> model,
					File selectedFile) throws FileNotFoundException {
				System.setOut(new PrintStream(new FileOutputStream(selectedFile.getPath())));
				for(int i = 0; i< model.getSize(); i++){
					MyUtils.println(model.getElementAt(i).toString());
				}
				
			}
		});
		GridBagConstraints gbc_btnSaveToFile = new GridBagConstraints();
		gbc_btnSaveToFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSaveToFile.gridx = 13;
		gbc_btnSaveToFile.gridy = 8;
		rulePane.add(btnSaveToFile, gbc_btnSaveToFile);
		
		JLabel lblConfidence = new JLabel("Confidence (%)");
		lblConfidence.setBounds(188, 66, 105, 15);
		frame.getContentPane().add(lblConfidence);
		
		confidenceSpinner = new JSpinner();
		confidenceSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				
				double confidence = new Integer(confidenceSpinner.getValue().toString())/(double)100;;
				DefaultListModel <AssociationRule> model = new DefaultListModel<>();
				for(AssociationRule rule: apriori.getAsociationRules(confidence)){
					model.addElement(rule);
				}
				outList.setModel(model);

			}
		});
		confidenceSpinner.setModel(new SpinnerNumberModel(70, null, 100, 1));
		confidenceSpinner.setBounds(296, 63, 50, 20);
		frame.getContentPane().add(confidenceSpinner);
		
		JButton btnLoadFileAnd = new JButton("Load file and Mine");
		btnLoadFileAnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusValLbl.setText("Mining");
				String filename = filepathTxt.getText();
				ArrayList<String[]> l = extractFile(filename);
				double sup = (new Integer(supportPercentSpinner.getValue().toString()))/(double)100;
				apriori = new AssociationRuleMiner(l, sup);
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
					double confidence = new Integer(confidenceSpinner.getValue().toString())/(double)100;;
					DefaultListModel <AssociationRule> model = new DefaultListModel<>();
					for(AssociationRule rule: apriori.getAsociationRules(confidence)){
						model.addElement(rule);
					}
					
					outList.setModel(model);
					rulePane.show();
					
					statusValLbl.setForeground(new Color(0,204,0));
					statusValLbl.setText("Total :"+i+" candidates. Output stored to "+outFile );
					
				} catch (FileNotFoundException e1) {
					statusValLbl.setForeground(Color.RED);
					statusValLbl.setText(e1.getMessage() );
					
					e1.printStackTrace();
				}
				catch(Exception e1){
					statusValLbl.setForeground(Color.RED);
					statusValLbl.setText(e1.getMessage() );
					
					e1.printStackTrace();
				}
			}
		});
		
		btnLoadFileAnd.setBounds(376, 61, 187, 25);
		frame.getContentPane().add(btnLoadFileAnd);
		
		btnChooseFile = new JButton("Choose File");
		btnChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooseDataFile = new JFileChooser();
				if(chooseDataFile.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION){
					filepathTxt.setText(chooseDataFile.getSelectedFile().getPath());
				}
			}
		});
		btnChooseFile.setBounds(681, 34, 117, 25);
		frame.getContentPane().add(btnChooseFile);
	}
	
	void runRule(){
		AssociationRuleParser parser = new AssociationRuleParser();
	    ArrayList<AssociationRule> outputAssciationRules = new ArrayList<AssociationRule>();
		int resultCount = 0;
		double confidence = new Integer(confidenceSpinner.getValue().toString())/(double)100;;
	    for (AssociationRule associationRule:apriori.getAsociationRules(confidence))
		{
		
			if(parser.runParser(searchRuleTxt.getText(),associationRule))
			{
				resultCount++;
				outputAssciationRules.add(associationRule);
			}
		}
	    
	    DefaultListModel <AssociationRule> model = new DefaultListModel<>();
		for(AssociationRule rule: outputAssciationRules){
			model.addElement(rule);
		}
		
		outList.setModel(model);

		statusValLbl.setForeground(Color.BLUE);
		statusValLbl.setText("Total :"+model.getSize()+" rules." );
		
//		System.out.println(outputAssciationRules.toString());
	}
}
