package edu.home.gwtwidgets.treetable.client.widgets.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.home.gwtwidgets.treetable.client.widgets.TreeTable;

/**
 * the abstract class which must be implemented by a custom {@link TreeTableItem} to be qualified to show in the {@link TreeTable}
 * @author Anil Sehgal
 */
public abstract class TreeTableItem {
	
	/**
	 * String defining the CLOSED state of the Tree Item
	 */
	public static final String STATE_CLOSED = "closed";
	
	/**
	 * String defining the OPEN state of the Tree Item
	 */
	public static final String STATE_OPEN = "open";
	
	/**
	 * String defining the PROCESSING state of the Tree Item
	 */
	public static final String STATE_PROCESSING = "processing";
	
	/**
	 * String defining the unique id of the Tree Item
	 */
	public String id;
	
	/**
	 * int defining the level of the Tree Item
	 */
	public int level;
	
	/**
	 * state of the current Tree Item
	 */
	public String state = STATE_CLOSED;
	
	/**
	 * child {@link List} stored in the {@link TreeTableItem} to facilitate collapse and caching in the {@link TreeTable}
	 */
	public List<TreeTableItem> children = new ArrayList<TreeTableItem>();
	
	/**
	 * The hasChildren flag which defines whether the expand icon is to be shown
	 */
	public boolean hasChildren;
	
	/**
	 * The {@link TreeTableModel} instance for expansion logic
	 */
	public TreeTableModel treeTableModel;
	
	/**
	 * Constructor for the TreeTableItem, must be called explicitly in the child implementation constructor
	 * @param id the id of the item
	 * @param treeTableModel the model to be stored
	 */
	public TreeTableItem(String id, TreeTableModel treeTableModel) {
		super();
		this.id = id;
		this.treeTableModel = treeTableModel;
	}
	
	/**
	 * The method which provides the safe html for the first tree column for every item
	 * @param treeTableItem the input item
	 * @return the safe html for the first tree column
	 */
	public abstract SafeHtml getTreeColumnSafeHtml( TreeTableItem treeTableItem );
	
	/**
	 * setter for {@link TreeTableItem} level
	 * @param level the level to be set
	 */
	public void setLevel( int level ) {
		
		this.level = level;
	}
	
	/**
	 * the method which gets called whenever an item is expanded
	 * @param callback the callback to be used for view manipulation once the async call has returned from the server
	 */
	public final void doGetChildren( final AsyncCallback<List<TreeTableItem>> callback ) {
		
		if ( this.children.size() == 0 && this.hasChildren ) {
			
			treeTableModel.getChildren(this, new AsyncCallback<List<TreeTableItem>>() {
				
				@Override
				public void onSuccess(List<TreeTableItem> result) {
	
					children.clear();
					children.addAll( result );
					callback.onSuccess(result);
				}
				
				@Override
				public void onFailure(Throwable caught) {
	
					callback.onFailure(caught);
				}
			});
		} else if ( this.hasChildren ) {
			
			callback.onSuccess( this.children );
		}
	}
}
