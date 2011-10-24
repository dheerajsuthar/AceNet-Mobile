package com.acenetcampus.mobile;

public class ClassUpdate {
	private String name,image,description,follow;
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setImage(String image){
		this.image = image;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public void setFollow(String follow){
		this.follow = follow;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getImage(){
		return this.image;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String getFollow(){
		return this.follow;
	}

}
