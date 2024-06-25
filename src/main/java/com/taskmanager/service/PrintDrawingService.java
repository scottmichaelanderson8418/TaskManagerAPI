package com.taskmanager.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.taskmanager.api.application.dto.PrintDrawingDto;
import com.taskmanager.api.application.dto.PrintDrawingResponse;


public interface PrintDrawingService {

	
	public PrintDrawingDto createPrint(PrintDrawingDto printDto);

	
	public PrintDrawingResponse getAllPrints(int pageNo, int pageSize);

	
	public PrintDrawingDto getPrintById(int id);

	
	public PrintDrawingDto updatePrint(PrintDrawingDto printDrawingUpdate, int id);

	
	public void deleteByPrintId(int id);

	
	public List<PrintDrawingDto> findAllProducts();

	
	public List<PrintDrawingDto> findAllProductsWithSorting(String field);

	
	public Page<PrintDrawingDto> findProductsWithPaginationAndSorting(int offset, int pageSize,
			String field);

	
	public PrintDrawingResponse findDiameterWithPaginationAndSorting(int pageNo, int pageSize,
			String field, float diameterMinValue, float diameterMaxValue, float faceLengthMinValue,
			float faceLengthMaxValue);
}
