package edu.home.gwtwidgets.treetable.client.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable.Style;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;

import edu.home.gwtwidgets.treetable.client.resources.Resources;
import edu.home.gwtwidgets.treetable.client.widgets.TreeTable;
import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableColumn;
import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableColumnValueProvider;
import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableColumnValueUpdater;
import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableItem;
import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableModel;
import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableTemplates;
import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableColumn.TreeTableColumnType;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TreeTableExample implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		List< TreeTableItem > items = new ArrayList<  >();
		MyTreeTableItem item1 = new MyTreeTableItem("TreeTableItem 1");
		item1.setProperties("item 1", 100, new Date());
		MyTreeTableItem item2 = new MyTreeTableItem("TreeTableItem 2");
		item2.setProperties("item 2", 200, new Date());
		MyTreeTableItem item3 = new MyTreeTableItem("TreeTableItem 3");
		item3.setProperties("item 3", 300, new Date());
		item1.hasChildren = true;

		items.add(item1);
		items.add(item2);
		items.add(item3);

		final TreeTable treeTable = new TreeTable( items, CustomResources.INSTANCE, new MyTreeTableModel() );
		RootPanel.get( "treeTableContainer" ).add(treeTable);
		
		Button addButton = new Button("Add Row");
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				MyTreeTableItem itemNew = new MyTreeTableItem("TreeTableItem New");
				itemNew.setProperties("", 0, new Date());
				treeTable.addNewRow(itemNew);
			}
		});
		Button removeButton = new Button("Remove Row");
		removeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				treeTable.removeSelectedRow();
			}
		});
		
		Button printButton = new Button("Print Data");
		printButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				List<TreeTableItem> data = treeTable.getTreeTableData();
				
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				builder.append(SafeHtmlUtils.fromString("|	property1	|	property2	|	property3	|" ));
				builder.append(SafeHtmlUtils.fromSafeConstant("<br>"));
				for ( TreeTableItem treeTableItem : data ) {
					
					builder.append(SafeHtmlUtils.fromString( treeTableItem.toString() ));
					builder.append(SafeHtmlUtils.fromSafeConstant("<br>"));
				}
				HTML html = new HTML(builder.toSafeHtml());
				RootPanel.get( "treeDataContainer" ).clear();
				RootPanel.get( "treeDataContainer" ).add(html);
			}
		});
		RootPanel.get( "treeTableContainer" ).add(addButton);
		RootPanel.get( "treeTableContainer" ).add(removeButton);
		RootPanel.get( "treeTableContainer" ).add(printButton);
		treeTable.displayTreeTable();
	}

	interface CustomResources extends com.google.gwt.user.cellview.client.CellTable.Resources {

		public static final CustomResources INSTANCE = GWT.create( CustomResources.class );

		/**
		 * The styles used in this widget.
		 */
		@Source( "edu/home/gwtwidgets/treetable/client/css/cellTreeTable.css" )
		Style cellTableStyle();
	}
}

class MyTreeTableModel implements TreeTableModel {

