package model;

public class Note {
	private String title;
	private String text;
	private long id;
	
	public Note(long id, String title, String text){
		this.id = id;
		this.text = text;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
