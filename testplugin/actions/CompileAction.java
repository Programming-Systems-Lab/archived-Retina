package testplugin.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.jface.dialogs.MessageDialog;

import compileaction.actions.Compiler;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class CompileAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	/**
	 * The constructor.
	 */
	public CompileAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		String str = null;
		String displayName = null;

		try {
			IEditorInput iei = window.getActivePage().getActiveEditor().getEditorInput();
			displayName = iei.getName();
			if (iei instanceof FileEditorInput) {
				str = (((FileEditorInput) iei).getPath()).toString();
			}

			compileoutput.views.CompileOutputView.setMessage(str);
			String results = Compiler.compileFile(str, displayName);
			compileoutput.views.CompileOutputView.setMessage(str + " compiled." + System.getProperty("line.separator") + results);
		} catch(Exception e) {
			e.printStackTrace();
			MessageDialog.openInformation(
					window.getShell(),
					"TestPlugin Plug-in",
					"View not open. Message:" + e.getMessage() + " name:" + e.toString());
		}
	}

	/**
	 * Selection in the workbench has been changed. We
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}