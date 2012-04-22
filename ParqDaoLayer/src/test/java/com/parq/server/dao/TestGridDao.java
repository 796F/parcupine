package com.parq.server.dao;

import java.util.List;

import com.parq.server.dao.model.object.Grid;

import junit.framework.TestCase;

public class TestGridDao extends TestCase {
	
	public void testGetAllCompleteGridHiarchy() {
		GridDao dao = new GridDao();
		
		List<Grid> grids = dao.getAllCompleteGridHiarchy();
		
		assertNotNull(grids);
	}

}
