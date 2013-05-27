package action;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class DisplayCheckActionInput extends MouseAdapter {
/*
 * action when checkBox of Display structures in Input is check : comboBox become visible
 */
	JTextArea area;
	JCheckBox checkDisp;
	JComboBox<String> combo;
	JButton ok;
	JRadioButton iupac;
	public HashMap<String,String> glycanInBox;
	HashMap<String,String> glycanAndId;
	public HashMap<String,String> uni;
	
	public DisplayCheckActionInput(JTextArea area, JCheckBox checkDisp, JComboBox<String> combo, JButton ok, JRadioButton iupac, HashMap<String,String> unicarbdb){
		this.area = area;
		this.checkDisp = checkDisp;
		this.combo = combo;
		this.ok = ok;
		this.iupac = iupac;
		this.uni = unicarbdb;
		glycanInBox = new HashMap<String,String>();
		glycanAndId = new HashMap<String,String>();
	}
	
	public void fillCombobox(){
		
			String[] sugar = area.getText().trim().split("\n\n");
			
			if (sugar.length == 1){
				System.out.println("1 sug : " + sugar[0]);
				if (iupac.isSelected())
					glycanInBox.put("0", "RES" + sugar[0].split("RES",2)[1]);
				else
					glycanInBox.put("0", sugar[0]);
				combo.addItem("Glycan 1");
			}
			else
			{
				combo.addItem("all");
				for (int i=0; i<sugar.length; i++){
					System.out.println("2 sug : "+ i +" : " +sugar[i]);
					if (iupac.isSelected())
						glycanInBox.put(String.valueOf(i+1), "RES" + sugar[i].split("RES",2)[1]);
					else
						glycanInBox.put(String.valueOf(i+1), sugar[i]);
					combo.addItem("Glycan " + (i+1));			
				}
			}
		}
	
	@SuppressWarnings("rawtypes")
	public void searchdb(){
		boolean f = false;
		Iterator itegly = glycanInBox.entrySet().iterator();		
		while (itegly.hasNext()){
			f = false;
			Map.Entry entry = (Map.Entry) itegly.next();
			String value = (String) entry.getValue();
			
			Iterator iterator = uni.entrySet().iterator();		
			while (iterator.hasNext()) {
				Map.Entry entry2 = (Map.Entry) iterator.next();
				String key = (String) entry2.getKey();
				String value2 = (String) entry2.getValue();
				if (value.trim().equals(value2.trim())){
					f = true;
					glycanAndId.put(value, key);
					break;
				}
			}
			if (!f)
				glycanAndId.put(value, "no_Unicarb_ID");
		}
	
		
	}
	
	  public void mouseClicked(MouseEvent e){
		fillCombobox();
		searchdb();
//		System.out.println(glycanInBox + "####" + "\nglyid : " + glycanAndId);
		if (checkDisp.isSelected()){
			combo.setVisible(true);
			ok.setVisible(true);	
		  }
		else{
			combo.removeAllItems();
			combo.setVisible(false);
			ok.setVisible(false);	
		}
	  }

	
	public HashMap<String, String> getGlycanAndId() {
			return glycanAndId;
		}
	  public HashMap<String, String> getGlycanInBoxInput() {
		  return glycanInBox;
	  }  

}