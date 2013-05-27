package action;


import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import main.EnzymeMapping;

@SuppressWarnings("serial")
public class ClearAction extends AbstractAction{
/*
 * Clear all, but little problem with comboBox of display Structures
 */
	JPanel jp;
	//input
	JTextArea input;
	JTextField idTextField;
	JTextField fileTextField;
	JCheckBox checkDispInput;
	JButton okDispInput;
	JComboBox<String> comboInput;
	ButtonGroup bgroup;
	
	//enzymes
	DefaultListModel<String> listModel1;
	DefaultListModel<String> listModel2;
	JCheckBox check;
	String[] enz_list;
	JCheckBox buttEnzAll;

	//output
	JTextArea output;
	JCheckBox checkDispOutput;
	JButton okDispOutput;
	JComboBox<String> comboOutput;
	JButton ref;
	JLabel labelRef;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ClearAction(JTextArea input, JTextField idTextField, JTextField fileTextField, DefaultListModel listModel1, DefaultListModel listModel2, 
			JTextArea output, JCheckBox check, JButton okDispInput , JButton okDispOutput, JCheckBox checkDispInput, JCheckBox checkDispOutput, 
			JComboBox comboInput , JComboBox comboOutput, ButtonGroup bgroup, JButton ref, JLabel labelRef, JCheckBox buttEnzAll){
		this.input = input;
		this.idTextField = idTextField;
		this.fileTextField = fileTextField;
		this.okDispInput = okDispInput;
		this.checkDispInput = checkDispInput;
		this.comboInput = comboInput;
		this.bgroup = bgroup;
		
		this.listModel1= listModel1;
		this.listModel2= listModel2;
		this.check = check;
		EnzymeMapping c = new EnzymeMapping();
		enz_list = c.getEnz_list();
		this.buttEnzAll = buttEnzAll;
		
		this.output = output;
		this.okDispOutput = okDispOutput;
		this.checkDispOutput = checkDispOutput;
		this.comboOutput = comboOutput;
		this.ref = ref;
		this.labelRef =labelRef;
	}
	

	public void clear_all(){
		input.setText("");
		idTextField.setText("");
		fileTextField.setText("");
		output.setText("");
		listModel1.removeAllElements();
		labelRef.setText("");
		ref.setText("Matching Unicarb DB");
		ref.setForeground(Color.black);
		for (int i=0; i<enz_list.length; i++)
			listModel1.addElement(enz_list[i]);
		listModel2.removeAllElements();
		if (check.isSelected())
			check.doClick();
		if (buttEnzAll.isSelected())
			buttEnzAll.doClick();
		comboInput.removeAllItems();
		if (checkDispInput.isSelected()){
			checkDispInput.doClick();
			comboInput.setVisible(false);
			okDispInput.setVisible(false);
		}
		comboOutput.removeAllItems();
		if (checkDispOutput.isSelected()){
			checkDispOutput.doClick();
			comboOutput.setVisible(false);
			okDispOutput.setVisible(false);
		}
		bgroup.clearSelection();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		clear_all();
		System.out.println("!!!!!!!!CLEAR!!!!!!!!");
		
	}

}
