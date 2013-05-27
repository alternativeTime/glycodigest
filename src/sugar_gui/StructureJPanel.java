package sugar_gui;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import action.GoToUnicarbAction;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class StructureJPanel extends JPanel{
	/*
	 * JPanel containing pictures of structures
	 * maybe the time problem is here
	 */
	
	public ArrayList<String> name;
	public int imwidth;
	public StructureJPanel(ArrayList<String> name){
		this.name = name;
		MigLayout miglay = new MigLayout ("align 50%","[][]");
		setLayout(miglay);
		setBackground(Color.white);
		init();
	}

	
	public void init(){
		String labId = "";
		String labEnz = "";

		ImageIcon im;
		System.out.println("name : " + name);
		
		for (int i = 0; i<name.size(); i++){
			
			//image
			if (name.get(i).contains("noImage"))
				im = new ImageIcon(getClass().getResource("/pictures/noImage.png"));	
			else
				im = new ImageIcon(name.get(i));
			JLabel j = new JLabel(im);

			//id
			labId = "";
			JLabel link = new JLabel("");
			if (name.get(i).contains("no_Unicarb_ID")){
				labId = "No Unicarb ID";
//				link.setText(" ");
			}
			else{
				if (name.get(i).contains("+"))
					labId = name.get(i).split("\\+")[0];
				else
					labId = name.get(i).split("\\.")[0];
				link = new GoToUnicarbAction(labId);
			}
			labId = "Unicarb id : " + labId;
			JLabel j2 = new JLabel(labId);
			
			//enzyme when output
			if (name.get(i).contains("+"))
				labEnz = name.get(i).split("\\+", 2)[1].split("\\.")[0];
			else
				labEnz = "";
			JLabel j3 = new JLabel(labEnz);
			

			add(j2, "newline, split 3, flowy");
			add(link);
			add (j3);
			add(j, "right");

		}
	}
}

