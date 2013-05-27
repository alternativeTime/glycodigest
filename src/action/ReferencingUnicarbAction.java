package action;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class ReferencingUnicarbAction extends AbstractAction {
/*
 * compare digested sequences with hashmap containing all unicarb sequence, and add Unicarb id if there are.
 */
	JTextArea output;
	int n;
	String id, seq;
	HashMap <String,String> uni;
	ArrayList<String> glycanInBox;
	ArrayList<String> uniId;
	HashMap<String,String> glycanAndId;
	JLabel labelRef;
	JButton but;
	

	public ReferencingUnicarbAction(JTextArea output, HashMap<String,String> uni, JLabel labelRef, JButton but){
		id = "";
		seq = "";
		this.uni = uni;
		this.output = output;
		this.labelRef = labelRef;
		this.but = but;
		glycanInBox = new ArrayList<String>();
		uniId = new ArrayList<String>();
		glycanAndId = new HashMap<String,String>();
	}


	
	@SuppressWarnings("rawtypes")
	public String search(){
		n = 0;
		boolean f = false;
		String all = "";
		String[] outputSplit = output.getText().split("\n\n");
		for (int i=0; i<outputSplit.length; i++){
			f = false;
			if (outputSplit[i].contains("GLYCAN"))
				all = all+outputSplit[i] + "\n\n";
			else{
				String[] glyAndTitle = outputSplit[i].split("RES",2);
				String title = glyAndTitle[0];
				String seq = "RES" + glyAndTitle[1];
				all = all + title;
				
				Iterator iterator = uni.entrySet().iterator();		
				while (iterator.hasNext()) {
					Map.Entry entry = (Map.Entry) iterator.next();
					String key = (String) entry.getKey();
					String value = (String) entry.getValue();
					if (value.trim().equals(seq.trim())){
						f = true;
						if (!glycanAndId.containsValue(key))
							n +=1;
						uniId.add(key);
						id = "Unicarb id : " + key;
						all = all + id + "\n";
						glycanAndId.put(seq,id);
					}
				}
				if (!f){
					glycanAndId.put(seq, "no Unicarb ID");
					uniId.add("no Unicarb id");
				}
				all = all + seq + "\n\n";
			}
		}
		output.setText(all);	
		
		return all;
	}

	public void changeInGUI(){
		but.setText("Matched Unicarb DB");
//		but.setFont(new Font("Times New Roman", Font.BOLD, 14));
		but.setForeground(Color.blue);
		String text = String.valueOf(n) + " Unicarb sequences found.";
		labelRef.setText(text);
		labelRef.setFont(new Font("Times New Roman", Font.ITALIC, 12));
		labelRef.setForeground(Color.blue);
	}
	public void actionPerformed(ActionEvent arg0) {
		uniId.clear();
		search();
		changeInGUI();
		System.out.println("gl and id : " + glycanAndId);
	}
	
	public ArrayList<String> getUniId() {
		return uniId;
	}

}
