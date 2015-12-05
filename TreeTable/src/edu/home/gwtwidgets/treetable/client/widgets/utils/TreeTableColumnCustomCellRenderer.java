package edu.home.gwtwidgets.treetable.client.widgets.utils;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import edu.home.gwtwidgets.treetable.client.widgets.utils.TreeTableColumn.TreeTableColumnType;
/**
 * Renderer Interface used to render the TreeTable column with {@link TreeTableColumnType}.CUSTOM rendering 
 * @author Anil Sehgal
 */
public interface TreeTableColumnCustomCellRenderer {

	/**
	 * The method responsible for rendering the cell of the TreeTable 
	 * @param context context of the column
	 * @param value the value to be rendered
	 * @param sb the safe html builder to build the html for the column
	 */
	void render(Context context, TreeTableItem value, SafeHtmlBuilder sb);
}
