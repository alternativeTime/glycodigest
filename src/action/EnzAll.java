package action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;


	public class EnzAll extends MouseAdapter {
		
		JList<String> l1;
		DefaultListModel<String> listModel1;
		DefaultListModel<String> listModel2;
		JCheckBox buttEnzAll;
		
		public EnzAll(final JList<String> l1, final DefaultListModel<String> listModel1, final DefaultListModel<String> listModel2, JCheckBox buttEnzAll){
			this.l1 = l1;
			this.listModel1 = listModel1;
			this.listModel2 = listModel2;
			this.buttEnzAll = buttEnzAll;
		}
		

		  public void mouseClicked(MouseEvent e){
			  
			  if (buttEnzAll.isSelected()){
				while (!listModel1.isEmpty()){
					if (listModel2.isEmpty())
						listModel2.addElement((String) listModel1.get(0));
					else{
						String lastElement = listModel2.getElementAt(listModel2.getSize()-1);
						listModel2.addElement( lastElement +"+"+ (String) listModel1.get(0));
					}
					listModel1.remove(0);
				}
			  }
		  }

}
