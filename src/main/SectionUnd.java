package main;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static main.UsualFunctions.*;

public class SectionUnd extends SugarTreatment {
/*
 * class treating the UND section
 */
	String und_input;
	String und;
	ArrayList<String> parent;
	String allUnd;
	String title;
	boolean nochange;
	ArrayList<String> undArray;

//* CONSTRUCTOR AND INITIALIZATION
	public SectionUnd(String sugar, EnzymeMapping dico, ArrayList<String> enz, Hashtable<String, Integer> section, String und, boolean unklink){
		super(sugar,dico,enz,section, unklink);
		this.und_input = und;
		initSectionUnd();
	}
	public void initSectionUnd(){
		undArray = new ArrayList<String>();
		allUnd = "";
		title = "";
		parent = new ArrayList<String>();
		nochange = true;
		String[] sp_und = und_input.split("UND");
		for (int i=0; i<sp_und.length; i++)
			if (!sp_und[i].isEmpty())
				undArray.add(sp_und[i]);
	}

//* CLASS FUNCTIONS 
	public int determineCase(){
		/*
		 * DETERMINE THE CASE OF UNDERDETERMINED SECTION 
		 * either c = 1 : the und section is on many possible parent
		 * either c = 2 : the und section in on 1 parent but with a percentage (between 60% and 80%) : untreated : I don't see a case for now
		 */
		int c = 0;
		String cas = und.split("\n")[0];
		Pattern p = Pattern.compile("\\d+:(\\d+)\\..+");
		Matcher m = p.matcher(cas);
		if (m.find()){
			if (m.group(1).equals("100"))
				return 1;
			else 
				return 2;
		}
		return c;
	}
	public void createPartUnd(ArrayList<String> r, ArrayList<String> l){
		/*
		 * create parts title, res and lin depending on if there are only one residue or not...
		 */
		String[] tempr;
		String[] sp_und = und.split("RES");
		title = sp_und[0];
		String temp = sp_und[1];
		if (temp.contains("LIN")){
			String[] sp_und1 = temp.split("LIN");
			String[] templ = sp_und1[1].split("\n");
			for (int i=1; i<templ.length; i++)
				l.add(templ[i]);
			tempr = sp_und1[0].split("\n");
		}
		else
			tempr = temp.split("\n");
		
		for (int i=1; i<tempr.length; i++)
			r.add(tempr[i]);		
	}
	public int determinCaseAndCreatePartUnd(ArrayList<String> undArray, int un, ArrayList<String> r, ArrayList<String> l){
		/*
		 * refresh arrays, determine case and create part
		 */
		und = undArray.get(un);
		r.clear();
		l.clear();
		int cas = determineCase();
		createPartUnd(r, l);
		return cas;
	}

	public void fillParent(){
		/*
		 * fill the array 'parent' containing all the parent (possible or not) of the und section
		 */
		String[] temp = title.split("\n");
		String[] temp1 = temp[1].split(":");
		String[] temp2 = temp1[1].split("\\|");
		for (int i = 0; i<temp2.length; i++)
			parent.add(temp2[i]);
	}

	public boolean goodLink(ArrayList<Residue> tab_res, ArrayList<String> r, String title){
		/*
		 * USED IN subTreatUndOneRes
		 * Check the link which bind the only one residue of Und to the parent residue and if it's good return true
		 */
		boolean y = false;
		//take link of und title
		String link = "";
		String[] temp = title.split("\n");
		Pattern p = Pattern.compile(".+\\((.+)\\).+");
		Matcher m = p.matcher(temp[2]);
		if (m.find()){
			link = m.group(1);
			
			for (int i = 0; i<tab_res.size(); i++){
				String enzTemp = tab_res.get(i).getEnz();
				//if link is unknown
				if (link.contains("-1") && unklink)
					if (treatUnknownLinkage(link, enzTemp, dico))
						y = true;
			
				for (int k=0; k<dico.dic_link.get(enzTemp).length;k++)
					if (dico.dic_link.get(enzTemp)[k].contentEquals(link))
						y = true;
			}
		}
		return y;
	}
	public boolean subTreatUndOneRes(ArrayList<String> r,ArrayList<String> l, int un){
		/*
		 * treat the case of there is only one residue : check if the residue correspond to the enzyme : fill tab_res
		 * then, if tab_res is not empty : check the link which bind the und residue on the parent residue
		 * if ok : remove the residue so there is not section und yet
		 */
		boolean contUnd = false;
		CreateTabResTerm ctrt = new CreateTabResTerm(r, l, enz, dico);
		ArrayList<Residue> tab_res = new ArrayList<Residue>();	
		ctrt.createTab_res(tab_res, r, l);
		if (!tab_res.isEmpty()){
			displayTabRes(tab_res);
			boolean goodLi = goodLink(tab_res, r, title);
			System.out.println("good : " + goodLi);
			if (!goodLi){
				System.out.println("UndSectionOneRes : "+ un + " : no change ");	
				if (nochange)
					nochange = true;
				contUnd = false;
			}
			else{
				und = "";	
				parent.clear();
				System.out.println("UndSectionOneRes : "+ un + " : change : no Und section ");	
				nochange = false;
				contUnd = true;
			}
		}
		return contUnd;
	}

	public void removeResidueLinkageInUnd(ArrayList<String> r, ArrayList<String> l){
		/*
		 * REMOVE RESIDUES AND LINKAGES OF THE UND SECTION
		 */
		int size1 = r.size();
		ArrayList<ResidueTerm> tab_res_term = new ArrayList<ResidueTerm>();					
		ArrayList<Residue> tab_res = new ArrayList<Residue>();			
		Hashtable<String, Integer> term = new Hashtable<String,Integer>();		
		do {
			createTab_res_term(r, l, tab_res, tab_res_term, term);
			if (!tab_res_term.isEmpty())
				removeResAndLink(l, r, tab_res, tab_res_term);		
			clear(tab_res_term, tab_res, term);
		}
		while (!tab_res_term.isEmpty() && oneResidueFromArray(r) == 0);
		if (size1 != r.size())
			nochange = false;
	}	
	public boolean subTreatUndManyRes(ArrayList<String> r, ArrayList<String> l){
		boolean contUnd = true;
		removeResidueLinkageInUnd(r,l);
		if (nochange)
			contUnd = false;
		return contUnd;
	}

	public void changeUnd(ArrayList<String> r, ArrayList<String> l){
		und = "";
		und = title + "RES\n";
		for (int i=0; i<r.size(); i++)
			und = und + r.get(i) + "\n";
		und = und + "LIN\n";
		for (int i=0; i<l.size();i++)
			und = und + l.get(i) + "\n";
	}

	@SuppressWarnings("unused")
	public ArrayList<String> treatUnd(){
		System.out.println("*************** UNDO section *******************");
		boolean contUnd = false;
		ArrayList<String> r = new ArrayList<String>();
		ArrayList<String> l = new ArrayList<String>();
		
		for (int un=0; un<undArray.size(); un++){
			
			int cas = determinCaseAndCreatePartUnd(undArray, un, r, l);
			fillParent();
//			if (cas == 1){				
				if (oneResidueFromArray(r) == 0)
					subTreatUndManyRes(r,l);	
				
				if (oneResidueFromArray(r) == 1)
					contUnd = subTreatUndOneRes(r,l, un);				
			
		if (!nochange && !und.isEmpty())	
			changeUnd(r,l);
		if (!und.isEmpty())
			allUnd= allUnd + "UND" + und;
		}

		System.out.println("*************************************************");
		return parent;
	}
	
//* GETTERS
	public String getUnd(){
		return allUnd;
	}
}

