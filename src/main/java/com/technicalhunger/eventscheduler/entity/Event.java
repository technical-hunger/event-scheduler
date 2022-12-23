package com.technicalhunger.eventscheduler.entity;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author shahnavaz.saiyad
 *
 */
public class Event {

	private int id;
	
	private Date time;
	private String title;
	private String description;
	private boolean isDeleted;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
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
	
	
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	@Override
	public String toString() {
		return "Event [id=" + id + ", time=" + time + ", title=" + title + ", description=" + description + "]";
	}
	
}
