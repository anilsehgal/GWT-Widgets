package edu.home.gwtwidgets.treetable.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {

	public static Resources INSTANCE = GWT.create(Resources.class);
	
	@Source({"edu/home/gwtwidgets/treetable/client/images/right.png"})
	public ImageResource getExpandIcon();
	
	@Source({"edu/home/gwtwidgets/treetable/client/images/object.png"})
	public ImageResource getTreeItemIcon();
	
	@Source({"edu/home/gwtwidgets/treetable/client/images/spinner.gif"})
	public ImageResource getSpinnerIcon();
}
