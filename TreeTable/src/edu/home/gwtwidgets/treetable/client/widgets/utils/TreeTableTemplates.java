package edu.home.gwtwidgets.treetable.client.widgets.utils;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;

/**
 * the templates for showing the TreeTable Tree Column Icons
 * @author Anil Sehgal
 */
public interface TreeTableTemplates extends SafeHtmlTemplates {
	
	/**
	 * The global instance of {@link TreeTableTemplates}
	 */
	public static TreeTableTemplates INSTANCE = GWT.create(TreeTableTemplates.class);
	
	/**
	 * icon representing closed and processing state of the tree column
	 * @param img the tree state icon resource safe uri
	 * @param id the id of the html element
	 * @return the safe html of the image tag
	 */
	@Template("<img style=\"transform: rotate(0deg);\" name=\"TreeState\" id=\"{1}\" src=\"{0}\"/>")
	SafeHtml icon(SafeUri img, String id);
	
	/**
	 * icon representing open state of the tree column
	 * @param img the tree state icon resource safe uri
	 * @param id the id of the html element
	 * @return the safe html of the image tag
	 */
	@Template("<img style=\"transform: rotate(90deg);\" name=\"TreeState\" id=\"{1}\" src=\"{0}\"/>")
	SafeHtml icon90(SafeUri img, String id);
	
	/**
	 * icon with text format
	 * 
	 * @param img icon shown on the left
	 * @param text the text displayed inline with the image
	 * @return the safe html of the division
	 */
	@Template("<div style=\"display: inline;\"><div style=\"display: table-cell;\"><img style=\"width: 20px; height: 20px;\" src=\"{0}\"/></div><div style=\"display: table-cell;padding-bottom: 5px;\">{1}</div></div>")
	SafeHtml treeImageTextColumn(SafeUri img, String text );
}