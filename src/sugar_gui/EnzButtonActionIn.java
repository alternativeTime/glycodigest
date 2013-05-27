package sugar_gui;


import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

@SuppressWarnings("serial")
public class EnzButtonActionIn extends JLabel  {
/*
 * button to enter enzymes
 */
	
	JList<String> l1;
	DefaultListModel<String> listModel1;
	DefaultListModel<String> listModel2;

	public EnzButtonActionIn (final JList<String> l1, final DefaultListModel<String> listModel1, final DefaultListModel<String> listModel2){
		this.l1 = l1;
		this.listModel1 = listModel1;
		this.listModel2 = listModel2;
	
		Icon flag = new ImageIcon(getClass().getResource("/pictures/fleche11.png"));
		setIcon(flag);
		
		addMouseListener(new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		
		public void mouseClicked(MouseEvent e){
			for (int i=0; i<listModel1.getSize(); i++)
				if (l1.getSelectedIndex() == i){
					if (listModel2.isEmpty())
						listModel2.addElement((String) l1.getSelectedValue());
					else{
						String lastElement = listModel2.getElementAt(listModel2.getSize()-1);
						listModel2.addElement( lastElement +"+"+ (String) l1.getSelectedValue());
					}
					listModel1.remove(i);
				}
			}
		});
	
	}

}
