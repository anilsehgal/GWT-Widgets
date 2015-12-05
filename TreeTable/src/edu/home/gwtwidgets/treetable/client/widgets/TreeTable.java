package edu.home.gwtwidgets.treetable.client.widgets;

import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.home.gwtwidgets.treetable.client.widgets.cells.EditNumberCell;
import edu.home.gwtwidgets.treetable.client.widgets.cells.EditTextAreaCell;
import edu.home.gwtwidgets.treetable.client.widgets.cells.TreeTableCell;
import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableColumn;
import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableItem;
import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableModel;
import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableColumn.TreeTableColumnType;
/**
 * The <code>TreeTable</code> Widget enables the creation of a TreeGrid view with 
 * GWT Async capabilities. The TreeTable is built over {@link CellTable} Widget provided by GWT
 * <br>
 * TODO Anil - add more constructors
 * @author Anil Sehgal
 */
public class TreeTable extends Composite {

	/**
	 * The Data Provider which controls the data for the entire TreeTable
	 */
	ListDataProvider<TreeTableItem> listDataProvider;

	/**
	 * Key Provider for TreeTable Data Provider
	 */
	private static final ProvidesKey<TreeTableItem> KEY_PROVIDER =
			new ProvidesKey<TreeTableItem>() {
		@Override
		public Object getKey(TreeTableItem TreeTableItem) {
			
			/**
			 * The key always corresponds to a unique id
			 */
			return TreeTableItem.id;
		}
	};

	/**
	 * The TreeTableModel Instance
	 * @see TreeTableModel for more details
	 */
	TreeTableModel m_treeTableModel;
	
	/**
	 * The GWT {@link CellTable} Widget 
	 */
	CellTable<TreeTableItem> cellTable;
	
	/**
	 * Page Size has not been handled in this widget
	 */
	int pageSize = 500;
	
	/**
	 * Single Selection model has been hardcoded. Modify it to make the user provide the TreeTable selectionModel
	 */
	SingleSelectionModel<TreeTableItem> selectionModel;
	
