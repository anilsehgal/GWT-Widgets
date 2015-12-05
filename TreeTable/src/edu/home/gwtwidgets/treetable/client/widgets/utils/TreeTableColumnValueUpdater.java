package edu.home.gwtwidgets.treetable.client.widgets.utils;

/**
 * The interface which provides a callback once the column value is commited and the editor is exited
 * @author Anil Sehgal
 */
public interface TreeTableColumnValueUpdater {

	/**
	 * the update method must be overridden to perform any action once the editor commits a column value
	 * @param index index of the row
	 * @param object the TreeTableItem instance rendered in the row
	 * @param value the updated value in the editor
	 */
	void update(int index, TreeTableItem object, Object value);
}
