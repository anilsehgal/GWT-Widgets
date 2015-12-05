package edu.home.gwtwidgets.treetable.client.widgets.utils;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;

/**
 * the interface defining the columns, tree column header, cell preview handler and the logic for fetching children for the tree table instance
 * @author Anil Sehgal
 */
public interface TreeTableModel {

	/**
	 * returns the list of {@link TreeTableColumn}s, all these columns would be rendered
	 * @return list of {@link TreeTableColumn}s
	 */
	List<TreeTableColumn> getColumns();
	
	/**
	 * This method defines the logic for fetching the children of an input {@link TreeTableItem} during expansion
	 * @param treeTableItem the input {@link TreeTableItem} to be expanded
	 * @param callback the callback with children of {@link TreeTableItem}s
	 */
	void getChildren(TreeTableItem treeTableItem, AsyncCallback<List<TreeTableItem>> callback);

	/**
	 * defines the header for the Tree Column
	 * @return the header string for the Tree Column
	 */
	String getTreeColumnHeader();

	/**
	 * exposes the cell preview event handler for all cells
	 * @return the {@link CellPreviewEvent}.Handler
	 */
	Handler<TreeTableItem> cellPreviewHandler();
}
