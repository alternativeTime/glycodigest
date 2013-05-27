package sugar_gui;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class HelpEnzyme extends JLabel {

	
	public HelpEnzyme(){
		
		final ImageIcon enzymeActivityPict = new ImageIcon(getClass().getResource("/pictures/help_picture.png"));
		Icon flag = new ImageIcon(getClass().getResource("/pictures/questionp.png"));
		setIcon(flag);
		
		addMouseListener(new MouseAdapter() {
		
			public void mouseEntered(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			public void mouseClicked(MouseEvent e){
				JDialog dialog = new JDialog();
				dialog.setTitle("Enzyme activity");
				JScrollPane all = new JScrollPane();
				dialog.add(all);
				JLabel jl = new JLabel(enzymeActivityPict);
				all.setViewportView(jl);
				dialog.pack();
				dialog.setVisible(true);	
				}
			});
		
	}

	
}
