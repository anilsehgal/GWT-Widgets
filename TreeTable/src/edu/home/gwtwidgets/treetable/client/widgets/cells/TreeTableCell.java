package edu.home.gwtwidgets.treetable.client.widgets.cells;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;

import edu.home.gwtwidgets.treetable.client.resources.Resources;
import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableItem;
import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableTemplates;


/**
 * A cell used to render Tree column of the TreeTable
 * @author Anil Sehgal
 *
 * @param <C> the type that this Cell represents
 */
public class TreeTableCell<C> extends AbstractCell<C> {

	/**
	 * The List Data Provider of the TreeTable
	 */
	ListDataProvider<TreeTableItem> m_listDataProvider;
	
	/**
	 * The Tree Table Instance
	 */
	CellTable<TreeTableItem> m_cellTable;
	
	/**
	 * The TreeTableCell constructor
	 * @param cellTable the  cell table, this would be a part of
	 * @param listDataProvider the data provider of the cell table
	 */
	public TreeTableCell( CellTable<TreeTableItem> cellTable, ListDataProvider<TreeTableItem> listDataProvider ) {
		
		super( BrowserEvents.CLICK );
		m_listDataProvider = listDataProvider;
		m_cellTable = cellTable;
	}
	
	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			C anyValue, SafeHtmlBuilder sb) {
		
		TreeTableItem value = null;
		if ( anyValue instanceof TreeTableItem ) {
			
			value = (TreeTableItem) anyValue;
			if ( value.level > 0 ) {
				
				/**
				 * indent the tree column value based on the level of the item
				 * 
				 * <br>
				 * TODO Anil - Make this configurable
				 */
				for ( int i = 0;i < value.level;i++ ) {
					
					sb.append( SafeHtmlUtils.fromSafeConstant( "<div style=\"display: inline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>" ) );					
				}
			}
			if ( value.hasChildren ) {
				
				/**
				 * calculate the expand/collapse icon in case the node has children
				 */
				if ( TreeTableItem.STATE_CLOSED.equals(value.state) ) {
					
					/**
					 * Closed Node
					 */
					sb.append( TreeTableTemplates.INSTANCE.icon( Resources.INSTANCE.getExpandIcon().getSafeUri(), value.id ) );
				} else if ( TreeTableItem.STATE_OPEN.equals(value.state) ) {
					
					/**
					 * Opened Node
					 */
					sb.append( TreeTableTemplates.INSTANCE.icon90( Resources.INSTANCE.getExpandIcon().getSafeUri(), value.id ) );			
				} else {
					
					/**
					 * Loading Node
					 */
					sb.append( TreeTableTemplates.INSTANCE.icon( Resources.INSTANCE.getSpinnerIcon().getSafeUri(), value.id ) );
				}
			} else {

				/**
				 * Leaf Node
				 */
				sb.append( SafeHtmlUtils.fromSafeConstant( "<div style=\"display: inline;\">&nbsp;&nbsp;&nbsp;</div>" ) );
			}
			sb.append(value.getTreeColumnSafeHtml( value ));
		} else {
			
			sb.append(SafeHtmlUtils.fromSafeConstant("input value = " + value + ",expected type <? extends TreeTableItem>, got " + anyValue.getClass() ) );
		}
	}
	@Override
	public void onBrowserEvent(final com.google.gwt.cell.client.Cell.Context context,
			Element parent, C value, NativeEvent event,
			ValueUpdater<C> valueUpdater) {

		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		if ( BrowserEvents.CLICK.equals( event.getType() ) && value instanceof TreeTableItem ) {
			
			EventTarget eventTarget = event.getEventTarget();
			if (Element.is(eventTarget)) {
		        Element target = Element.as(eventTarget);
		        
		        /**
				 * Check if the TreeNode expand/collapse icon was clicked
				 */
		        if ("img".equals(target.getTagName().toLowerCase()) && "TreeState".equals(target.getAttribute("name")) ) {
		          
		        	final TreeTableItem object = (TreeTableItem) value;
					if ( TreeTableItem.STATE_CLOSED.equals(object.state) ) {
						
						/**
						 * show the spinner
						 */
						object.state = TreeTableItem.STATE_PROCESSING;
						m_cellTable.redrawRow(context.getIndex());
						
						/**
						 * ask the model to provide async children 
						 */
						object.doGetChildren(new AsyncCallback<List<TreeTableItem>>() {
							
							@Override
							public void onSuccess(List<TreeTableItem> result) {

								/**
								 * increase the depth of children by One
								 */
								int depth = object.level + 1;
								for ( TreeTableItem child : object.children ) {
									
									child.setLevel(depth);
								}
								
								/**
								 * add the indented children to the treetable under the expanded row
								 */
								object.state = TreeTableItem.STATE_OPEN;
								m_listDataProvider.getList().addAll(context.getIndex() + 1, object.children);
								m_listDataProvider.refresh();
							}
							
							@Override
							public void onFailure(Throwable caught) {

								/**
								 * TODO Anil see if you can return the callback in case of failure
								 */
								caught.printStackTrace();
							}
						});
					} else {
						
						/**
						 * Remove the children from the tree data provider and set the icon to closed state.
						 * <br>
						 * Here, the children are not removed from the TreeTableItem so as to avoid another server call
						 */
						object.state = TreeTableItem.STATE_CLOSED;
						m_listDataProvider.getList().removeAll(object.children);
						recurseRemove(object);
						m_listDataProvider.refresh();
					}
					m_cellTable.redrawRow(context.getIndex());
		        }
		    }
		}
	}
	
	/**
	 * Recurse removes all the children of the input tree table item from the treetable data provider
	 * @param treeTableItem the parent TreeTableItem
	 * <br>
	 * <br>
	 * NOTE: The children are not removed from the TreeTableItem so as to avoid another server call 
	 */
	private void recurseRemove( TreeTableItem treeTableItem ) {
		
		for (TreeTableItem item : treeTableItem.children) {
			
			item.state = TreeTableItem.STATE_CLOSED;
			m_listDataProvider.getList().removeAll(item.children);
			recurseRemove(item);
		}
	}
}
