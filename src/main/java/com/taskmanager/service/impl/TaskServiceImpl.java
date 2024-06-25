package com.taskmanager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.taskmanager.dto.TaskDto;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.exceptions.TaskNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

	private TaskRepository taskRepository;

	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	private TaskDto convertToDto(Task task) {

		TaskDto taskDto = new TaskDto();

		// ... map properties from task to dto

		taskDto.setUsername(task.getUsername());
		taskDto.setContent(task.getContent());
		taskDto.setComplete(task.isComplete());

		return taskDto;
	}

	@Override
	public TaskDto createTask(TaskDto taskDto) {
		Task task = new Task();

		task.setUsername(taskDto.getUsername());
		task.setContent(taskDto.getContent());
		task.setComplete(taskDto.isComplete());

		Task newTask = taskRepository.save(task);

		return mapToDto(newTask);
	}

	public Task createTaskUpdate(Task task, TaskDto taskUpdate) {

		task.setUsername(taskUpdate.getUsername());
		task.setContent(taskUpdate.getContent());
		task.setComplete(taskUpdate.isComplete());

		return task;
	}

	@Override
	public void deleteByTaskId(int id) {
		Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task could not be deleted"));

		taskRepository.delete(task);
	}

	public List<TaskDto> findAllProducts() {
		List<Task> taskList = taskRepository.findAll();
		return taskList.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	public List<TaskDto> findAllProductsWithSorting(String field) {
		List<Task> taskList = taskRepository.findAll(Sort.by(Sort.Direction.ASC, field));
		return taskList.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public Page<TaskDto> findTasksWithPaginationAndSorting(int offset, int pageSize, String field) {
		Page<Task> drawings = taskRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));

		// Convert Page<Task> to List<TaskDto>
		List<TaskDto> dtos = drawings.stream().map(this::convertToDto).collect(Collectors.toList());

		// Create a new Page<TaskDto> with the converted DTOs and original pagination
		// info

		return new PageImpl<>(dtos, drawings.getPageable(), drawings.getTotalElements());
	}

	@Override
	public TaskResponse getAllTasks(int pageNo, int pageSize) {

		PageRequest pageable = PageRequest.of(pageNo, pageSize);

		Page<Task> task = taskRepository.findAll(pageable);

		// this "task.getContent()" will get everything in the page
		List<Task> taskList = task.getContent();

		List<TaskDto> content = taskList.stream().map(this::mapToDto).collect(Collectors.toList());

		TaskResponse taskResponse = new TaskResponse();

		taskResponse.setContent(content);
		taskResponse.setPageNo(task.getNumber());
		taskResponse.setPageSize(task.getSize());
		taskResponse.setTotalElements(task.getTotalElements());
		taskResponse.setTotalPages(task.getTotalPages());
		taskResponse.setLast(task.isLast());

		return taskResponse;
	}

	@Override
	public TaskDto getTaskById(int id) {
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new TaskNotFoundException("Print drawing could not be found :("));
		return mapToDto(task);
	}

	private TaskDto mapToDto(Task task) {
		TaskDto taskDto = new TaskDto();

		return taskDto;
	}

	@Override
	public TaskDto updateTask(TaskDto taskUpdate, int id) throws TaskNotFoundException {

		try {
			// Find the Task entity by ID or throw an exception if not found
			Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task could not be updated"));

			// Create an updated Task entity
			Task updatedTask = createTaskUpdate(task, taskUpdate);

			// Save the updated Task entity
			Task newTask = taskRepository.save(updatedTask);

			// Map the updated Task entity to DTO and return it
			return mapToDto(newTask);

		} catch (TaskNotFoundException pde) {
			// Re-throw TaskNotFoundException with a more specific message
			throw new TaskNotFoundException("Print drawing could not be updated--OOp");
		}
	}

	@Override
	public List<TaskDto> findAllTasksWithSorting(String field) {
		List<Task> taskList = taskRepository.findAll(Sort.by(Sort.Direction.ASC, field));
		return taskList.stream().map(this::mapToDto).collect(Collectors.toList());
	}

}
