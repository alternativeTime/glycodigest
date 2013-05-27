package sugar_gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.FileNotFoundException;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import main.UnicarbDico;
import action.ClearAction;
import action.DCEnzList;
import action.DisplayCheckActionInput;
import action.DisplayCheckActionOutput;
import action.DisplayOkActionInput;
import action.DisplayOkActionOutput;
import action.DownloadResultsAction;
import action.EnzAll;
import action.IdLoadingAction;
import action.OpenFileAction;
import action.ReferencingUnicarbAction;
import action.SubmitAction;
import action.TranslationAction;

@SuppressWarnings("serial")
public class SugarJPanel extends JPanel{
	/*
	 * Main panel containing all component
	 */
	HashMap<String,String> unicarbdb;
	public SugarJPanel() throws FileNotFoundException{
		MigLayout miglay = new MigLayout ("align 50%", "[]related*4[]related*3[]related*3[]related*4","[][]related*5[][][][][]related*5[][][][][][][][]related*2[][]");
		setLayout(miglay);
		this.setBackground(new Color(240,240,240));
		unicarbdb = new HashMap<String,String>();
		UnicarbDico un = new UnicarbDico();
		unicarbdb = un.getUniCarb();
		init();
		

	}

	public JLabel create_jlabel (String text, int i){
		JLabel j = new JLabel();
		j.setText(text);
		if (i == 1){
			j.setFont(new Font("Cambria", Font.BOLD, 16));
			j.setForeground(new Color(44,117,255));
		}
		else if (i == 2)
			j.setFont(new Font("Cambria", Font.BOLD, 14));		
		else if (i == 3)
			j.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		return j;
	}
	public JSeparator create_separator (int i){
		JSeparator js = new JSeparator();
		if (i==1)
			js.setForeground(Color.DARK_GRAY);
		if (i==2)
			js.setForeground(new Color(192,192,192));
		return js;
	}
	public JLabel create_image(String im){
		JLabel jl = new JLabel (new ImageIcon(getClass().getResource(im)));
		return jl;
	}
	public JComboBox<String> create_combobox(){
		JComboBox<String> combo = new JComboBox<String>();
		combo.setVisible(false);
		return combo;
	}
	public JCheckBox create_checkbox (String name, String i){
		JCheckBox check = new JCheckBox();
		String text = name;
		check.setText(text);
		if (i.equals("d"))
			check.setFont(new Font("Cambria", Font.BOLD, 14));	
		else if (i.equals("u"))
			check.setFont(new Font("Times New Roman", Font.LAYOUT_LEFT_TO_RIGHT, 14));
		return check;
	}
	public JButton createSimpleButton(String name, boolean visible){
			JButton j = new JButton(name);
			j.setForeground(Color.black);
			j.setVisible(visible);
			j.setMargin(new Insets(0,0,0,0));
			return j;
	}
	public JButton create_Jbutton(String name, AbstractAction action, boolean visible){

			JButton jb;	
			jb = new JButton(action);
			jb.setText(name);
			if (name.equals("Submit")){
				jb.setFont(new Font("Cambria", Font.BOLD+Font.ITALIC, 16));
				jb.setForeground(new Color(187, 210, 225));
				jb.setBackground(new Color (3, 34, 76));
			}

			else{
				jb.setMargin(new Insets(0,0,0,0));
				jb.setFont(new Font("Times New Roman", Font.LAYOUT_LEFT_TO_RIGHT, 14));
				jb.setForeground(Color.black);
			}
			if (!visible)
				jb.setVisible(false);
			return jb;
		

	}
	public void createRadioButton (JRadioButton GlycoCT, JRadioButton iupac, ButtonGroup bgroup, JTextArea areaInput){
		TranslationAction translate = new TranslationAction(areaInput, this);
		iupac.addMouseListener(translate);
		bgroup.add(GlycoCT);
		bgroup.add(iupac);
	}

