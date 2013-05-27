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
public class EnzButtonActionOut extends JLabel {
/*
 * button to remove enzymes 
 */
	JList<String> l2;
	DefaultListModel<String> listModel1;
	DefaultListModel<String> listModel2;
	
	public EnzButtonActionOut (final JList<String> l2, final DefaultListModel<String> listModel1, final DefaultListModel<String> listModel2){
		this.l2 = l2;
		this.listModel1 = listModel1;
		this.listModel2 = listModel2;
	
		Icon flag = new ImageIcon(getClass().getResource("/pictures/fleche22.png"));
		setIcon(flag);
		
		addMouseListener(new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		
		public void mouseClicked(MouseEvent e){
			String toremove = "";
			boolean select = false;
			int index = 0;
			for (int i=0; i<listModel2.getSize(); i++){
				if (l2.getSelectedIndex() == i){
					select = true;
					String[] temp = listModel2.get(i).split("\\+");
					listModel1.addElement(temp[temp.length-1]);
					index = i;
					toremove = temp[temp.length-1];
					listModel2.remove(i);
					break;
				}
			}
			if (select){
				for (int i=index; i<listModel2.getSize(); i++){
					String t = listModel2.get(i).replace(toremove + "+", "");
					listModel2.remove(i);
					listModel2.add(i, t);
				}
			}
			
			
		}
		});
	
	}
}

