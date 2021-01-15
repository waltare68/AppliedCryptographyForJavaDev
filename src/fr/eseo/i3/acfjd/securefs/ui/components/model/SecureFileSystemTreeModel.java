package fr.eseo.i3.acfjd.securefs.ui.components.model;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class SecureFileSystemTreeModel extends DefaultTreeModel {

	private static final String DEFAULT_ROOT_FOLDER;

	static {
		DEFAULT_ROOT_FOLDER = System.getProperty("user.home") + File.separator + "secure";
	}

	private final String rootFolder;

	private final HiddenFileFilter hiddenFileFilter;

	private final HiddenFolderFilter hiddenFolderFilter;

	public SecureFileSystemTreeModel() {
		this(SecureFileSystemTreeModel.DEFAULT_ROOT_FOLDER);
	}

	public SecureFileSystemTreeModel(String rootFolder) {
		super(new DefaultMutableTreeNode(rootFolder));
		this.rootFolder = rootFolder;
		this.hiddenFileFilter = new HiddenFileFilter();
		this.hiddenFolderFilter = new HiddenFolderFilter();
	}

	public Object getRoot() {
		return new File(this.rootFolder);
	}

	public Object getChild(Object parent, int index) {
		final File directory = (File) parent;
		final File[] childrenFolders = directory.listFiles(this.hiddenFolderFilter);
		final File[] childrenFiles = directory.listFiles(this.hiddenFileFilter);
		Arrays.sort(childrenFolders);
		Arrays.sort(childrenFiles);
		final List<File> children = new ArrayList<>();
		for (int i = 0; i < childrenFolders.length; i++) {
			children.add(childrenFolders[i]);
		}
		for (int i = 0; i < childrenFiles.length; i++) {
			children.add(childrenFiles[i]);
		}
		return new File(directory, children.get(index).getName());
	}

	@Override
	public int getChildCount(Object parent) {
		final File fileSystemEntity = (File) parent;
		if (fileSystemEntity.isDirectory()) {
			final File directory = (File) parent;

			final File[] childrenFolders = directory.listFiles(this.hiddenFolderFilter);
			final File[] childrenFiles = directory.listFiles(this.hiddenFileFilter);
			Arrays.sort(childrenFolders);
			Arrays.sort(childrenFiles);
			final List<File> children = new ArrayList<>();
			for (int i = 0; i < childrenFolders.length; i++) {
				children.add(childrenFolders[i]);
			}
			for (int i = 0; i < childrenFiles.length; i++) {
				children.add(childrenFiles[i]);
			}
			return children.size();
		}
		return 0;
	}
	
	@Override
	public boolean isLeaf(Object node) {
		return this.getChildCount(node)==0;
	}
	
	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {}
	
	@Override
	public int getIndexOfChild(Object parent, Object child) {
		final File directory = (File) parent;
		final File fileSystemEntity = (File)child;
		final File[] childrenFolders = directory.listFiles(this.hiddenFolderFilter);
		final File[] childrenFiles = directory.listFiles(this.hiddenFileFilter);
		Arrays.sort(childrenFolders);
		Arrays.sort(childrenFiles);
		final List<File> children = new ArrayList<>();
		for (int i = 0; i < childrenFolders.length; i++) {
			children.add(childrenFolders[i]);
		}
		for (int i = 0; i < childrenFiles.length; i++) {
			children.add(childrenFiles[i]);
		}
		int result = -1;
		int i = 0;
		while(i<children.size() && result == -1) {
			if(fileSystemEntity.equals(children.get(i))) {
				result = i;
			}
			i++;
		}
		return result;
	}
	
	@Override
	public void addTreeModelListener(TreeModelListener l) {}
	@Override
	public void removeTreeModelListener(TreeModelListener l) {}
	

	private class HiddenFileFilter implements FileFilter{
		@Override
		public boolean accept(File pathname) {
			return !pathname.isHidden() && pathname.isFile();
		}
	}

	private class HiddenFolderFilter implements FileFilter{
		@Override
		public boolean accept(File pathname) {
			return !pathname.isHidden() && pathname.isDirectory();
		}
	}
}
