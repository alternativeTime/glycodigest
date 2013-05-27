package sugar_gui;


import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class RadioButtFormat {
/*
 * RadioButton for GlycoCT or IUPAC format
 */
	public JRadioButton glyco;
	public JRadioButton iupac ;
	ButtonGroup bgroup;
	
	public RadioButtFormat(){
		bgroup = new ButtonGroup();
		glyco = new JRadioButton("GlycoCT");
		iupac = new JRadioButton("IUPAC");
		bgroup.add(glyco);
		bgroup.add(iupac);		
	}
	
	
	public String format_choice(){
		if (iupac.isSelected())
			return iupac.getText();
		else
			return glyco.getText();
	}

	
//* Getters and Setters
	public JRadioButton getGlyco() {
		return glyco;
	}
	public void setGlyco(JRadioButton glyco) {
		this.glyco = glyco;
	}
	public JRadioButton getIupac() {
		return iupac;
	}
	public void setIupac(JRadioButton iupac) {
		this.iupac = iupac;
	}

}
