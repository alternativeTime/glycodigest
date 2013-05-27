package main;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static main.UsualFunctions.*;

public class SectionRep extends SugarTreatment{
/*
 * class treating the REP section
 */
	ArrayList<String> rmain;		//array with the residues of the glycan
	ArrayList<String> lmain;		//array with the linkages of the glycan
	ArrayList<String> repet;		//array with all the repetition : r1, r2...
	ArrayList<String> r;			//array with the residues of the rep part
	ArrayList<String> l;			//array with the linkages of the rep part
	String rep_input;				//String : all the rep part		
	boolean nochange;
	boolean cont;
	boolean changeInRep;
	String allRep;
	String rep;						//the treating repetition : r1 or r2...
	String title;					//the title of the rep : REP1:12o(1+2)10d:7-12
	String numbofrep;				//the number of repetition : 7-12
	public String sugar;			//String : all the glycan
	
//* CONSTRUCTOR AND INITIALIZATION
	public SectionRep(String sugar, EnzymeMapping dico, ArrayList<String> enz, Hashtable<String, Integer> section, String rep_input, boolean unklink) {
		super(sugar,dico,enz,section, unklink);
		this.rep_input = rep_input;
		initSectionRep();		
	}
	public void initSectionRep(){
		rmain = new ArrayList<String>();
		lmain = new ArrayList<String>();
		repet = new ArrayList<String>();
		r = new ArrayList<String>();
		l = new ArrayList<String>();
		nochange = true;
		cont = false;
		changeInRep = false;
		numbofrep = "";
		title = "";
		sugar = "";
		allRep = "";
		String[] sp_rep = rep_input.split("REP");
		for (int i=0; i<sp_rep.length; i++)
			if (!sp_rep[i].isEmpty())
			repet.add(sp_rep[i]);

	} 

	//* CLASS FUNCTIONS
	public boolean treat2Link(String link, String enzy){
		boolean ok1 = false;
		boolean ok2 = false;
		String[] allLink = link.split("\\|");
		String[] link1 = new String[2];
		link1[0] = allLink[1];
		link1[1] = allLink[0] + "+" + allLink[1].split("\\+")[1];
		for (int j=0; j<dico.dic_link.get(enzy).length; j++){
			if (dico.dic_link.get(enzy)[j].contentEquals(link1[0]))
				ok1 = true;
			if (dico.dic_link.get(enzy)[j].contentEquals(link1[1]))
				ok2 = true;
		}
		if (ok1 && ok2)
			return true;
		return false;
				
	}

	@SuppressWarnings("unused")
	public void createArray(String rep){
		/*
		 * CREATE PARTS 're' : RESIDUE AND 'li' : LINKAGE INSIDE THE REP SECTION
		 * if the rep is composed by only one residue, stock only this residue in 're'
		 */
		r.clear();
		l.clear();
		int oneres_rep = oneResidueFromString(rep, 2);
		if (rep.contains("LIN")){
			String[] sp_rep = rep.split("LIN");
			String[] rtemp = sp_rep[0].split("\n");
			String[] ltemp = sp_rep[1].split("\n");
			title = rtemp[0];	
			for (int j=2; j<rtemp.length; j++)
				r.add(rtemp[j]);
			for (int j=0; j<ltemp.length; j++)
				l.add(ltemp[j]);
			l.remove(0);
		}		
		else{ 									//if rep contain only one residue and no substituent (so no section LIN)
			String[] sp_rep = rep.split("\n");
			r.add(sp_rep[sp_rep.length-1]);
			title = sp_rep[0];
		}


	}	
	public void removeResidueLinkageInRep(ArrayList<String> r, ArrayList<String> l){
		/*
		 * REMOVE RESIDUES AND LINKAGES OF THE REP SECTION
		 */
		int size1 = r.size();
		ArrayList<ResidueTerm> tab_res_term = new ArrayList<ResidueTerm> ();					
		ArrayList<Residue> tab_res = new ArrayList<Residue>();		
		Hashtable<String, Integer> term = new Hashtable<String,Integer>();			
		do {
			createTab_res_term(r, l, tab_res, tab_res_term, term);
			if (!tab_res_term.isEmpty())
				removeResAndLink(l, r, tab_res, tab_res_term);	
			clear(tab_res_term, tab_res, term);

			}
		while (!tab_res_term.isEmpty()&& oneResidueFromArray(r) == 0);
		if (size1 != r.size())
			nochange = false;
	}

