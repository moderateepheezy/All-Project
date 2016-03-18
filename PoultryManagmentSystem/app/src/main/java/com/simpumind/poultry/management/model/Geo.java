package com.simpumind.poultry.management.model;

public class Geo {
	private long id;
	private String name;
	private double malePrice ;
	private double femalePrice;
	private String trade;
	
	
	@Override
	public String toString() {
				return "Name= "+name +"\nMale Price= "+malePrice+"\nfemalePrice= " +femalePrice + "\n trade="+trade;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getMalePrice() {
		return malePrice;
	}


	public void setMalePrice(double malePrice) {
		this.malePrice = malePrice;
	}


	public double getFemalePrice() {
		return femalePrice;
	}


	public void setFemalePrice(double femalePrice) {
		this.femalePrice = femalePrice;
	}


	public String getTrade() {
		return trade;
	}


	public void setTrade(String trade) {
		this.trade = trade;
	}

}
