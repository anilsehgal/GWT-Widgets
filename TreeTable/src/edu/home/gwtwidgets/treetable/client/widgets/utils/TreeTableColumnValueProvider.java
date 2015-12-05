package edu.home.gwtwidgets.treetable.client.widgets.utils;

/**
 * The interface which provides the value rendered in a column for all column types except CUSTOM
 * @author Anil Sehgal
 */
public interface TreeTableColumnValueProvider {

	/**
	 * This method must be overridden to return the value which will be rendered
	 * @param treeTableItem the input tree table item
	 * @return The returned value will be rendered
	 */
	Object getValue( TreeTableItem treeTableItem );
}
