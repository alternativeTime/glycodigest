package main;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static main.UsualFunctions.*;

public class CreateTabResTerm {
	/*
	 * find terminal residues matching with enzymes and return an array with residues, linkages and enzymes to remove
	 */

	ArrayList<String> r;
	ArrayList<String> l;
	EnzymeMapping dico;
	ArrayList<String> enz;
	boolean unklink;
	ArrayList<Residue> tab_res;					// array of good residues (res - *substituent*  ; enzyme)
	ArrayList<ResidueTerm> tab_res_term;		// array of good and terminal residues (res - *substituent* ; link - linkSubsituent ; enzyme)
	Hashtable<String, Integer> term;			// hashtable (N°link - number of occurrence in left part of linkage)

//* CONSTRUCTORS
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CreateTabResTerm(ArrayList r, ArrayList l, EnzymeMapping dico, ArrayList enz, ArrayList tab_res, ArrayList tab_res_term, Hashtable term, boolean unklink){
		this.r = r;
		this.l = l;
		this.dico = dico;
		this.enz = enz;
		this.tab_res = tab_res;
		this.tab_res_term = tab_res_term;
		this.term = term;
		this.unklink = unklink;

	}
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public CreateTabResTerm(ArrayList r,  ArrayList l, ArrayList enz, EnzymeMapping dico){
		this.r = r;
		this.l = l;
		this.enz = enz;
		this.dico = dico;
		ArrayList<String> tab_res_term = new ArrayList<String> ();	
		Hashtable<String, String> tab_res = new Hashtable<String,String>();	
		Hashtable<String, Integer> term = new Hashtable<String,Integer>();
	}

	
	//* CLASS FUNCTIONS
	public boolean verifLinkSubstituent (ArrayList<String> r, ArrayList<String> l, ArrayList<String> res, String pa, String[] a, String enzy){
		/*
		 * USED IN createTab_res
		 * if the residue have a substituent : check if the basetype link the substituent, check if the substituent is ok and add it in tab_res
		 */
		boolean ok = false;
		if (dico.family.get(enzy) == 2){
			for (int i=0; i<l.size(); i++){
				Pattern li = Pattern.compile("\\d+:(\\d+).*\\((.*)\\)(\\d+)");
				Matcher m = li.matcher(l.get(i));
				if (m.find()) {
					String res1 = m.group(1);
					String res2 = m.group(3);
					if (pa.equals(res1)){
						for (int j=0; j<r.size(); j++){
							if (r.get(j).contains(res2 + "s")){
								Pattern s = Pattern.compile(a[1]);
								Matcher m1 = s.matcher(r.get(j));
								if (m1.find()){
									res.add(res2);
									return true;
								}
							}
						}
					}
				}		
			}
		}
		else
			return true;
		return ok;
	}
	public String[] enzymePatternCreation(String enzy){
		/*
		 * USED IN createTab_res
		 */
		String[] a = new String[2];	
		if (dico.family.get(enzy) == 2)
			a = dico.dic_res.get(enzy).split("\n");
		else 
			a[0] = (String) dico.dic_res.get(enzy);	
		return a;
	}

	@SuppressWarnings("static-access")
	public void createTab_res(ArrayList<Residue> tab_res, ArrayList<String> r, ArrayList<String> l){
		/*
		 * IF A RESIDUE CAN BE CUT BY AN ENZYME, PUT IN 'tab_res' THIS NUMBER (and if it have a substituent, add it also) AND THIS ENZYME
		 */	
		boolean ok = false;
		System.out.println("enz : " + enz);
		for (int j=0; j<enz.size(); j++){
			String enzy = enz.get(j);
			String[] a = enzymePatternCreation(enzy);
			Pattern reg_res = Pattern.compile(a[0]);
			for(int i = 0; i<r.size(); i++){
				ArrayList<String> res = new ArrayList<String>();
				String pa = patt(r.get(i));
				res.clear();
				ok = false;

				if (reg_res.matches(a[0], r.get(i))){
					res.add(pa);
					ok = verifLinkSubstituent(r,l,res,pa, a, enzy);
				}
				if (pa != null && ok)
					tab_res.add(new Residue(res,enzy));
			}

		}
	}

	public void insertInTerm (String s, Hashtable<String, Integer> term){
		/*
		 * USEd IN 'createTerm'
		 * ADD 1 TO A RESIDUE IF IT'S IN THE LINKAGE
		 */
		if (!term.containsKey(s))
			term.put(s, 1);
		else{
			int a = term.get(s);
			a +=1;
			term.put(s, a);
		}
	}	
	public void createTerm(ArrayList<Residue> tab_res, ArrayList<String> l, Hashtable<String,Integer> term){
		/*
		 * FILL THE HASHTABLE 'term' : FOR EACH BASETYPE RESIDUE OF 'tab_res', SEARCH HOW MANY TIMES IT APPEARS IN THE LEFT LINKAGE AND ADD IT IN 'term'
		 */
		ArrayList<String> memoire = new ArrayList<String>();
		for (int i=0; i<tab_res.size(); i++){
			ArrayList<String> t = tab_res.get(i).getR();
			String res = t.get(0);
			if (!memoire.contains(res)){
				memoire.add(res);
				for (int k=0; k<l.size(); k++){
					String resi = ":" + res;
					if (l.get(k).contains(resi))
						insertInTerm(res,term);			
				}
			}
		}
		
	}