	public void createJList(DefaultListModel<String> listModel, JList<String> listEnz){
		String[] enz_list = {"JBM", "BTG", "SPG", "CBG", "BKF", "AMF", "XMF", "GUH", "JBH", "ABS", "NAN1"};
		for (int i=0; i<enz_list.length; i++)
			listModel.addElement(enz_list[i]);
	}
	
	
	@SuppressWarnings("static-access")
	public void init(){
		//TODO z-traiter le help (mais comment?)
//		HelpJLabel help = new HelpJLabel();
		
		/*declaration*/		

		JLabel pictureTitle = create_image("/pictures/title.png");
		HelpEnzyme infoEnz = new HelpEnzyme();
		JLabel blank = create_image("/pictures/blank.png");
		JLabel labelStep1 = create_jlabel ("1. Load glycan sequence(s)", 2);
		JLabel labelInput1 = create_jlabel("- enter sequence(s) and choose the format", 3);
		JLabel labelInput2 = create_jlabel ("- or load a file containing sequence(s)", 3);
		JLabel labelInput3 = create_jlabel ("- or enter a UniCarb id", 3);
		JLabel labelStep2 = create_jlabel("2. Choose enzyme(s) to digest and SUBMIT", 2);
		JLabel labelStep3 = create_jlabel ("3. Simulation results", 2);
		JLabel labelRef = new JLabel();
		
		JSeparator sepTitle = create_separator(1);
		JSeparator sepStep1 = create_separator(2);
		JSeparator sepStep2 = create_separator(2);
		JSeparator sepStep3 = create_separator(2);
		JSeparator sepDispInput = create_separator(2);
		JSeparator sepDispOutput = create_separator(2);
		
		JTextArea areaInput = new JTextArea (18,27);
		JTextArea areaOutput = new JTextArea (18,27);
		
		JTextField Fieldfile = new JTextField(19);
		JTextField FieldId = new JTextField (19);
		
		DefaultListModel<String> listModel1 = new DefaultListModel<String>();
		DefaultListModel<String> listModel2 = new DefaultListModel<String>();
		JList<String> listEnz = new JList<String>(listModel1);
		JList<String> listEnzSelect = new JList<String>(listModel2);
		listEnz.addMouseListener(new DCEnzList(listEnz, listModel1, listModel2));
		createJList(listModel1, listEnz);
	
		EnzButtonActionIn buttEnzin = new EnzButtonActionIn(listEnz, listModel1, listModel2);
		EnzButtonActionOut buttEnzout = new EnzButtonActionOut(listEnzSelect, listModel1, listModel2);
		JCheckBox buttEnzAll =  create_checkbox("All", "u");
		EnzAll allEnzymeAction = new EnzAll(listEnz, listModel1, listModel2, buttEnzAll);
		buttEnzAll.addMouseListener(allEnzymeAction);
		
		JScrollPane scrollpaneInput = new JScrollPane (areaInput);
		scrollpaneInput.setHorizontalScrollBarPolicy(scrollpaneInput.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane scrollpaneOutput = new JScrollPane(areaOutput);
		scrollpaneOutput.setHorizontalScrollBarPolicy(scrollpaneOutput.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane scrollForEnz = new JScrollPane(listEnz);
		JScrollPane scrollForEnzSelect = new JScrollPane(listEnzSelect);
		scrollForEnz.setPreferredSize(new Dimension(150,170));	
		scrollForEnzSelect.setPreferredSize(new Dimension(150,170));
		
		JComboBox<String> comboInput = create_combobox();
		JComboBox<String> comboOutput = create_combobox();
		JCheckBox check_unk_link = create_checkbox("accepted unknown linkages","u");
	
		JRadioButton glycoCT = new JRadioButton("GlycoCT");
		glycoCT.setFont((new Font("Cambria", Font.BOLD, 13)));
		JRadioButton iupac = new JRadioButton("IUPAC");
		iupac.setFont((new Font("Cambria", Font.BOLD, 13)));
		ButtonGroup bgroup = new ButtonGroup();
		createRadioButton(glycoCT, iupac, bgroup, areaInput);
		
		JButton buttFile = create_Jbutton ("...",new OpenFileAction(this, Fieldfile, areaInput), true);
		JButton buttId = create_Jbutton("OK", new IdLoadingAction(areaInput, FieldId, unicarbdb), true);
		SubmitAction sub = new SubmitAction(this,areaInput, areaOutput, listEnzSelect, check_unk_link, iupac);
		JButton submit = create_Jbutton("Submit",sub, true);
		JButton download = create_Jbutton ("Download raw results", new DownloadResultsAction(areaOutput), true);
		JButton referencing1 = createSimpleButton("Matching Unicarb DB", true);
		ReferencingUnicarbAction ref = new ReferencingUnicarbAction(areaOutput, unicarbdb, labelRef, referencing1);
		referencing1.setAction(ref);
		referencing1.setText("Matching Unicarb DB");
		referencing1.setFont(new Font("Times New Roman", Font.LAYOUT_LEFT_TO_RIGHT, 14));
		JButton okDispInput = createSimpleButton("OK", false);
		JButton okDispOutput = createSimpleButton("OK", false);
		
		
		JCheckBox checkDispInput = create_checkbox("Display glycan structures", "d");
		JCheckBox checkDispOutput = create_checkbox("Display glycan structures","d");
		DisplayCheckActionInput checkAction = new DisplayCheckActionInput(areaInput, checkDispInput, comboInput, okDispInput, iupac, unicarbdb);
		DisplayCheckActionOutput checkAction2 = new DisplayCheckActionOutput(areaOutput, checkDispOutput, comboOutput, okDispOutput, unicarbdb);
		checkDispInput.addMouseListener(checkAction);
		checkDispOutput.addMouseListener(checkAction2);

		okDispInput.setAction(new DisplayOkActionInput(areaInput, comboInput,checkAction.getGlycanInBoxInput(), checkAction.getGlycanAndId()));
		okDispInput.setText("OK");
		okDispOutput.setAction(new DisplayOkActionOutput(areaOutput, comboOutput, checkAction2.getGlycanInBoxOutput(), listEnzSelect, checkAction2.getGlycanAndId()));
		okDispOutput.setText("OK");
		
		JButton clearinput_button = create_Jbutton(" Clear ", new ClearAction(areaInput, FieldId, Fieldfile, listModel1, listModel2, areaOutput, 
				check_unk_link, okDispInput, okDispOutput, checkDispInput, checkDispOutput, comboInput, comboOutput, bgroup, referencing1, labelRef, buttEnzAll), true);
		

		
		/*Addition*/
		add(pictureTitle, "spanx 5, growx");
		add(sepTitle, "newline, spanx 5 , growx ");
		
		add(labelStep1, "newline");
		add (labelStep2, " spanx 3");
		add (labelStep3);
		
		add(sepStep1, "newline, growx");
		add(sepStep2, "growx, spanx 3");
		add(sepStep3, "growx");
		
		add(labelInput1, "newline related *2, grow");
		
		add(scrollpaneInput, "newline, spany 5");	
		add(scrollForEnz, "top");
		add(infoEnz, "split 4, flowy, top, center");
		add(blank);
		add(buttEnzin);
		add(buttEnzout);
		add (scrollForEnzSelect, "top");

		add(scrollpaneOutput, "spany 8,grow, wrap");
	
		add(buttEnzAll, "wrap");
		add(check_unk_link,"spanx 3, bottom, growx, split 2");
		add(clearinput_button, "right, wrap");
		add(submit, "spanx 3, center, growx, wrap");

		add(labelInput2, "newline related *2");
		
		add(Fieldfile, "newline, split 2, growx");
		add(buttFile, "sizegroup 2, right");

		add(glycoCT, "newline, split 2, center");
		add(iupac, "center");
		
		add(labelInput3, "newline, growx");
		add(referencing1,"skip 3, split 2, growx");
		add(download, "right, growx");
		
		add(FieldId, "newline , split 2, growx");
		add(buttId, "right, sizegroup 2 ");	
		add(labelRef, "skip 3");


		add(checkDispInput, "newline");
		add(checkDispOutput, "skip 3");
		
		add(sepDispInput, "newline, grow");
		add(sepDispOutput, "skip 3, grow");
		
		add(comboInput, "newline, growx, split 2");
		add(okDispInput, "right");
		add(comboOutput, "skip 3, growx, split 2");
		add(okDispOutput, "right");


	}


}