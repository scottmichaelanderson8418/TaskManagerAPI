package com.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "content", "complete" })
public class TaskDto {

	private boolean complete;
	private String content;
	private Integer id;
	private String username;

	public String getContent() {
		return content;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return username;
	}

	public String getUsername() {
		return username;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.username = name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}