	public void repFormatage(){
		rep = "";
		rep = rep + "REP" + title + "\nRES\n";
		for (int j = 0; j<r.size(); j++)
			rep = rep + r.get(j) + "\n";
		rep = rep + "LIN\n";
		for (int j = 0; j<l.size()-1; j++)
			rep = rep + l.get(j) + "\n";
	}
	public boolean treatNonterminalRep(){
		/*
		 * TREAT THE NON-TERMINAL REPETITION : IF A RESIDUE IS TERMINAL IN THE REP EVEN IF THE REP IS NO TERMINAL
		 * RETURN IF THERE IS A CHANGMENT OR NOT
		 */
		System.out.println(">>>>>>>>>>>>> NON-TERMINAL REPETITION <<<<<<<<<<<<<<<");
		for (int i=0; i<repet.size(); i++){
			rep = repet.get(i);
			createArray(rep);
			if (oneResidueFromArray(r) == 0){
				//add the link of the title of the rep, to count it 
				Pattern p = Pattern.compile("(\\d:\\d+[a-z]\\(.+\\)\\d+[a-z]).*");
				Matcher m = p.matcher(title.split("\n")[0]);
				if (m.find())
					l.add(m.group(1));	
				
				removeResidueLinkageInRep(r,l);
	
				if (nochange)
					System.out.println("NonTerminalRep - " + i + ": no change.");		
				else{
					System.out.println("NonTerminalRep - " + i + " : change.");
					repFormatage();
				}
			}
			else{
				System.out.println("NonTerminalRep - " + i + " : no change.");		
			}
		}
		allRep = allRep + rep;
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		 return nochange;
		
}
	
	public String searchResRep (ArrayList<String> t, Integer j){
		/*
		 * FIND THE NUMBER OF THE RESIDUE CODING FOR THE REP / 
		 * 7r:r1 > send 7
		 */
		for (int i = 0; i<t.size(); i++){
			if (t.get(i).startsWith(patt(t.get(i)) + "r:r" + j.toString()))
				return patt(t.get(i));		
		}
		return "";
	}	

	public boolean repTerm(String nrep){
		boolean y = true;
		for (int i=0; i<lmain.size(); i++){
			if (lmain.get(i).contains(":" + nrep)){
				return false;
			}
		}
		return y;
	}
	public String getLinkAndTitle(){
		/*
		 * EXTRACT INFORMATION FROM THE TITLE OF REP 
		 * link : linkage which bonds the rep
		 * numofrep : number of repetition 
		 */
		String link= "";
		Pattern p = Pattern.compile("\\d+:\\d+[a-z](\\(.+\\))\\d+[a-z]=(.*)");
		Matcher m = p.matcher(title);
		if (m.find()){
			link = m.group(1);
			numbofrep = m.group(2);
		}
		return link;		
	}

	public void changeRep(ArrayList<String> rmain, ArrayList<String> lmain, String nrep, ArrayList<String> ltable){
		/*
		 * IF THE REP PART CHANGES : A RESIDUE IS CUT BUT NOT ALL
		 * les residues non coupés partent dans les résidues généraux et la section rep ne change pas
		 */
		//add residue
		for (int i = 0; i<r.size(); i++)
			rmain.add(r.get(i));
		//add linkage (1 new linkage)
		String temp = ltable.size()+1 + ":" + nrep + "n" + getLinkAndTitle() + String.valueOf(Integer.parseInt(nrep)+1) + "d";
		lmain.add(temp);
		for (int i= 0; i<l.size(); i++)
			lmain.add(l.get(i));
		changeInRep = true;
	}			
	public void treatTermRepManyRes(int i, String nrep, ArrayList<String> rtable, ArrayList<String> ltable){
		
		removeResidueLinkageInRep(r, l);
		if (nochange)
			System.out.println("TerminalRepManyRes : "+ i + ": no change ");	
		
		else{
			System.out.println("TerminalRepManyRes  : "+ i + " : change");	
			if (oneResidueFromArray(r) != 1)
				changeRep(rmain,lmain, nrep, ltable);
			}
		cont= false;				
	}

