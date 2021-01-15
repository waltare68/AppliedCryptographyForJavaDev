package fr.eseo.i3.acfjd.securefs.ui.components;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import fr.eseo.i3.acfjd.securefs.ui.components.model.SecureFileSystemTreeModel;
import fr.eseo.i3.acfjd.securefs.ui.components.view.SecureFileSystemTreeRenderer;

public class SecureFileSystemTree extends JTree implements MouseListener {

	private JPopupMenu secureFileSystemTreePopup;

	public SecureFileSystemTree(String rootFolder) {
		super(new SecureFileSystemTreeModel(rootFolder));
		this.setCellRenderer(new SecureFileSystemTreeRenderer());
		this.setEditable(false);
		this.addMouseListener(this);
	}

	public void setSecureFileSystemTreePopup(JPopupMenu menu) {
		this.add(menu);
		this.secureFileSystemTreePopup = menu;
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		if (event == null) {
			return "";
		}
		final TreePath path = getPathForLocation(event.getX(), event.getY());
		if (path != null) {
			return path.getLastPathComponent().toString();
		}
		return "";
	}

	@Override
	public void mouseClicked(MouseEvent event) {
	}

	public void mousePressed(MouseEvent event) {
		mouseReleased(event);
	}

	public void mouseReleased(MouseEvent event) {
		final int x = event.getX();
		final int y = event.getY();
		final TreePath path = this.getPathForLocation(x, y);
		if (path == null) {
			this.clearSelection();
		}
		if (event.isControlDown()) {
			this.addSelectionPath(path);
		} else {
			this.clearSelection();
			this.addSelectionPath(path);
		}
		if (event.isPopupTrigger()) {
			if (event.isControlDown()) {
				this.addSelectionPath(path);
			} else {
				this.clearSelection();
				this.addSelectionPath(path);
			}

			if (this.secureFileSystemTreePopup != null) {
				this.secureFileSystemTreePopup.show(this, x, y);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {}

	@Override
	public void mouseExited(MouseEvent event) {}

	
	public File [] getFiles() {
		final TreePath[] treePaths = this.getSelectionPaths();
		final File[] files = new File[treePaths.length];
		for(int i = 0; i < treePaths.length; i++) {
			files[i] = (File)treePaths[i].getLastPathComponent();
		}
		return files;
	}
}
