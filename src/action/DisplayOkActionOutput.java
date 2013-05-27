package action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import main.GlycoCTtoImage;
import sugar_gui.StructureJPanel;

@SuppressWarnings("serial")
public class DisplayOkActionOutput extends AbstractAction {
	/*
	 * Action for button OK near the comboBox after the checkbox is checked, for Output. 
	 * Produce pictures for selected glycan.
	 */
	JTextArea area;
	JPanel jp;
	JComboBox<String> combo;
	ArrayList<String> name;
	static HashMap<String,String> nameRecorder;
	ArrayList<String> glycanToDisplay;
	HashMap<String,String> glycanInBox;
	HashMap<String,String> glycanAndId;
	ArrayList<String> enz;
	JList<String> list;
	ListModel<String> model1;
	int nbEnz;
	int count;

	public DisplayOkActionOutput (JTextArea area, JComboBox<String> combo, HashMap<String,String> glycanInBox, JList<String> list, HashMap<String,String> glycanAndId){
		this.area = area;
		this.combo = combo;
		this.glycanInBox = glycanInBox;
		this.glycanAndId = glycanAndId;
		this.list= list; 
		model1 = list.getModel();
		nameRecorder = new HashMap<String,String>();
		name = new ArrayList<String>();
		glycanToDisplay = new ArrayList<String>();
		enz = new ArrayList<String>();
		count = 0;
	}
	

	public int comboChoice (){
		nbEnz = model1.getSize()+2;
		enz.add("UND");
		for (int i=0; i<model1.getSize(); i++)
			enz.add(model1.getElementAt(i));
		System.out.println("enz : " + enz);
		int choice = combo.getSelectedIndex();

		if (choice%nbEnz == 0){
			for (int i=choice+1; i<choice+nbEnz; i++){
				glycanToDisplay.add(glycanInBox.get(String.valueOf(i)));	
			}
		}
		else{
			glycanToDisplay.add(glycanInBox.get(String.valueOf(choice)));
		}
		return choice;
	}

	public String randomName(){
		String ran = "";
		String alpha = "abcdefghijklmnopqrstuvwxyz";
		for (int i=0; i<3; i++){
			double num = Math.random()*26;
			ran = ran + alpha.substring((int)num,(int) num+1);	
		}
		System.out.println("ran : " + ran);
		return ran;
		
	}
	@SuppressWarnings("rawtypes")
	public void producePictures(){

		int choice = comboChoice();
		String choiceName = String.valueOf(choice/nbEnz);
		for (int i=0; i<glycanToDisplay.size(); i++){
			String nameTemp = "";
			String glycanM = glycanToDisplay.get(i);
			Iterator iterator = glycanAndId.entrySet().iterator();		
			while (iterator.hasNext()){
				
				Map.Entry entry = (Map.Entry) iterator.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				if (key.trim().equals(glycanM.trim())){
					if (value.equals("no_Unicarb_ID"))
						nameTemp = value + "_" + choiceName + "_" + i + randomName()+ "+" + enz.get(i)+ ".png";
					else
						nameTemp = value + "+" + enz.get(i) + ".png";
					break;
				}
			}
			GlycoCTtoImage glycotopict = new GlycoCTtoImage(glycanM, nameTemp);
			try {
				nameTemp = glycotopict.produceImage();
			}
			catch (Exception e1) {
				nameTemp = nameTemp + "noImage" + "+" + enz.get(i);
			}
			name.add(i, nameTemp);	
		}
	}
	
	public void produceJDialog(){
		JDialog dialog = new JDialog();
		dialog.setTitle("Glycan Structure - output");
		JScrollPane all = new JScrollPane();
		dialog.add(all);
		StructureJPanel struct = new StructureJPanel(name);
		dialog.setLocation(300, 0);
		all.setViewportView(struct);
		dialog.pack();
		dialog.setVisible(true);	

	}
	@Override


	public void actionPerformed(ActionEvent e) {
		glycanToDisplay.clear();
		name.clear();
		enz.clear();
		producePictures();
		produceJDialog();
	

		//TODO faire un truc du genre quand on ferme l'application principale : supprimer les fichiers image créés
	}



}
