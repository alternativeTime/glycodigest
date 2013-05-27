package action;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

public class DisplayCheckActionOutput extends MouseAdapter {
	/*
	 * action when checkBox of Display structures in Output is check : comboBox become visible
	 */
	JTextArea area;
	JCheckBox checkDisp;
	JComboBox<String> combo;
	JButton ok;
	HashMap<String,String> glycanAndId;
	public HashMap<String,String> uni;
	HashMap<String,String> glycanInBox ;
	
	public DisplayCheckActionOutput(JTextArea area, JCheckBox checkDisp, JComboBox<String> combo, JButton ok, HashMap<String,String> uni){
		this.area = area;
		this.checkDisp = checkDisp;
		this.combo = combo;
		this.ok = ok;
		this.uni = uni;
		glycanAndId = new HashMap<String,String>();
		glycanInBox = new HashMap<String,String>();
	}

	
	public void fillCombobox(){

		//sugar : glycan all step
		String[] sugar = area.getText().split("GLYCAN");
		for (int i=1; i<sugar.length; i++){
			combo.addItem("Glycan " + String.valueOf(i));
			//subsugar : diff step
			String[] subsugar = sugar[i].trim().split("\n\n");
			
			int le = subsugar.length;
			for (int j=1; j<le; j++){
				String title = subsugar[j].split("\n")[0];
				String glyTemp = subsugar[j].split("RES",2)[1];
				int index = (le*(i-1) + j) ;
				System.out.println("index : "+index + " le : " + le + " i : "+i+" j : "+j);
				glycanInBox.put(String.valueOf(index),"RES" + glyTemp);
				if(title.contains(":"))
					combo.addItem("    "+i+"." + title.split(": ")[1]);
				else
					combo.addItem("    " +i+ "." +title);
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

	public HashMap<String, String> getGlycanInBoxOutput() {
			return glycanInBox;
		}
	public HashMap<String, String> getGlycanAndId() {
		return glycanAndId;
	}

}











