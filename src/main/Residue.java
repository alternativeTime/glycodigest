package main;


import java.util.ArrayList;

public class Residue {
	/*
	 * Residue : objects containing in 'tab_res' within the "CreateTabResTerm" class
	 */
	private ArrayList<String> r;
	private String enz;
	
	public Residue( ArrayList<String> r, String enz){
		this.r = r;
		this.enz = enz;
		
	}
	
	public ArrayList<String> getR() {
		return r;
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

}
