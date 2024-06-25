
package com.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanager.model.Task;

@Repository // Mark this interface as a Spring Data repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

	void save(String string);
}
