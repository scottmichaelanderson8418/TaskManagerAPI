package com.taskmanager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.taskmanager.dto.TaskDto;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.exceptions.MyUserNotFoundException;
import com.taskmanager.exceptions.TaskNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {
	Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class.getName());

	private TaskRepository taskRepository;

	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	private TaskDto convertToDto(Task task) {
		logger.trace("ENTERED……………………………………convertToDto()");

		TaskDto taskDto = new TaskDto();

		// ... map properties from task to dto

		taskDto.setUsername(task.getUsername());
		taskDto.setContent(task.getContent());
		taskDto.setComplete(task.isComplete());
		logger.trace("EXITED……………………………………convertToDto()");

		return taskDto;
	}

	@Override
	public TaskDto createTask(TaskDto taskDto) {
		logger.trace("Entered......createTask() ");

		Task task = new Task();

		task.setUsername(taskDto.getUsername());
		task.setContent(taskDto.getContent());
		task.setComplete(taskDto.isComplete());

		Task newTask = taskRepository.save(task);
		logger.trace("Exited......createTask() ");
		return mapToDto(newTask);
	}

	public Task createTaskUpdate(Task task, TaskDto taskUpdate) {
		logger.trace("Entered......createTaskUpdate() ");

		task.setUsername(taskUpdate.getUsername());
		task.setContent(taskUpdate.getContent());
		task.setComplete(taskUpdate.isComplete());
		logger.trace("Exited......createTaskUpdate() ");
		return task;
	}

	@Override
	public void deleteByTaskId(int id) {
		logger.trace("Entered......deleteByTaskId() ");

		Task task = taskRepository.findById(id).orElseThrow(() -> new MyUserNotFoundException("MyUser could not be deleted..."));
		logger.trace("Exited......deleteByTaskId() ");
		taskRepository.deleteById(id);

	}

	public List<TaskDto> findAllProducts() {
		logger.trace("Entered......findAllProducts() ");

		List<Task> taskList = taskRepository.findAll();
		logger.trace("Exited......findAllProducts() ");
		return taskList.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public TaskResponse getAllTasks(int pageNo, int pageSize) {
		logger.trace("Entered...........................getAllTasks()");

		PageRequest pageable = PageRequest.of(pageNo, pageSize);

		Page<Task> task = taskRepository.findAll(pageable);

		// this "task.getContent()" will get everything in the page
		List<Task> taskList = task.getContent();

		List<TaskDto> myTaskDtoList = taskList.stream().map(this::mapToDto).collect(Collectors.toList());

		TaskResponse taskResponse = new TaskResponse();

		taskResponse.setContent(myTaskDtoList);
		taskResponse.setPageNo(task.getNumber());
		taskResponse.setPageSize(task.getSize());
		taskResponse.setTotalElements(task.getTotalElements());
		taskResponse.setTotalPages(task.getTotalPages());
		taskResponse.setLast(task.isLast());
		logger.trace("Exited...........................getAllTasks()");
		return taskResponse;
	}

	@Override
	public TaskDto getTaskById(int id) {
		logger.trace("Entered.................getTaskById()");

		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new TaskNotFoundException("Print drawing could not be found :("));
		logger.trace("Exited...........................getTaskById()");
		return mapToDto(task);
	}

	private TaskDto mapToDto(Task task) {
		logger.trace("Entered...........................mapToDto()");

		TaskDto taskDto = new TaskDto();

		taskDto.setId(task.getId());
		taskDto.setComplete(task.isComplete());
		taskDto.setContent(task.getContent());
		taskDto.setUsername(task.getUsername());

		logger.trace("Exited...........................mapToDto()");
		return taskDto;
	}

	@Override
	public TaskDto updateTask(TaskDto taskUpdate, int id) throws TaskNotFoundException {
		logger.trace("Entered...........................updateTask()");

		try {
			// Find the Task entity by ID or throw an exception if not found
			Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task could not be updated"));

			// Create an updated Task entity
			Task updatedTask = createTaskUpdate(task, taskUpdate);

			// Save the updated Task entity
			Task newTask = taskRepository.save(updatedTask);

			// Map the updated Task entity to DTO and return it
			logger.trace("Exited...........................updateTask()");
			return mapToDto(newTask);

		} catch (TaskNotFoundException pde) {
			logger.trace("Exited...........................updateTask()");
			// Re-throw TaskNotFoundException with a more specific message
			throw new TaskNotFoundException("Task  could not be updated--OOp");
		}
	}

}
