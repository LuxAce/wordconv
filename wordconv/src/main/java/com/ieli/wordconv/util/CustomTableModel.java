package com.ieli.wordconv.util;

import javax.swing.table.DefaultTableModel;

public class CustomTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	public CustomTableModel(Object[][] data, Object[] columns) {
		super(data, columns);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
