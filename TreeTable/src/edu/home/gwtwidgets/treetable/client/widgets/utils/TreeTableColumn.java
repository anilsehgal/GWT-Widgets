package edu.home.gwtwidgets.treetable.client.widgets.utils;

import com.google.gwt.user.cellview.client.Header;

import edu.home.gwtwidgets.treetable.client.widgets.TreeTable;

/**
 * The {@link TreeTable} column definition class, defines the column behavior
 * 
 * @author Anil Sehgal
 */
public class TreeTableColumn {
	
	/**
	 * The TreeTableColumnType enum defines all the column types available with TreeTable
	 * @author Anil Sehgal
	 *
	 */
	public enum TreeTableColumnType {
		
		/**
		 * The {@link TreeTable} column type which renders:
		 * read-only -> Text
		 * editable  -> <input type="text" value="Some Text"></input>
		 */
		TEXT,
		/**
		 * The {@link TreeTable} column type which renders:
		 * read-only -> Text
		 * editable  -> <textarea rows="6" columns="30">Some Text</textarea>
		 */
		TEXTAREA,
		/**
		 * The {@link TreeTable} column type which renders:
		 * read-only -> Read Only Date
		 * editable  -> date picker
		 */
		DATE,
		/**
		 * The {@link TreeTable} column type which renders:
		 * read-only -> 20.0(number format)
		 * editable  -> <input type="text" value="20"></input>
		 */
		NUMBER,
		/**
		 * The {@link TreeTable} column type which renders:
		 * read-only -> <input type="checkbox" checked="true"  disabled="true"></input>
		 * editable  -> <input type="checkbox" checked="true"></input>
		 */
		BOOLEAN,
		/**
		 * The {@link TreeTable} column type which renders:
		 * read-only -> the abstract cell implementation provided by the user
		 * TODO Anil - Provide the editable implementation for this
		 */
		CUSTOM
	}
	
	/**
	 * The type of current column
	 */
	private TreeTableColumnType treeTableColumnType;
	
	/**
	 * If the current column can be edited
	 */
	private boolean isEditable;
	
	/**
	 * if the field is to be always shown in editor
	 * 
	 * TODO Anil - remove this, this is not needed
	 * @deprecated
	 */
	private boolean showInEditor;
	
	/**
	 * Column header string value
	 * 
	 * TODO Anil - Provide the implementation of the {@link Header} class
	 */
	private String columnHeader;
	
	/**
	 * Provides the value for the column, the value would be rendered
	 */
	private TreeTableColumnValueProvider columnValueProvider;
	
	/**
	 * The value updater, called once the value is commited
	 */
	private TreeTableColumnValueUpdater columnValueUpdater;
	
	/**
	 * Custom Cell renderer called in case of {@link TreeTableColumnType}.CUSTOM 
	 */
	private TreeTableColumnCustomCellRenderer treeTableColumnCustomCellRenderer;
	
	/**
	 * TreeTableColumn constructor 
	 * @param treeTableColumnType the type of current column
	 * @param isEditable if the current column is editable
	 * @param showInEditor if the volumn will always be shown in editpr
	 * @param columnHeader header of the column
	 * @param columnValueProvider the value provider for the column
	 * @param columnValueUpdater the value updator for the column
	 */
	public TreeTableColumn(TreeTableColumnType treeTableColumnType,
			boolean isEditable, boolean showInEditor, String columnHeader,
			TreeTableColumnValueProvider columnValueProvider,
			TreeTableColumnValueUpdater columnValueUpdater) {
		super();
		this.treeTableColumnType = treeTableColumnType;
		this.isEditable = isEditable;
		this.showInEditor = showInEditor;
		this.columnHeader = columnHeader;
		this.columnValueProvider = columnValueProvider;
		this.columnValueUpdater = columnValueUpdater;
	}

	/**
	 * getter for TreeTableColumnType
	 * @return TreeTableColumnType
	 */
	public TreeTableColumnType getTreeTableColumnType() {
		return treeTableColumnType;
	}

	/**
	 * getter for isEditable
	 * @return isEditable
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * getter for showInEditor
	 * @return showInEditor
	 */
	public boolean isShowInEditor() {
		return showInEditor;
	}

	/**
	 * getter for columnValueProvider
	 * @return columnValueProvider
	 */
	public TreeTableColumnValueProvider getColumnValueProvider() {
		return columnValueProvider;
	}

	/**
	 * getter for columnHeader
	 * @return columnHeader
	 */
	public String getColumnHeader() {
		return columnHeader;
	}

	/**
	 * getter for treeTableColumnCustomCellRenderer
	 * @return treeTableColumnCustomCellRenderer
	 */
	public TreeTableColumnCustomCellRenderer getTreeTableColumnCustomCellRenderer() {
		return treeTableColumnCustomCellRenderer;
	}

	/**
	 * setter for treeTableColumnCustomCellRenderer
	 * @return treeTableColumnCustomCellRenderer
	 */
	public void setTreeTableColumnCustomCellRenderer(
			TreeTableColumnCustomCellRenderer treeTableColumnCustomCellRenderer) {
		this.treeTableColumnCustomCellRenderer = treeTableColumnCustomCellRenderer;
	}

	/**
	 * getter for columnValueUpdater
	 * @return columnValueUpdater
	 */
	public TreeTableColumnValueUpdater getColumnValueUpdater() {
		return columnValueUpdater;
	}
	
	
}
