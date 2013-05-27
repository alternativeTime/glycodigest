package action;



import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import main.Sugar;


@SuppressWarnings("serial")
public class SubmitAction extends AbstractAction {
/*
 * Action when Submit is called. translates from IUPAC to GlycoCT if necessary, takes enzymes and calls Sugar class.
 */
	JPanel jp;
	JTextArea input;
	JTextArea output;
	JList<String> list;
	JCheckBox check;
	ArrayList<String> enz;
	public boolean unklink = false;
	JRadioButton iupac;	
	String app;
	String in ;
	
	public SubmitAction (JPanel jp, JTextArea input, JTextArea output, 	JList<String> list, JCheckBox check, JRadioButton iupac){
		this.jp = jp;
		this.input = input;
		this.output = output;
		this.list = list;
		this.check = check;
		enz = new ArrayList<String>();
		app = "";
		in = "";
		this.iupac = iupac;

	}	

	
	@SuppressWarnings("rawtypes")
	public void enzymeChoice(){
		enz.clear();
		ListModel model1 = list.getModel();
		if (model1.getSize() != 0){
			String e1 = (String) model1.getElementAt(model1.getSize()-1);
			String e2[] = e1.split("\\+");
			for (int i =0; i<e2.length; i++)
				enz.add(e2[i]);
		}
	}

	public void actionPerformed(ActionEvent arg0) {

		enzymeChoice();	
		if (!input.getText().isEmpty()){
			if (!enz.isEmpty()){
				in = input.getText();
				
				if (check.isSelected()){
					unklink = true;
				}
				
				Sugar s = new Sugar(in, enz, unklink, iupac.isSelected());
				try {
					app= s.glycanTreatment();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				if (s.isExceptFormat())
					JOptionPane.showMessageDialog(jp, "input format incorrect\nmore information in the GlycoCT_manual pdf", "Error", JOptionPane.ERROR_MESSAGE);
				if (s.isPbDigestion())
					JOptionPane.showMessageDialog(jp, "Problem during the digestion, sorry", "Error", JOptionPane.ERROR_MESSAGE);
				this.output.setText(output.getText() + app);
			}
			else 
				JOptionPane.showMessageDialog(jp, "no enzyme selected\nPlease choose one enzyme at least", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else 
			JOptionPane.showMessageDialog(jp, "No glycan in input \nPlease load a glycan sequence ", "Error", JOptionPane.ERROR_MESSAGE);
		}

	
//GETTERS
	public boolean isUnklink() {
		return unklink;
	}


	}


