package main;

import java.util.Hashtable;

public class EnzymeMapping {
/*
 * create hashmap to store matching between enzymes and glycoCT format
 */
	Hashtable<String, String> dic_res = new Hashtable<String, String> ();				//map with ENZYME - RESIDUE
	Hashtable<String, String[]> dic_link = new Hashtable<String,String[]>();			//map with ENZYME - LINK
	Hashtable<String, Integer> family = new Hashtable<String,Integer> ();

	public EnzymeMapping(){
		
	}
	String[] enz_list = {"JBM", "BTG", "SPG", "CBG", "BKF", "AMF", "XMF", "GUH", "JBH", "ABS", "NAN1"};

	public void createFamily (){
		/*
		 * FILL THE DICTIONARY 'family'
		 * 1 if residue is only 1 basetype
		 * 2 if residue is 1 basetype + 1 substituent
		 */
		family.put("JBM", 1);
		family.put("BTG", 1);
		family.put("SPG", 1);
		family.put("CBG", 1);
		family.put("BKF", 1);
		family.put("AMF", 1);
		family.put("XMF", 1);
		family.put("GUH", 2);
		family.put("JBH", 2);
		family.put("ABS", 2);
		family.put("NAN1", 2);
	}
	public void createDic_res (){
		/*
		 * FILL THE DICTIONARY 'dic_res' WITH RESIDUES CORRESPONDING TO THE ENZYMES
		 */
		dic_res.put("JBM", "\\d+b:a-[dlx]man-HEX-1:5");
		dic_res.put("BTG", "\\d+b:b-[dlx]gal-HEX-1:5");	
		dic_res.put("SPG", "\\d+b:b-[dlx]gal-HEX-1:5");
		dic_res.put("CBG", "\\d+b:a-[dlx]gal-HEX-1:5");
		dic_res.put("BKF", "\\d+b:a-[dlx]gal-HEX-1:5\\|6:d");
		dic_res.put("AMF", "\\d+b:a-[dlx]gal-HEX-1:5\\|6:d");
		dic_res.put("XMF", "\\d+b:a-[dlx]gal-HEX-1:5\\|6:d");
		dic_res.put("GUH", "\\d+b:b-[dlx]glc-HEX-1:5\n\\d+s:n-acetyl");
		dic_res.put("JBH", "\\d+b:b-[dlx]glc-HEX-1:5|\\d+b:b-[dlx]gal-HEX-1:5\n\\d+s:n-acetyl");     
		dic_res.put("ABS","\\d+b:a-[dlx]gro-dgal-NON-2:6\\|1:a\\|2:keto\\|3:d\n\\d+s:n-acetyl|\\d+s:n-glycolyl");
		dic_res.put("NAN1","\\d+b:a-[dlx]gro-dgal-NON-2:6\\|1:a\\|2:keto\\|3:d\n\\d+s:n-acetyl|\\d+s:n-glycolyl");
	}
	public void createDic_link (){
		/*
		 * FILL THE DICTIONARY 'dic_link' WITH LINKAGES CORRESPONDING TO THE ENZYMES
		 */
		String[] link1 = {"2+1", "3+1", "6+1"};
		String[] link2 = {"3+1","4+1"};
		String[] link3 = {"4+1"};
		String[] link4 = {"3+1","4+1", "6+1"};
		String[] link5 = {"2+1","3+1","4+1","6+1"};
		String[] link6 = {"2+1"};
		String[] link7 = {"3+2","6+2","8+2"};
		String[] link8 = {"3+2"};
		
		dic_link.put("JBM", link1);
		dic_link.put("BTG",link2);
		dic_link.put("SPG", link3);
		dic_link.put("CBG", link4);
		dic_link.put("BKF", link5);
		dic_link.put("AMF", link2);
		dic_link.put("XMF", link6);
		dic_link.put("GUH", link5);
		dic_link.put("JBH", link5);
		dic_link.put("ABS", link7);
		dic_link.put("NAN1", link8);
	}

	public String[] getEnz_list() {
		return enz_list;
	}


}