	/**
	 * The TreeTable constructor
	 * @param items the root level items to be shown
	 * @param resources the {@link CellTable} resources 
	 * @param treeTableModel the TreeTableModel containing column information and tree expansion logic
	 */
	public TreeTable(List< TreeTableItem > items, Resources resources, TreeTableModel treeTableModel ) {

		super();
		/**
		 * Init the CellTable
		 */
		cellTable = new CellTable<>(pageSize, resources, KEY_PROVIDER);
		initWidget(cellTable);
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		selectionModel = new SingleSelectionModel<>();
		m_treeTableModel = treeTableModel;
		cellTable.setSelectionModel(selectionModel);
		cellTable.addCellPreviewHandler( m_treeTableModel.cellPreviewHandler() );
		listDataProvider = new ListDataProvider<>(items);
		
		/**
		 * Add the Tree Column, very little modification is allowed here 
		 * as the widget logic will govern the expansionof the tree
		 */
		Column<TreeTableItem, TreeTableItem> treeColumn = new Column<TreeTableItem, TreeTableItem>(new TreeTableCell<TreeTableItem>( cellTable, listDataProvider )) {

			@Override
			public TreeTableItem getValue(TreeTableItem object) {

				return object;
			}
		};
		cellTable.addColumn(treeColumn, treeTableModel.getTreeColumnHeader() );
		
		for ( TreeTableColumn treeTableColumn : m_treeTableModel.getColumns() ) {
			
			Column<TreeTableItem, ?> column = buildColumn( treeTableColumn );			
			cellTable.addColumn( column, treeTableColumn.getColumnHeader() );
		}


		/**
		 * Set the widget dimensions
		 */
		cellTable.setWidth("100%");
		cellTable.setHeight( ( 20 * ( items.size() + 1 ) ) + "px");

		cellTable.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {

			@Override
			public void onRowCountChange(RowCountChangeEvent event) {

				cellTable.setHeight( ( 20 * ( event.getNewRowCount() + 1 ) ) + "px");
			}
		});
	}
	
	/**
	 * Returns the underlying selection model from the TreeTable
	 * <br>
	 * <br>
	 * One may add selection listeners to capture the selection change event
	 * @return <code>SingleSelectionModel</code> Instance
	 */
	public SingleSelectionModel<TreeTableItem> getSelectionModel() {
		
		return selectionModel;
	}
	
	/**
	 * Displays the TreeTable on UI, call this after fetching the root level information
	 */
	public void displayTreeTable() {
		
		listDataProvider.addDataDisplay( cellTable );
	}

	/**
	 * Returns the current list of rows available in the TreeTable widget
	 * @return the list of TreeTableItem instances
	 */
	public List<TreeTableItem> getTreeTableData() {
		
		return listDataProvider.getList();
	}
	
	/**
	 * Adds a new child row under the selected row based on the selection model, 
	 * if nothing is selected in the selection model, the row will be added at the root level,
	 * <br><br>
	 * NOTE: the added row also gets selected
	 * @param treeTableItem the row data to be displayed in the added row, the tree column would be formulated from the model
	 * <br>
	 * TODO Anil- Add Multi selection model support
	 */
	public void addNewRow( TreeTableItem treeTableItem ) {
		
		TreeTableItem parentItem = selectionModel.getSelectedObject();
		if ( parentItem == null ) {
			
			listDataProvider.getList().add( treeTableItem );
		} else {
			
			int index = listDataProvider.getList().indexOf(parentItem);
			parentItem.children.add(treeTableItem);
			parentItem.hasChildren = true;
			treeTableItem.level = parentItem.level + 1;
			parentItem.state = TreeTableItem.STATE_OPEN;
			listDataProvider.getList().add(index + 1, treeTableItem);
			cellTable.redrawRow(index);
		}
		listDataProvider.refresh();
		selectionModel.setSelected(treeTableItem, true);
	}
	
	/**
	 * Removes the selected row from the selection model, does nothing if nothing is selected
	 * <br>
	 * TODO Anil- Add Multi selection model support
	 */
	public void removeSelectedRow() {
		
		TreeTableItem treeTableItem = selectionModel.getSelectedObject();
		if ( treeTableItem == null ) {
			
			return;
		}
		
		int index = listDataProvider.getList().indexOf(treeTableItem);
		if ( index > 0 ) {
			
			TreeTableItem parentItem = listDataProvider.getList().get(index - 1);
			parentItem.children.remove(treeTableItem);
			if ( parentItem.children.size() > 0 ) {
				
				parentItem.hasChildren = true;
			} else {
				
				parentItem.hasChildren = false;
			}
			parentItem.state = TreeTableItem.STATE_CLOSED;
			cellTable.redrawRow(index - 1);
			selectionModel.setSelected(parentItem, true);
		}
		listDataProvider.getList().remove(treeTableItem);
		recurseRemove(treeTableItem);
		listDataProvider.refresh();
	}
	
	/**
	 * recursively removes children of a table item from the TreeTable
	 * @param treeTableItem the top most table item
	 */
	private void recurseRemove( TreeTableItem treeTableItem ) {
		
		listDataProvider.getList().remove(treeTableItem);
		for (TreeTableItem item : treeTableItem.children) {
			
			item.state = TreeTableItem.STATE_CLOSED;
			listDataProvider.getList().removeAll(item.children);
			recurseRemove(item);
		}
	}
	
	/**
	 * Builds a column from the column definition provided in the {@link TreeTableColumn}
	 * @param treeTableColumn the {@link TreeTableColumn} instance
	 * @return the CellTable Column
	 * <br>
	 * TODO Anil- Add Sorting and Custom Header support
	 */
	private Column<TreeTableItem, ?> buildColumn(final TreeTableColumn treeTableColumn) {
		
		/**
		 * Rendering the TreeTableColumnType.TEXT column
		 */
		if ( TreeTableColumnType.TEXT.equals( treeTableColumn.getTreeTableColumnType() ) ) {
			
			AbstractCell<String> cell = null;
			if ( treeTableColumn.isEditable() ) {
				
				if ( treeTableColumn.isShowInEditor() ) {
					
					cell = new TextInputCell();
				} else {
					
					cell = new EditTextCell();
				}
			} else {
				
				cell = new TextCell();
			}
			Column<TreeTableItem, String> column = new Column<TreeTableItem, String>( cell ) {

				@Override
				public String getValue(TreeTableItem object) {

					return (String) treeTableColumn.getColumnValueProvider().getValue(object);
				}
			};
			column.setFieldUpdater(new FieldUpdater<TreeTableItem, String>() {

				@Override
				public void update(int index, TreeTableItem object, String value) {
					
					treeTableColumn.getColumnValueUpdater().update( index, object, value );
				}
			});
			return column;
		/**
		 * Rendering the TreeTableColumnType.TEXTAREA column
		 */
		} else if ( TreeTableColumnType.TEXTAREA.equals( treeTableColumn.getTreeTableColumnType() ) ) {
			
			AbstractCell<String> cell = null;
			if ( treeTableColumn.isEditable() ) {
				
				cell = new EditTextAreaCell();
			} else {
				
				cell = new TextCell();
			}
			Column<TreeTableItem, String> column = new Column<TreeTableItem, String>( cell ) {

				@Override
				public String getValue(TreeTableItem object) {

					return (String) treeTableColumn.getColumnValueProvider().getValue(object);
				}
			};
			column.setFieldUpdater(new FieldUpdater<TreeTableItem, String>() {

				@Override
				public void update(int index, TreeTableItem object, String value) {
					
					treeTableColumn.getColumnValueUpdater().update( index, object, value );
				}
			});
			return column;
		/**
		 * Rendering the TreeTableColumnType.NUMBER column
		 */
		} else if ( TreeTableColumnType.NUMBER.equals( treeTableColumn.getTreeTableColumnType() ) ) {
			
			AbstractCell<Number> cell = null;
			if ( treeTableColumn.isEditable() ) {
				
				cell = new EditNumberCell();
			} else {
				
				cell = new NumberCell();
			}
			Column<TreeTableItem, Number> column = new Column<TreeTableItem, Number>( cell ) {

				@Override
				public Number getValue(TreeTableItem object) {

					return (Number) treeTableColumn.getColumnValueProvider().getValue(object);
				}
			};
			column.setFieldUpdater(new FieldUpdater<TreeTableItem, Number>() {

				@Override
				public void update(int index, TreeTableItem object, Number value) {
					
					treeTableColumn.getColumnValueUpdater().update( index, object, value );
				}
			});
			return column;
		/**
		 * Rendering the TreeTableColumnType.DATE column
		 */
		} else if ( TreeTableColumnType.DATE.equals( treeTableColumn.getTreeTableColumnType() ) ) {
			
			AbstractCell<Date> cell = null;
			if ( treeTableColumn.isEditable() ) {
				
				cell = new DatePickerCell();
			} else {
				
				cell = new DateCell();
			}
			Column<TreeTableItem, Date> column = new Column<TreeTableItem, Date>( cell ) {

				@Override
				public Date getValue(TreeTableItem object) {

					return (Date) treeTableColumn.getColumnValueProvider().getValue(object);
				}
			};
			column.setFieldUpdater(new FieldUpdater<TreeTableItem, Date>() {

				@Override
				public void update(int index, TreeTableItem object, Date value) {
					
					treeTableColumn.getColumnValueUpdater().update( index, object, value );
				}
			});
			return column;
		/**
		 * Rendering the TreeTableColumnType.BOOLEAN column
		 */
		} else if ( TreeTableColumnType.BOOLEAN.equals( treeTableColumn.getTreeTableColumnType() ) ) {
			
			AbstractCell<Boolean> cell = null;
			cell = new CheckboxCell();
			Column<TreeTableItem, Boolean> column = new Column<TreeTableItem, Boolean>( cell ) {

				@Override
				public Boolean getValue(TreeTableItem object) {

					return (Boolean) treeTableColumn.getColumnValueProvider().getValue(object);
				}
			};
			column.setFieldUpdater(new FieldUpdater<TreeTableItem, Boolean>() {

				@Override
				public void update(int index, TreeTableItem object, Boolean value) {
					
					treeTableColumn.getColumnValueUpdater().update( index, object, value );
				}
			});
			return column;
		/**
		 * Rendering the TreeTableColumnType.CUSTOM column
		 */
		} else if ( TreeTableColumnType.CUSTOM.equals( treeTableColumn.getTreeTableColumnType() ) ) {
			
			AbstractCell<TreeTableItem> cell = new AbstractCell<TreeTableItem>() {
				
				@Override
				public void render(Context context, TreeTableItem value, SafeHtmlBuilder sb) {
					
					treeTableColumn.getTreeTableColumnCustomCellRenderer().render(context, value, sb);
				}
			};
			Column<TreeTableItem, TreeTableItem> column = new Column<TreeTableItem, TreeTableItem>( cell ) {

				@Override
				public TreeTableItem getValue(TreeTableItem object) {

					return (TreeTableItem) treeTableColumn.getColumnValueProvider().getValue(object);
				}
			};
			column.setFieldUpdater(new FieldUpdater<TreeTableItem, TreeTableItem>() {

				@Override
				public void update(int index, TreeTableItem object, TreeTableItem value) {
					
					treeTableColumn.getColumnValueUpdater().update( index, object, value );
				}
			});
			return column;
		}
		return null;
	}

	
}
