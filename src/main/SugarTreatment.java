package main;

import java.util.ArrayList;
import java.util.Hashtable;
import static main.UsualFunctions.*;

public class SugarTreatment {
/*
 * digests the sugar : call "CreateTabResTerm" class to find terminal residues, and remove it. 
 */
	String sugar;
	EnzymeMapping dico;
	ArrayList<String> enz;
	Hashtable<String, Integer> section;
	
	ArrayList<String> res;		//the first res array
	ArrayList<String> lin;		//the first lin array
	String rep;
	String und;
	ArrayList<String> undParent;
	boolean changeInRep;	
	String glycan;
	ArrayList<String> app;
	boolean unklink;


//* CONSTRUCTOR AND INITIALIZATION
	public SugarTreatment(String sugar, EnzymeMapping dico, ArrayList<String> enz, Hashtable<String, Integer> section, boolean unklink){
		this.sugar = sugar;
		this.dico = dico;
		this.enz = enz;
		this.section = section;
		this.unklink = unklink;
		initSugarTreatment();		
	}	
	public void initSugarTreatment(){
		res = new ArrayList<String>();						//array of the residues
		lin = new ArrayList<String>();						//array of the links
		rep = "";											//string with the section REP
		und = "";
		undParent = new ArrayList<String>();
		changeInRep = false;
		glycan = "";
		app = new ArrayList<String>();						//array with all possible polysaccharides							
	}
	
	
//* FUNCTIONS FOR SIMULATION 
	public void createParts (){
		/*
		 * DIVIDE THE SUGAR AND CREATE PARTS RES, LIN, REP
		 * fill the array 'RES' and 'LIN' and the string 'REP'
		 * return 'oneres' : see function 'oneResidue()'
		 */
			int oneres = oneResidueFromString(sugar, 1);
			System.out.println("one res : " + oneres);
			if (oneres == 0){
				String[] spR = sugar.split("LIN", 2);
				String[] resid = spR[0].split("\n");
				String[] l = new String[500];
				
				if (section.get("REP") == 1 && section.get("UND") == 0){
					String[] spRep = spR[1].split("REP\n");
					l = spRep[0].split("\n");
					rep = spRep[1];
				}
				else if(section.get("REP") == 0 && section.get("UND") == 1){
					String[] spUnd = spR[1].split("UND\n");
					l = spUnd[0].split("\n");
					und = spUnd[1];		
				}
				else
					l = spR[1].split("\n");		
				
				for (int i = 1; i<resid.length; i++)
					res.add(resid[i]);
				for (int i = 1; i<l.length; i++)
					lin.add(l[i]);			
			}
			if (oneres == 2){
				String[] spR = sugar.split("REP\n");
				res.add(spR[0].split("\n")[1]);
				rep = spR[1];
			}
	}

	public void format (ArrayList<String> r, ArrayList<String> l, String rep, String und){
		/*
		 * FORMAT THE GLYCANS (NEED BECAUSE OF REMOVING RESIDUES CHANGE NUMBERS), STOCK THE RESULTING GLYCAN IN THE STRING 'glycan'
		 */
		boolean rep_1 = false;
		if (changeInRep)
			rep_1 = true;
		Formatting f = new Formatting(r,l,rep, und, rep_1);
		glycan = f.format();
	}
	public void clear( ArrayList<ResidueTerm> a1,ArrayList<Residue> a2, Hashtable<String, Integer> h1){
		/*
		 * CLEAR THE ARRAYS AND MAP
		 */
		a1.clear();
		a2.clear();
		h1.clear();
	}

	@SuppressWarnings("rawtypes")
	public void createTab_res_term(ArrayList r, ArrayList l, ArrayList tab_res, ArrayList tab_res_term,  Hashtable term){
		/*
		 * CALL ANOTHER CLASS WHICH CREATE 'tab_res_term' : AN ARRAY WITH THE RESIDUE TO CUT
		 */
		CreateTabResTerm creat = new CreateTabResTerm(r, l, dico, enz, tab_res, tab_res_term, term, unklink);
		creat.beginToTab_res_term();
		tab_res = creat.getTab_res();
		tab_res_term = creat.getTab_res_term(); 
		term = creat.getTerm();
	}	

