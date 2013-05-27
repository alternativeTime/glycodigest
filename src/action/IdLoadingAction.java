package action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class IdLoadingAction extends AbstractAction{
	/*
	 * button for loading a sequence from an Unicarb ID
	 */

	JPanel jp;
	JTextField idTextField;
	JTextArea input;
	HashMap<String,String> uni;
	
	public IdLoadingAction(JTextArea input, JTextField idTextField, HashMap<String,String> uni){
		this.input = input;
		this.idTextField = idTextField;
		this.uni = uni;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		String id = idTextField.getText();
		String structureCT = "";
		if (uni.get(id) == null)
			JOptionPane.showMessageDialog(jp, "no entry for this Unicarb id", "Information", JOptionPane.WARNING_MESSAGE);
		else{
			structureCT = uni.get(id);
			input.setText(input.getText() + structureCT + "\n");
		}
		
	}

}
