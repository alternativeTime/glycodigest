package action;


import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import main.GlycoCTtoImage;
import sugar_gui.StructureJPanel;

@SuppressWarnings("serial")
public class DisplayOkActionInput extends AbstractAction {
/*
 * Action for button OK near the comboBox after the checkbox is checked, for Input. 
 * Produce pictures for selected glycan.
 */
	JTextArea area;
	JPanel jp;
	JComboBox<String> combo;
	ArrayList<String> name;
	HashMap<String,String> glycanInBox;
	ArrayList<String> glycanToDisplay;
	HashMap<String,String> glycanAndId;
	int count;
//	JFrame applet;
	
	public DisplayOkActionInput (JTextArea area, JComboBox<String> combo, HashMap<String,String> glycanInBox, HashMap<String,String> glycanAndId){
		this.area = area;
		this.combo = combo;
		name = new ArrayList<String>();
		this.glycanInBox = glycanInBox;
		this.glycanAndId = glycanAndId;
		glycanToDisplay = new ArrayList<String>();
		count = 0;
	}
	
	public int comboChoice(){
		int choice = combo.getSelectedIndex();
		//if there is only 1 sequence
		if (combo.getItemCount() == 1){
			glycanToDisplay.add(glycanInBox.get("0"));
		}
		else{
			//if there are more than 1 sequence
			if (choice == 0)
				for (int i=0; i<glycanInBox.size(); i++)
					glycanToDisplay.add(glycanInBox.get(String.valueOf(i+1)));
				
			else{
				glycanToDisplay.add(glycanInBox.get(String.valueOf(choice)));
			}	
		}
		return choice;
	}
	
	@SuppressWarnings("rawtypes")
	private void producePictures(){
		glycanToDisplay.clear();
		name.clear();
		comboChoice();
		System.out.println("glycantodisplay : "+ glycanToDisplay);
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
						nameTemp = value + "_" + i +"_Input.png";
					else
						nameTemp = value + ".png";
					break;
				}
			}		
			GlycoCTtoImage glycotopict = new GlycoCTtoImage(glycanM, nameTemp);
			try {
				nameTemp = glycotopict.produceImage();
			}
			catch (Exception e1) {
				nameTemp = nameTemp + "noImage";
			}
			name.add(i, nameTemp);	
		}
	}
		
	private void produceJDialog() {
		JDialog dialog = new JDialog();
		dialog.setTitle("Glycan Structure - input");
		JScrollPane all = new JScrollPane();
		dialog.add(all);
		all.setViewportView(new StructureJPanel(name));
		dialog.pack();
		dialog.setVisible(true);	
	}

	
	public void actionPerformed(ActionEvent e) {
		producePictures();
		produceJDialog();	

	
	}

	



}
