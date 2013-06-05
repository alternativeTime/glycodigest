package main;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import static main.UsualFunctions.*;

public class Sugar {
/*
 * main class for the digestion. when the Submit Button is pressed, it calls Sugar. it recovers the input entry and calls SugarTreatment class
 */
		private String enter_sugar;
		private String[] sugartab;
		private String sugar;
		private ArrayList<String> enz;
		private Hashtable<String, Integer> section = new Hashtable <String, Integer> ();
		private EnzymeMapping dico;
		private String rep;
		private String allpoly;
		String glycan;
		ArrayList<String> glycanInput;
		ArrayList<String> glycanOutput;
		boolean exceptFormat;
		boolean pbDigestion;
		boolean unklink;
		boolean iupac;

		//CONSTRUCTOR AND INITIALIZATION
		public Sugar(String enter_sugar, ArrayList<String> enz, boolean unklink, boolean iupac){
			this.enter_sugar = enter_sugar;
			this.enz = enz;
			this.unklink = unklink;
			this.iupac = iupac;
			initSugar();
		}	
		public void initSugar(){
			dico = new EnzymeMapping();
			initDico(dico);
			glycanInput = new ArrayList<String>();
			glycanOutput = new ArrayList<String>();
			sugar = "";
			rep = "";
			glycan = "";
			allpoly = "";
			exceptFormat = false;
			pbDigestion = false;
			
		}
		public static void initDico (EnzymeMapping dico){
			/*
			 * INITIALIZE THE DICTIONNARY AND CREATE THE CORRESPONDANCE BETWEEN ENZYMES, RESIDUES AND LINK
			 */
			dico.createDic_link();
			dico.createDic_res();
			dico.createFamily();
		}

//OTHERS FUNCTIONS
		public boolean dataLoading (){
			/* 
			 * CREATE AN ARRAY WITH ALL THE GLYCANS (1 OR +) 
			 * from the textArea in the GUI
			 */		
			boolean ok = true;
			if (!iupac){
				ok = false;
				String[] temp = enter_sugar.split("\n");	
				for (int i=0; i<temp.length; i++){
					boolean blank = temp[i].isEmpty();
					boolean sect = temp[i].equals("RES") | temp[i].equals("LIN") | temp[i].equals("UND") | temp[i].equals("ALT") |temp[i].equals("REP");
					boolean numb = false;
					for (int j=1; j<10; j++)
						if (temp[i].startsWith(String.valueOf(j)))
								 numb = true;
					if (blank || sect || numb)
						ok = true;
				}
			}
			sugartab = enter_sugar.trim().split("\n\n+");
			return ok;
		}			
		public void sectionCreation (){
			/*
			 * FILL THE DICTIONNARY 'SECTION' WITH 0 OR 1 IF THE DIFFERENT SECTIONS EXIST OR NOT
			 * REP : REPETITION
			 * ALT : ALTERNATIVE
			 * UND : UNDERDETERMINED
			 */
			section.put("LIN", 0);
			section.put("REP", 0);
			section.put("ALT", 0);
			section.put("UND", 0);
			if (sugar.contains("LIN\n"))
				section.put("LIN", 1);
			if (sugar.contains("REP\n"))
				section.put("REP", 1);
			if (sugar.contains("ALT\n"))
				section.put("ALT", 1);
			if (sugar.contains("UND\n"))
				section.put("UND", 1);
		}


//FUNCTION FOR REPETITION SECTION
		public void treat_non_terminal_rep(SugarTreatment ts, ArrayList<String> enz1){
			/*
			 * TREAT THE NON-TERMINAL REPETITION PART : 
			 * if change : get the rep and set it to SugarTreatment
			 * if nochange : no change !
			 */
			SectionRep rep_sect = new SectionRep(sugar, dico, enz1, section, ts.getRep(), unklink);
			boolean nochange = rep_sect.treatNonterminalRep();
			if (!nochange){
				rep = rep_sect.getRep();
				ts.setRep(rep);
			}
		}
		
//MAIN
		public void glycanFormatage(ArrayList<String> enz1){
			boolean nochange = false;
			if (sugar.equals(glycan))
				nochange = true;
			String enzList = "enzyme : " + enz1.get(0);
			for (int j = 1; j<enz1.size(); j++)
				enzList = enzList + " + " + enz1.get(j);
			allpoly = allpoly + "\n" +enzList;
			if (nochange)
				allpoly = allpoly + "\nno change";
			//caution : sometimes, there a change (remove 1 repetition but the numb is unknown...)
			allpoly = allpoly + "\n" + glycan + "\n" ;
			glycanOutput.add(glycan);
			enz1.clear();
		}
	
		public String glycanTreatment() throws IOException{
		/*
		 * TREAT ALL THE GLYCANS OF THE INPUT AND FILL THE STRING 'allpoly' CONTAINING ALL POSSIBLE STRUCTURES
		 */
			//load data from the input frame
			boolean goodFormat = dataLoading();
			if (goodFormat){
				ArrayList<String> enz1 = new ArrayList<String>();
				//treat each glycan one by one
				for (int i = 0; i<sugartab.length; i++){
					if (i>0)
						allpoly = allpoly + "\n\n";
					if (iupac)
						sugar = "RES" + sugartab[i].split("RES",2)[1];
					else
						sugar = sugartab[i];
					
					sugar = sugar.trim();
					glycanInput.add(sugar);
					//create section of glycan : lin, rep, und 
					sectionCreation();
					try{
						SugarTreatment sugtreat = new SugarTreatment (sugar, dico, enz1, section, unklink);
						sugtreat.createParts();

						allpoly = allpoly + "GLYCAN N° " + (i+1) + "\n\nUNDIGESTED\n" + sugar + "\n";
						glycanOutput.add(sugar);
						int e = 1;
						while (e <= enz.size()){ 
							for (int en = 0; en<e; en++)
								enz1.add(enz.get(en));
												
							System.out.println(" \n------------------enz1 : " + enz1 + "\nsugar : \n" + sugar );
							int oneres = oneResidueFromString(sugar, 1);
							System.out.println("oneres : " + oneres);
							if (oneres != 1){
								if (section.get("REP") == 1)
									treat_non_terminal_rep(sugtreat, enz1);	
								glycan = sugtreat.glycanSubTreatment();
							}
							e++;
							
							glycanFormatage(enz1);
							sugar = glycan;
							sectionCreation();
							sugtreat = new SugarTreatment (sugar, dico, enz1, section, unklink);
							sugtreat.createParts();
						}	
					}
					catch (Exception ex){
						pbDigestion = true;
					}	
				}
			}
			else
				exceptFormat = true;
			
			return allpoly;
		}
	

//GETTERS
		public ArrayList<String> getGlycanInput() {
			return glycanInput;
		}
		public ArrayList<String> getGlycanOutput() {
			return glycanOutput;
		}
		public boolean isExceptFormat() {
			return exceptFormat;
		}
		public boolean isPbDigestion() {
			return pbDigestion;
		}
}
