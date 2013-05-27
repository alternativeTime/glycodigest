package action;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class GoToUnicarbAction extends JLabel {

	String name;
	public GoToUnicarbAction(String name){
		this.name = name;
		this.setText("http://www.unicarb-db.org/" + name);
		this.setForeground(Color.blue);
		this.setFont(new Font("Calibri", Font.ITALIC, 12));
		
		addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			public void mouseClicked(MouseEvent e){
				URI uri = URI.create("http://www.unicarb-db.org/");
				try {
					Desktop.getDesktop().browse(uri);
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
		});

	}
}
