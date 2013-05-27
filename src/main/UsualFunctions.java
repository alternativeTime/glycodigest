package main;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsualFunctions {
/*
 * Usual functions for the simulation : return the canonical number of the residue or link, display array, and treat unknown linkages
 */
	
	static String patt (String str){
		/*
		 * RETURN THE NUMBER IN THE BEGINNING OF THE LINE (NUMBER OF THE RESIDUE OR OF THE LINKAGE
		 * return a string		 
		 */
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(str);
		if (m.find())
			return m.group();
		return "";
	}
	
	@SuppressWarnings("rawtypes")
	static void displayTabResTerm(ArrayList<ResidueTerm> tab_res_term){
		System.out.println("TAB_RES_TERM");
		for (int i = 0; i<tab_res_term.size(); i++){
			ArrayList t = new ArrayList();
			t = tab_res_term.get(i).getR();
			for (int j=0; j<t.size(); j++)
				System.out.print(t.get(j) + " ");
			ArrayList ll = new ArrayList();
			ll = tab_res_term.get(i).getL();
			for (int j=0; j<ll.size(); j++)
				System.out.print(ll.get(j) + " ");
			System.out.println(" #" + tab_res_term.get(i).getEnz()+ "#");
		}
		System.out.println("//tab_res_term//\n");
	}
	@SuppressWarnings("rawtypes")
	static void displayTabRes(ArrayList<Residue> tab_res){
		/*
		 * DISPLAY 'tab_res'
		 */
		System.out.println("TAB_RES");
		for (int i = 0; i<tab_res.size(); i++){
			ArrayList t = new ArrayList();
			t = tab_res.get(i).getR();
			for (int j=0; j<t.size(); j++)
				System.out.print(t.get(j) + " - ");
			System.out.println(tab_res.get(i).getEnz());
		}
		System.out.println("//tab_res//\n");
	}

	static int oneResidueFromArray(ArrayList<String> r){
		/*
		 * RETURN 0 if the glycan has more than 1 residue
		 * RETURN 1 if the glycan has only one residue and it's a basetype
		 * RETURN 2 if the glycan has only one residue and it's a repetition
		 */
		int y = 0, i=0;
		int countB = 0, countR = 0;
		Pattern p = Pattern.compile("\\d+([a-z]):");
		Matcher m; 
		do{
			m = p.matcher(r.get(i));
			if (m.find()){
				if (m.group(1).equals("b"))
					countB += 1;
				if (m.group(1).equals("r"))
					countR += 1;
			}
			i+=1;
		}
		while (i <r.size());
		if (countB == 1 && countR == 0)
			return 1;
		if (countR == 1 && countB == 0)
			return 2;	
		return y;
	}
	static int oneResidueFromString (String s, int index){
		/*
		 * RETURN 0 if the glycan has more than 1 residue
		 * RETURN 1 if the glycan has only one residue and it's a basetype
		 * RETURN 2 if the glycan has only one residue and it's a repetition
		 * index : index a partir duquel on commence a compter
		 */
		int y = 0;
		int i = index;
		int countB = 0, countR = 0, c = 0;
		String[] sp_s = s.split("\n");
		Pattern p = Pattern.compile("\\d+([a-z]):");
		Matcher m ;
		do{
			c = 0;
			m = p.matcher(sp_s[i]);
			if (m.find()){
				c = 1;
				if (m.group(1).equals("b"))
					countB += 1;
				if(m.group(1).equals("r"))
					countR += 1;	
			}
			i+=1;
		}
		while (i<sp_s.length && c==1);
		if (countB == 1 && countR == 0)
			return 1;
		if (countR == 1 && countB == 0)
			return 2;		
		return y;		
	}

	static String arrayToString (ArrayList<String> r){
		/*
		 * CHANGE AN ARRAY TO A STRING WITH \n
		 */
		String s = "";
		if (!r.isEmpty()){
			for (int i = 0; i<r.size(); i++)
				s = s + r.get(i) + "\n";
		}
		return s;
	}
	static ArrayList<String> stringToArray (String s){
		/*
		 * CHANGE A STRING TO AN ARRAY 
		 */
		ArrayList<String> a = new ArrayList<String>();
		if (!s.isEmpty()){
			String[] sp = s.split("\n");
			for (int i = 0; i<sp.length; i++)
				a.add(sp[i]);
		}
		return a;
	}

	static boolean treatUnknownLinkage(String link, String enzy, EnzymeMapping dico){
		boolean ret = false;
		String g = link.split("\\+")[0];
		String d = link.split("\\+")[1];
		if (g.equals("-1") && d.equals("-1"))
			return true;
		else{
			for (int i=0; i<dico.dic_link.get(enzy).length; i++){
//				System.out.println("g : " + g +"\td : " + d + "\tenzy : " +dico.dic_link.get(enzy)[i]);
				if (g.equals("-1"))
					if (d.equals(dico.dic_link.get(enzy)[i].split("\\+")[1]))
						return true;
				if (d.equals("-1"))
					if (g.equals(dico.dic_link.get(enzy)[i].split("\\+")[0]))
						return true;
			}
		}
		
		return ret;
	}


}
