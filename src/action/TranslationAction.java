package action;



import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import parser.IupacParser;

public class TranslationAction extends MouseAdapter{

	JPanel jp;
	JTextArea input;
	
	public TranslationAction(JTextArea input, JPanel jp){
		this.input = input;	
		this.jp = jp;
	}
	
	public void translate_iupac_to_ct(){
		String out = "";
		String in = input.getText();
		String[] sp_in = in.split("\n\n");

		for( int i =0; i<sp_in.length; i++){
			System.out.println("sp :  "+ sp_in[i]);
			if (!sp_in[i].isEmpty()){
				if (!sp_in[i].contains("RES")){
					String temp = sp_in[i];
					IupacParser p= new IupacParser(temp.trim());
					try {
						p.getCtTree(p.parse());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					out = out + sp_in[i] + "\n" + p.getCtSequence() + "\n";
				}
				else
					out = out + sp_in[i] + "\n";
			}
		}
		input.setText(out);
	}	
	
	@Override
	public void mouseClicked(MouseEvent e){
			translate_iupac_to_ct();
			String text = "Caution : the translation between Iupac and GlycoCT format can perturb \nthe enzyme simulation because of a lack " +
					"of information in Iupac format.\nThe translation into GlycoCT will appear below the IUPAC sequence.\n" +
					"The structure display often fails when input is in iupac.";
			JOptionPane.showMessageDialog(jp,  text, "Information", JOptionPane.WARNING_MESSAGE);
		}



}
