package action;


import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class OpenFileAction extends AbstractAction {
	/*
	 * button to open a file and extract it
	 */
	
	JFileChooser cho ;
	JPanel jp;
	JTextArea input;
	JTextField jf;
	
	public OpenFileAction(JPanel jp, JTextField jf, JTextArea input){
		cho = new JFileChooser();
		this.input = input;
		this.jp = jp;
		this.jf = jf;
	}
	


      
	public void actionPerformed(ActionEvent arg0) {
		int returnValue = cho.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = cho.getSelectedFile();
			System.out.println(selectedFile.getPath());
			jf.setText(selectedFile.getPath());
			
			String str ="";
			Scanner scanfile;
			try {
				scanfile = new Scanner (new FileReader(selectedFile));
				while (scanfile.hasNextLine())
					str = str + "\n" + scanfile.nextLine();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}		
			System.out.println(str);
			input.setText(str);


	}

	
	}
}