	public void removeResAndLink(ArrayList<String> l, ArrayList<String> r, ArrayList<Residue> tab_res, ArrayList<ResidueTerm> tab_res_term){
		/*
		 * REMOVE THE RESIDUE AND THE LINK GIVEN BY 'tab_res_term'
		 */
		ResidueTerm residTerm = tab_res_term.get(0);
		ArrayList<String> rtemp = residTerm.getR();
		ArrayList<String> ltemp = residTerm.getL();
		if (und.isEmpty() || !und.isEmpty() && !undParent.contains(rtemp.get(0))){		
			for (int i=0; i<rtemp.size(); i++){
				for (int j=0; j<r.size(); j++){
					if (r.get(j).startsWith(rtemp.get(i)))	{
						r.remove(r.get(j));				
						break;
					}
				}
			}
			for (int j=0; j<l.size(); j++){
				for (int i=0; i<ltemp.size(); i++){	
					if (l.get(j).contains("(" + ltemp.get(i)+")" + rtemp.get(i))){
						System.out.println(l.get(j));
						l.remove(l.get(j));	

					}
				}
			}
		}

		else
			tab_res_term.remove(0);
	}

	public boolean stopLoop(ArrayList<ResidueTerm> tab_res_term){
		//TODO mettre un truc du genre if !und.isEmpty ou undParent : a voir
		boolean stop = false;
		for (int i=0; i<tab_res_term.size(); i++)
			if (undParent.contains(tab_res_term.get(i).getR().get(0)))
				stop = true;
			else
				stop = false;
		return stop;
	}
	
	public String glycanSubTreatment ()  { 
		/*
		 * DO ALL STEP TO SEARCH IF A RESIDUE IS TERMINAL AND REMOVE IT
		 */
		ArrayList<String> r = res;
		ArrayList<String> l = lin;
		ArrayList<ResidueTerm> tab_res_term = new ArrayList<ResidueTerm>();					
		ArrayList<Residue> tab_res = new ArrayList<Residue>();					
		Hashtable<String, Integer> term = new Hashtable<String,Integer>();	
		boolean cont = false;

		do {
			if (oneResidueFromArray(r) == 1)
				break;
			
			cont = false;
			clear (tab_res_term, tab_res, term);
			
			if (section.get("UND") == 1 && !und.isEmpty())
				underdeterminedTreatment();
			System.out.println("#####");
			createTab_res_term(r, l, tab_res, tab_res_term, term);
			if (!tab_res_term.isEmpty()){ 		
				removeResAndLink(l, r, tab_res, tab_res_term);
			}	
			if (section.get("REP") == 1 && !rep.isEmpty() && tab_res_term.isEmpty())
				cont = repetitionTreatment(r,l,term);
					
		}
		
		while (!tab_res_term.isEmpty() && !stopLoop(tab_res_term) || cont && !stopLoop(tab_res_term));
		format (r,l, rep, und);

		return glycan;
	}
	
	
//* REP and UND TREATMENT 
	public boolean repetitionTreatment (ArrayList<String> r, ArrayList<String> l, Hashtable<String, Integer> term){	
		SectionRep rs = new SectionRep(sugar, dico, enz, section, rep, unklink);
		boolean cont = rs.treatTerminalRep(term, r, l);
		changeInRep = rs.isChangeInRep();
		r = rs.getRmain();
		l = rs.getLmain();
		rep = rs.getRep();
		return cont;
	}
	public void underdeterminedTreatment(){
		SectionUnd sectUnd = new SectionUnd(sugar, dico, enz, section, und, unklink);
		undParent = sectUnd.treatUnd();
		und = sectUnd.getUnd();
	}

	
//* GETTERS AND SETTERS
	public String getRep(){
		return rep;
	}
	public void setRep(String rep){
		this.rep = rep;
	}	
	

}
