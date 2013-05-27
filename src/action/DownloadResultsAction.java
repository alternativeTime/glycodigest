package action;


import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class DownloadResultsAction extends AbstractAction{
/*
 * Button for download raw digested sequences
 */
	JPanel jp;
	JTextArea output;
	
	public DownloadResultsAction(JTextArea output){
	this.output = output;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser cho = new JFileChooser();
		int returnValue = cho.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = cho.getSelectedFile();
			String sug = output.getText();
			FileWriter fw;
				try {
					fw = new FileWriter(selectedFile.getPath());
					fw.write(sug);
					fw.close();
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}

			

			String text = "In silico glycans digestion save in the file "+selectedFile.getPath()+".";
			JOptionPane.showMessageDialog(jp, text, "information", JOptionPane.INFORMATION_MESSAGE);
		}
	}
		
	

}
