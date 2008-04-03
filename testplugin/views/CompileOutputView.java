package testplugin.views;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.*;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class CompileOutputView extends ViewPart {
	private static Label lbl;
	private static String message = "";

	public CompileOutputView() {
		super();
	}

	public void setFocus() {
		lbl.setFocus();
	}
	public void createPartControl(Composite parent) {
		lbl = new Label(parent, 0);
		lbl.setText(CompileOutputView.message);
	}

	public static void setMessage(String msg) {
		lbl.setText(msg);
	}
}