	@Override
	public List<TreeTableColumn> getColumns() {

		List<TreeTableColumn> columns = new ArrayList<>();
		columns.add( new TreeTableColumn(TreeTableColumnType.TEXT, true, false, "Col Header 1", new TreeTableColumnValueProvider() {

			@Override
			public Object getValue(TreeTableItem treeTableItem) {

				if ( treeTableItem instanceof MyTreeTableItem ) {

					return ((MyTreeTableItem)treeTableItem).property1;
				} else {

					return "Failure in rendering";
				}
			}
		}, new TreeTableColumnValueUpdater() {
			
			@Override
			public void update(int index, TreeTableItem object, Object value) {
				
				((MyTreeTableItem)object).property1 = (String) value;
			}
		}) );

		columns.add( new TreeTableColumn(TreeTableColumnType.NUMBER, true, false, "Col Header 2", new TreeTableColumnValueProvider() {

			@Override
			public Object getValue(TreeTableItem treeTableItem) {

				if ( treeTableItem instanceof MyTreeTableItem ) {

					return ((MyTreeTableItem)treeTableItem).property2;
				} else {

					return 0;
				}
			}
		}, new TreeTableColumnValueUpdater() {
			
			@Override
			public void update(int index, TreeTableItem object, Object value) {

				((MyTreeTableItem)object).property2 = Math.round(((Float)value));
			}
		}) );

		columns.add( new TreeTableColumn(TreeTableColumnType.DATE, true, false, "Col Header 1", new TreeTableColumnValueProvider() {

			@Override
			public Object getValue(TreeTableItem treeTableItem) {

				if ( treeTableItem instanceof MyTreeTableItem ) {

					return ((MyTreeTableItem)treeTableItem).property3;
				} else {

					return new Date();
				}
			}
		}, new TreeTableColumnValueUpdater() {
			
			@Override
			public void update(int index, TreeTableItem object, Object value) {

				((MyTreeTableItem)object).property3 = (Date) value;
			}
		}) );

		return columns;
	}

	@Override
	public void getChildren(TreeTableItem treeTableItem,
			final AsyncCallback<List<TreeTableItem>> callback) {



		final List<TreeTableItem> treeTableItems = new ArrayList<TreeTableItem>();
		if ( "TreeTableItem 1".equals( treeTableItem.id ) ) {

			MyTreeTableItem item4 = new MyTreeTableItem("TreeTableItem 4");
			item4.setProperties("item 4", 400, new Date());
			MyTreeTableItem item5 = new MyTreeTableItem("TreeTableItem 5");
			item5.setProperties("item 5", 500, new Date());
			MyTreeTableItem item6 = new MyTreeTableItem("TreeTableItem 6");
			item6.setProperties("item 6", 600, new Date());
			item4.hasChildren = true;
			treeTableItems.add(item4);
			treeTableItems.add(item5);
			treeTableItems.add(item6);
		} else if ( "TreeTableItem 4".equals( treeTableItem.id ) ) {

			MyTreeTableItem item7 = new MyTreeTableItem("TreeTableItem 7");
			item7.setProperties("item 7", 700, new Date());
			MyTreeTableItem item8 = new MyTreeTableItem("TreeTableItem 8");
			item8.setProperties("item 8", 800, new Date());
			MyTreeTableItem item9 = new MyTreeTableItem("TreeTableItem 9");
			item9.setProperties("item 9", 900, new Date());
			treeTableItems.add(item7);
			treeTableItems.add(item8);
			treeTableItems.add(item9);
		}

		final Timer timer = new Timer() {

			@Override
			public void run() {

				callback.onSuccess(treeTableItems);	
			}
		};
		timer.schedule(2000);
	}

	@Override
	public String getTreeColumnHeader() {

		return "TreeColumn Header";
	}

	@Override
	public Handler<TreeTableItem> cellPreviewHandler() {

		return new CellPreviewEvent.Handler<TreeTableItem>() {

			@Override
			public void onCellPreview(CellPreviewEvent<TreeTableItem> event) {

				// do nothing
			}
		};
	}
}

class MyTreeTableItem extends TreeTableItem {

	public String property1;

	public int property2;

	public Date property3;

	public MyTreeTableItem(String id) {
		super(id, new MyTreeTableModel());
	}

	public void setProperties( String property1, int property2, Date property3 ) {

		this.property1 = property1;
		this.property2 = property2;
		this.property3 = property3;
	}

	@Override
	public SafeHtml getTreeColumnSafeHtml( TreeTableItem treeTableItem ) {

		return TreeTableTemplates.INSTANCE.treeImageTextColumn(Resources.INSTANCE.getTreeItemIcon().getSafeUri(), treeTableItem.id);
	}

	@Override
	public String toString() {
		return property1 + "|" + property2 + "|" + property3 + "|";
	}
	
	
}
