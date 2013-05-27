package sugar_gui;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;


@SuppressWarnings("serial")
public class SugarJFrame extends JFrame{
/*
 * main frame, containing the SugarJPanel
 */
	
//	TODO : static ou private et fonction qui renvoit la longeuur
	public static int framewidth;
	
	public SugarJFrame () throws FileNotFoundException{
		this.setTitle("Enzyme Simulation");
		this.setPreferredSize(new Dimension (1100,750));
		JScrollPane all = new JScrollPane();
		add(all);
		all.setViewportView(new SugarJPanel());		
	}
	
	public static void main(String[] args) throws FileNotFoundException {
	

		final SugarJFrame frame = new SugarJFrame();
//		frame.addWindowListener(new WindowAdapter(){
//            public void windowClosing(WindowEvent e){
//                 System.out.println("bla");
//            }
//   });
		frame.pack();
		frame.setVisible(true);	
		framewidth = frame.getWidth();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	

	
	
}
