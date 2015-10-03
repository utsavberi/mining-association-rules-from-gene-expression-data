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
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 810, 506);
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
		rulePane.setBounds(12, 95, 786, 371);
		frame.getContentPane().add(rulePane);
		GridBagLayout gbl_rulePane = new GridBagLayout();
		gbl_rulePane.columnWidths = new int[]{0, 106, 484, 0, 0};
		gbl_rulePane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_rulePane.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_rulePane.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		rulePane.setLayout(gbl_rulePane);
		rulePane.hide();
		
		lblEnterRuleTemplate = new JLabel("Enter Rule Template");
		GridBagConstraints gbc_lblEnterRuleTemplate = new GridBagConstraints();
		gbc_lblEnterRuleTemplate.anchor = GridBagConstraints.WEST;
		gbc_lblEnterRuleTemplate.insets = new Insets(0, 0, 5, 5);
		gbc_lblEnterRuleTemplate.gridx = 1;
		gbc_lblEnterRuleTemplate.gridy = 0;
		rulePane.add(lblEnterRuleTemplate, gbc_lblEnterRuleTemplate);
		
		searchRuleTxt = new JTextField();
		searchRuleTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runRule();

			}
		});
		GridBagConstraints gbc_searchRuleTxt = new GridBagConstraints();
		gbc_searchRuleTxt.gridwidth = 2;
		gbc_searchRuleTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchRuleTxt.insets = new Insets(0, 0, 5, 5);
		gbc_searchRuleTxt.gridx = 1;
		gbc_searchRuleTxt.gridy = 1;
		rulePane.add(searchRuleTxt, gbc_searchRuleTxt);
		searchRuleTxt.setColumns(10);
		
		JButton btnGo = new JButton("GO");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runRule();
			}
		});
		GridBagConstraints gbc_btnGo = new GridBagConstraints();
		gbc_btnGo.insets = new Insets(0, 0, 5, 0);
		gbc_btnGo.gridx = 3;
		gbc_btnGo.gridy = 1;
		rulePane.add(btnGo, gbc_btnGo);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 6;
		gbc_scrollPane.gridwidth = 3;
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
		gbc_btnSaveToFile.gridx = 3;
		gbc_btnSaveToFile.gridy = 8;
		rulePane.add(btnSaveToFile, gbc_btnSaveToFile);
		
		JLabel lblConfidence = new JLabel("Confidence (%)");
		lblConfidence.setBounds(188, 66, 105, 15);
		frame.getContentPane().add(lblConfidence);
		
		JSpinner confidenceSpinner = new JSpinner();
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
	    for (AssociationRule associationRule:apriori.getAsociationRules())
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
