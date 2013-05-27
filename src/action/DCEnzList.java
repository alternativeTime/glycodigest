package action;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class DCEnzList extends MouseAdapter{
	/*
	 *double click on enzyme puts it on the right panel. 
	 */

	JList<String> l1;
	DefaultListModel<String> listModel1;
	DefaultListModel<String> listModel2;
		    
	public DCEnzList(JList<String> l, DefaultListModel<String> listModel1, DefaultListModel<String> listModel2){
		   l1 = l;
		   this.listModel1 = listModel1;
		   this.listModel2 = listModel2;
	  }
		    
	  public void mouseClicked(MouseEvent e){
	   if(e.getClickCount() == 2){
		   
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
	   
   }
	
}
