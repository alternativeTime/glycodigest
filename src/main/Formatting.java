package main;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static main.UsualFunctions.*;

public class Formatting {
/*
 * format digested sequences 
 */
	private ArrayList<String> res;			//Array : first residues
	private ArrayList<String> lin;			//Array : first linkages
	private String rep;						//rep
	private String numbofrep;
	boolean rep_1;
	ArrayList<String> all = new ArrayList<String>();
	private String und;

	private ArrayList<String> res1;			//Array : residues once format is done
	private ArrayList<String> lin1;			//Array : linkages once format is done
	private String rep1;					//rep once format is done
	private String und1;
	
	String poly;							
	private Hashtable<String, String> sub;



//* CONSTRUCTOR AND INITIALIZATION
	public Formatting (ArrayList<String> res, ArrayList<String> lin, String rep, String und){
		this.res = res;
		this.lin = lin;
		this.rep = rep;
		this.und = und;
		initFormatage();
	}		
	public Formatting (ArrayList<String> res, ArrayList<String> lin, String rep, String und, boolean rep_1){
		this.res = res;
		this.lin = lin;
		this.rep = rep;
		this.und = und;
		this.rep_1 = rep_1;
		initFormatage();
	}	
	public void initFormatage (){
		sub = new Hashtable<String, String>();
		res1 = new ArrayList <String>();
		lin1 = new ArrayList <String>();
		rep1 = "";
		und1 = "";
		poly = "";
		numbofrep = "";
	}
	
	//* CLASS FUNCTIONS
	public ArrayList<String> treatRes (ArrayList<String> r, int k){
		/*
		 * REFORMATE THE RESIDUES 
		 * RETURN A NEW LIST WITH IN-ORDER RESIDUES
		 */
		ArrayList<String> r1 = new ArrayList<String>();
		String pa;
		for (int i = 0; i<r.size(); i++){
			pa = patt(r.get(i));
			sub.put(pa, String.valueOf(Integer.parseInt(pa)-k));		
			r1.add(String.valueOf(k) + r.get(i).substring(pa.length()));
			k++;
		}
		return r1;
	}			
	public ArrayList<String> treatLin(ArrayList<String> l, int k){
		/*
		 * FORMATE THE LINKAGES AND RETURN A NEW LIST WITH IN-ORDER LINKAGES
		 */
		ArrayList<String> l1 = new ArrayList<String> ();
		String temp1;
		String temp2;	
		for (int i = 0; i<l.size(); i++){
			temp1 = "";
			temp2 = "";
			Pattern p = Pattern.compile("\\d+:(\\d+)(.\\(.+\\))(\\d+)(.)");
			Matcher m = p.matcher(l.get(i));
			if (m.find()){
				String resid1 = m.group(1);
				String link1 = m.group(2);
				String resid2 = m.group(3);
				String rest = m.group(4);
				Enumeration<String> e = sub.keys();

				while (e.hasMoreElements()){
					Object key = e.nextElement();		
					if (key.toString().equals(resid1))
						temp1 = resid1;
					if (key.toString().equals(resid2))
						temp2=resid2;
				}
				if (!temp1.isEmpty())
					resid1 = String.valueOf(Integer.parseInt(temp1)-Integer.parseInt(sub.get(temp1)));
				if (!temp2.isEmpty())
					resid2 = String.valueOf(Integer.parseInt(temp2)-Integer.parseInt(sub.get(temp2)));
				l1.add(String.valueOf(i+k) + ":"+ resid1 + link1 + resid2 + rest);
			}
		}
		return l1;
		
	}
	
