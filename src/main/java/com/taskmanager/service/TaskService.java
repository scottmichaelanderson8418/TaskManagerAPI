package com.taskmanager.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.taskmanager.dto.TaskDto;
import com.taskmanager.dto.TaskResponse;

public interface TaskService {

	public TaskDto createTask(TaskDto taskDto);

	public TaskResponse getAllTasks(int pageNo, int pageSize);

	public TaskDto getTaskById(int id);

	public TaskDto updateTask(TaskDto taskUpdate, int id);

	public void deleteByTaskId(int id);

	public List<TaskDto> findAllTasksWithSorting(String field);

	public Page<TaskDto> findTasksWithPaginationAndSorting(int offset, int pageSize, String field);

}
