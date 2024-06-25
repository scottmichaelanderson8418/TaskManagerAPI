
package com.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.taskmanager.api.application.model.PrintDrawing;


@Repository // Mark this interface as a Spring Data repository
public interface TasksRepository extends JpaRepository<PrintDrawing, Integer> {



	
	void save(String string);
}