	public String createResLinFromRepUnd (ArrayList<String> r, ArrayList<String> l, String cas, String var){
		String[] r1;
		String[] temp = var.split("RES", 2);
		String title = temp[0];
		if (temp[1].contains("LIN")){
			String[] sp = temp[1].split("LIN");
			r1 = sp[0].split("\n");
			String[] l1 = sp[1].split("\n");
			for (int i = 1; i<l1.length; i++)
				l.add(l1[i]);
		}
		else
			r1 = temp[1].split("\n");
		
		for (int i=1; i<r1.length; i++)
			r.add(r1[i]);
		
		return title;
	}	
	public void treatNumbOfRep(){
		/*
		 * SUBSTRACT 1 OF THE NUMBER OF REPETITION, IF THIS IS KNOWN
		 */
		if (!numbofrep.equals("-1--1")){
			Pattern p = Pattern.compile("(\\d+)-(\\d+)");
			Matcher m = p.matcher(numbofrep);
			if (m.find()){
				String temp = String.valueOf(Integer.parseInt(m.group(1))-1) + "-" + String.valueOf(Integer.parseInt(m.group(2))-1);
				numbofrep = temp;
			}
		}
	}	
	public String treatTitleRep(int rr, String title){
		/* 
		 * REFORMATE THE TITLE OF THE REP 
		 */
		String newtitle = "";
		Pattern p = Pattern.compile("\\d+:(\\d+)([a-z]\\(.+\\))(\\d+)([a-z]=)(.*)");
		Matcher m = p.matcher(title);
		if (m.find()){
			numbofrep = m.group(5);
			if (rep_1)
				treatNumbOfRep();
			String resid1 = String.valueOf(Integer.parseInt(m.group(1))-Integer.parseInt(sub.get(m.group(1))));
			String resid2 = String.valueOf(Integer.parseInt(m.group(3))-Integer.parseInt(sub.get(m.group(3))));
		
			newtitle = "REP" + String.valueOf(rr) + ":" + resid1 + m.group(2) + resid2 + m.group(4) + numbofrep;
		}
		return newtitle;
	}
	public String treatTitleUnd(int i, String title) {
		String newtitle = "";
		newtitle = "UND" + String.valueOf(i) + title.substring(1);
		return newtitle.trim();
	}	
	public void clear(String temp, ArrayList<String> r1, ArrayList<String> l1, ArrayList<String> r, ArrayList<String> l, String title){
		temp = "";
		title = "";
		r1.clear();
		l1.clear();
		l.clear();
		r.clear();
	}	
	public String treatRepUnd(String cas){
		String new1 = "", var = "", title = "";
		if (cas.equals("rep"))
			var = rep;
		else 
			var = und;
		
		ArrayList<String> r = new ArrayList<String>();
		ArrayList<String> l = new ArrayList<String>();
		ArrayList<String> r1 = new ArrayList<String>();
		ArrayList<String> l1 = new ArrayList<String>();
		int indexr = res1.size();
		int indexl = lin1.size();
		String temp = "";
		String[] sp = var.split(cas.toUpperCase());
		for (int i=1; i<sp.length; i++){
			if (!sp[i].isEmpty()){
				indexr = indexr + r.size();
				indexl = indexl + l.size();

				clear(temp, r1, l1, r,l, title);
				var = sp[i];			
				title = createResLinFromRepUnd(r,l, cas, sp[i]);
				r1 = treatRes(r,indexr+1);
				if (!l.isEmpty())
					l1 = treatLin(l,indexl+1);
				
				if (cas.equals("rep"))
					title = treatTitleRep(i, title);
				else 
					title = treatTitleUnd(i, title);

				temp =  title + "\nRES\n";
				for (int j = 0; j<r1.size(); j++)
					temp =temp + r1.get(j) + "\n";
				if (!l1.isEmpty()){
					temp = temp + "LIN" + "\n";
					for (int j=0; j<l1.size(); j++)
						temp = temp + l1.get(j) + "\n";
				}
				new1 = new1 + temp;			
			}
		}
		return new1;
	}



	public void subFormat(){

		all.addAll(res1);
		if (!lin1.isEmpty()){
			all.add("LIN");
			all.addAll(lin1);
			if (!rep1.isEmpty()){
				all.add("REP");
				all.add(rep1);
			}
		}
		if (lin1.isEmpty() && !rep1.isEmpty()){
				all.add("REP");
				all.add(rep1);
		}
		if (!und1.isEmpty()){
			all.add("UND");
			all.add(und1);
		}
			
		poly = "RES";
		for (int i = 0; i<all.size(); i++)
			poly = poly + "\n" + all.get(i);

	}
	public String format (){	
		/*
		 * FORMATE THE GLYCAN
		 */
		System.out.println("##############FORMATAGE#################\nre :" + res + "\nlin : "+lin + "\nrep:" + rep.trim() + "\nund : " + und);	
		all.clear();
		res1 = treatRes(res,1);
		if (!lin.isEmpty())
			lin1 = treatLin(lin,1);
		if (!rep.isEmpty())
			rep1 = treatRepUnd("rep");		
		else
			rep1 = rep;
		if (!und.isEmpty())
			und1 = treatRepUnd("und");
	
		subFormat();
		System.out.println("######################################");
		return poly.trim();
		}
		
	
//* GETTERS
	public ArrayList<String> getRes1() {
		return res1;
	}
	public ArrayList<String> getLin1() {
		return lin1;
	}


}

