package fr.eseo.i3.acfjd.securefs;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import fr.eseo.i3.acfjd.securefs.i18n.I18n;
import fr.eseo.i3.acfjd.securefs.managers.AbstractSecureFileSystemManager;
import fr.eseo.i3.acfjd.securefs.managers.DummySecureFileSystemManager;
import fr.eseo.i3.acfjd.securefs.managers.SolutionSecureFileSystemManager;
import fr.eseo.i3.acfjd.securefs.ui.SecureFileSystemUI;

public class SecureFileSystem {
	
	private SecureFileSystemUI ui;
	
	public static final String DEFAULT_ROOT_FOLDER_PATH;
	
	static {
		DEFAULT_ROOT_FOLDER_PATH = System.getProperty("user.home")+File.separator+"secure";
	}
	
	 //new instance of the class that i have created : fr.eseo.i3.acfjd.securefs.SecureFileSystem class (The solution)
	private final AbstractSecureFileSystemManager MANAGER = new SolutionSecureFileSystemManager();
	
	public static void main(String[] args) {
		final String rootFolderPath;
		if(args.length == 1) {
			rootFolderPath = args[0];
		}
		else {
			rootFolderPath = DEFAULT_ROOT_FOLDER_PATH;
		}
		new SecureFileSystem(rootFolderPath);
	}
	
	public SecureFileSystem(String rootFolderPath) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final File verifyRootPath = new File(rootFolderPath);
				if(!verifyRootPath.exists() || !verifyRootPath.isDirectory()) {
					JOptionPane.showMessageDialog(null, String.format(I18n.getI18n("Application.RootFolderError"), rootFolderPath),I18n.getI18n("Application.Title"),JOptionPane.ERROR_MESSAGE);
					System.exit(-1);
				}
				SecureFileSystem.this.ui = new SecureFileSystemUI(rootFolderPath, SecureFileSystem.this.MANAGER);
				SecureFileSystem.this.ui.display();
			}
		});
	}

}