	public String findLink(String enzy, int i, ArrayList<String> l){
		/*
		 * USED IN 'findGoodLink'
		 * return the linkage of the line n°i
		 */
		String ret = "";
		Pattern p = Pattern.compile("\\d+:(\\d+).*\\((.*)\\)(\\d+)");
		Matcher m = p.matcher(l.get(i));
		if (m.find()){
			String link = m.group(2);
			ret =  link;
		}
		return ret;
	}
	public boolean treat2Link(String link, String enzy){
		/*
		 * used in createTabResTerm if the linkage is 3|4
		 * return ok if all the links are good
		 */
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

	public ArrayList<String> findGoodLink(ArrayList<String> l, String res, String enzy){
		/*
		 * USED IN 'createTabResTerm'
		 * if the line contain the residue to remove, check if it corresponds to the enzyme and stock it
		 * if there are a substituent, stock also the linkage of the substituent-basetype
		 */
		ArrayList<String> arLink = new ArrayList<String>();
		String link ="";
		boolean finder= false;
		for (int i=0; i<l.size(); i++){
			if (l.get(i).contains(")"+ res)){	
				link = findLink(enzy, i, l);
				//if linkage is x|x
				if (link.contains("|")){
					if (treat2Link(link, enzy)){
						arLink.add(link);
						finder = true;
					}
				}
				else{
					//if linkage is undefined and unklink checked
					if (link.contains("-1") && unklink){
						if (treatUnknownLinkage(link, enzy, dico)){
							arLink.add(link);
							finder = true;
						}
					}
					else
					for (int j=0; j<dico.dic_link.get(enzy).length; j++)
						if (dico.dic_link.get(enzy)[j].contentEquals(link)){
							finder = true;
							arLink.add(link);
						}
				}
			}
		}
		if (dico.family.get(enzy) == 2)
			for (int i=0; i<l.size(); i++){
				if (l.get(i).contains(":"+ res) && finder){
					link = findLink(enzy, i, l);
					if (!link.isEmpty())
						arLink.add(link);
			}
		}
		return arLink;
	}
	
	public boolean guhTreatment(ArrayList<String> r, ArrayList<String> l, String res){
		boolean cut = true;
		String parent = "";
		for (int i=0; i<l.size();i++){
			if (l.get(i).contains(")"+ res)){
				parent = l.get(i).split(":",2)[1].substring(0, 1);
				if (parent.equals("5") && r.get(4).equals("5b:b-dman-HEX-1:5")){
					cut = false;
				}
			}
		}
		return cut;		
	}

	public boolean amfTreatment(ArrayList<String> r, ArrayList<String> l){
		boolean cut = true;
		ArrayList<String> gal = new ArrayList<String>();
		for (int i=0; i<r.size();i++){
			Pattern reg_res = Pattern.compile("(\\d+)b:b-[dl]gal-HEX-1:5");
			Matcher match = reg_res.matcher(r.get(i));
			if (match.find())
				gal.add(match.group(1));
		}
		for (int i=0; i<l.size(); i++){
			for (int j=0; j<gal.size(); j++){
				String res = gal.get(j);
				if (l.get(i).contains(":"+res))
					gal.remove(res);
			}
		}
		if (!gal.isEmpty())
			cut = false;
		return cut;
	}
	
	public void createTabResTerm(ArrayList<Residue> tab_res, ArrayList<String> r, ArrayList<String> l){
		/*
		 * FILL THE ARRAY 'tab_res_term'
		 * for each residue of 'tab_res' look at if the residue is terminal (cond1Term and cond2Term)
		 * if yes, 
		 */
		for (int i=0; i<tab_res.size(); i++){
			//boolean for AMF and GUH
			boolean guhTreat = true;
			boolean amfTreat = true;
			//take residue number of tab res and create a lin array 
			ArrayList<String> rtemp = tab_res.get(i).getR();
			ArrayList<String> ltemp = new ArrayList<String>();
			String kr = rtemp.get(0);	
			String enzy = tab_res.get(i).getEnz();
			//cond1 : simple residu : pas dans term et famille = 1
			//cond2 : complex residu : 1 dans term et famille = 2
			boolean cond2Term = false;
			boolean cond1Term = !term.containsKey(kr) && dico.family.get(enzy)==1;
			System.out.println("term : " + term + "\tdico : " + dico.family.get(enzy));
			if (!cond1Term)
				cond2Term = dico.family.get(enzy)-1 == term.get(kr);
			System.out.println("kr : "+ kr + "\tenzy : " + enzy + "\t1 : " + cond1Term  + "\t2 : "+cond2Term);
			if (cond1Term || cond2Term){
				ltemp = findGoodLink(l, kr, enzy);
				if (!ltemp.isEmpty()){
					//test for GUH and AMF
					if (enzy.equals("GUH"))
						guhTreat = guhTreatment(r, l, kr);
					else if (enzy.equals("AMF"))
						amfTreat = amfTreatment(r,l);
					//if all ok : creation of ResidueTerm and add to tab_res_term
					if (guhTreat && amfTreat){
						ResidueTerm resTerm = new ResidueTerm(rtemp,ltemp,enzy);
						tab_res_term.add(resTerm);	
					}
				}
			}
		}
	}
	
	public void beginToTab_res_term(){
		/*
		 * DO ALL STEP TO CREATE 'tab_res_term'
		 */
//		if (!tab_res.isEmpty())
			tab_res.clear();
//		if (!term.isEmpty())
			term.clear();

		createTab_res(tab_res, r, l);
		displayTabRes(tab_res);
		createTerm(tab_res, l, term);
		if (!tab_res.isEmpty()){
			createTabResTerm(tab_res,  r, l);
			displayTabResTerm(tab_res_term);
		}
	}


//* GETTERS
	public ArrayList<ResidueTerm> getTab_res_term() {
		return tab_res_term;
	}
	public ArrayList<Residue> getTab_res() {
		return tab_res;
	}
	public Hashtable<String, Integer> getTerm() {
		return term;
	}

	
}
