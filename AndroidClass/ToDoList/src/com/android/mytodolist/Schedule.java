package com.android.mytodolist;

public class Schedule {
	private String title;
	private String description;
	private String date;
	private String time;
	private long id;
	
	public Schedule(long id, String title, String description, String date, String time){
		this.id = id;
		this.description = description;
		this.title = title;
		this.date = date;
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate(){
		return date;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	public String getTime(){
		return time;
	}
	
	public void setTime(String time){
		this.time = time;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
