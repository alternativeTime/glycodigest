package main;


import java.util.ArrayList;


public class ResidueTerm {
/*
 * ResidueTerm : Objects containing in 'tab_res_term' within "CreateTabResTerm" class
 */

	private ArrayList<String> r;
	private ArrayList<String> l;
	String enz;
	
	public ResidueTerm( ArrayList<String> r,  ArrayList<String> l, String enz){
		this.r = r;
		this.l = l;
		this.enz = enz;	
	}
	
	public ArrayList<String> getR() {
		return r;
	}
	public ArrayList<String> getL() {
		return l;
	}
	public String getEnz() {
		return enz;
	}
	public void setEnz(String enz) {
		this.enz = enz;
	}
	public void setR(ArrayList<String> r) {
		this.r = r;
	}
	public void setL(ArrayList<String> l) {
		this.l = l;
	}
}