	public boolean noRep(ArrayList<String> rmain, ArrayList<String> lmain, Integer rr,  ArrayList<String> r, ArrayList<String> l){
		/*
		 * ALL THE RESIDUE OF THE REP PART ARE REMOVE
		 */
		System.out.println("NO section REP");
		int removeR = 0;
		int removeL = 0;
		rep = "";
		if (oneResidueFromArray(rmain) != 2){
			for (int i=0; i<rmain.size(); i++)
				if (rmain.get(i).startsWith(patt(rmain.get(i)) + "r:r" + rr.toString()))
					removeR = i;				
			removeL = Integer.parseInt(patt(rmain.get(removeR)));
			rmain.remove(removeR);
			for (int i=0; i<lmain.size(); i++)
				if (lmain.get(i).contains(")" + removeL + "n"))
					lmain.remove(i);
		}
		else{
			rmain.clear();
			lmain.clear();
			rmain.addAll(r);
			lmain.addAll(l);
		}
		return true;
	}
	public String getLinkFromRes(Integer rr){
		String linkFromRes = "";
		String resrep = "";
		for (int i=0; i<rmain.size(); i++)
			if (rmain.get(i).startsWith(patt(rmain.get(i)) + "r:r" + rr.toString()))
				resrep = patt(rmain.get(i));	
		for (int i=0; i<lmain.size(); i++)
			if (lmain.get(i).contains(")" + resrep)){
				Pattern p = Pattern.compile(".+\\((.+)\\)");
				Matcher m = p.matcher(lmain.get(i));
				if (m.find())
					linkFromRes = m.group(1);
			}
	return linkFromRes;
	}
	public boolean linkFromResOK(Integer rr, String enzy){
		boolean ok = false;
		String linkFromRes = getLinkFromRes(rr);
		for (int j=0; j<dico.dic_link.get(enzy).length; j++)
			if (dico.dic_link.get(enzy)[j].contentEquals(linkFromRes))
				ok = true;
		return ok;
	}
	
	public void treatTermRepOneRes(int i, ArrayList<String> rtable, ArrayList<String> ltable, String nrep){
		System.out.println("Terminal Rep One res");
		boolean ch = true;
		CreateTabResTerm ctrt = new CreateTabResTerm(r, l, enz, dico);
		ArrayList<Residue> tab_res = new ArrayList<Residue>();	
		ctrt.createTab_res(tab_res, r, l);
		if (!tab_res.isEmpty()){
			String enzy = tab_res.get(0).getEnz();
			Pattern p = Pattern.compile(".+\\((.+)\\)");
			Matcher m = p.matcher(title);
			if (m.find()){
				String link = m.group(1);
				if (link.contains("|")){
					if (treat2Link(link, enzy)){
						cont = noRep(rmain, lmain, i+1, r,l);		
						ch = false;
					}
				}
				else{
					for (int j=0; j<dico.dic_link.get(enzy).length; j++){
						if (dico.dic_link.get(enzy)[j].contentEquals(link) && linkFromResOK(i+1, enzy)){
							cont = noRep(rmain, lmain, i+1, r, l);		
							ch = false;
							break;
						}
					}
				}
			}
		}
		if (ch){
			changeRep(rmain,lmain, nrep, ltable);
			cont = false;
		}
	}

	public boolean treatTerminalRep (Hashtable<String, Integer> term, ArrayList<String> rtable, ArrayList<String> ltable){
		/*
		 * TREAT THE TERMINAL REPETITION : IF THE REPETITION IS TERMINAL
		 * return cont : say if the simulation continue or not after that : depending on if the rep is remove entirely or not
		 */
		nochange = true;
		rmain = rtable;
		lmain = ltable;

		System.out.println(">>>>>>>>>>>>>> TERMINAL REPETITION <<<<<<<<<<<<<<<<<<<< ");
		for (int i=0; i<repet.size(); i++){
			rep = "REP"+repet.get(i);		
			String nrep = searchResRep (rtable, i+1);
			if (repTerm(nrep)){
				createArray(rep);
				if (oneResidueFromArray(r) == 0){
					treatTermRepManyRes(i, nrep, rtable, ltable);		
				}
				if(oneResidueFromArray(r) == 1) {
					treatTermRepOneRes(i,rtable,ltable, nrep);
				}
		}
		allRep = allRep + rep;
	}
	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
	return cont;
}

	
//* Getters
	public ArrayList<String> getRmain() {
		return rmain;
	}
	public ArrayList<String> getLmain() {
		return lmain;
	}
	public String getSugar() {
		return sugar;
	}
	public String getRep() {
		return allRep;
	}
	public boolean isChangeInRep() {
		return changeInRep;
	}
	
	
	


	
}
