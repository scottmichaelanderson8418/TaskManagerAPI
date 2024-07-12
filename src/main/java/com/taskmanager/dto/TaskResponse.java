package com.taskmanager.dto;

import java.util.List;

//@JsonPropertyOrder({ "content", "last", "pageNo", "pageSize", "totalElements", "totalPages" })
public class TaskResponse {
	private List<TaskDto> content;
	private boolean last;
	private int pageNo;
	private int pageSize;
	private Long totalElements;
	private int totalPages;

	public TaskResponse() {

	}

	public TaskResponse(
			List<TaskDto> content,
			int pageNo,
			int pageSize,
			Long totalElements,
			int totalPages,
			boolean last) {
		super();
		this.content = content;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.last = last;
	}

	public List<TaskDto> getConent() {
		return content;
	}

	public boolean getLast() {
		return last;
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setContent(List<TaskDto> content) {
		this.content = content;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

}
