package fr.eseo.i3.acfjd.securefs.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import fr.eseo.i3.acfjd.securefs.i18n.I18n;
import fr.eseo.i3.acfjd.securefs.managers.AbstractSecureFileSystemManager;
import fr.eseo.i3.acfjd.securefs.ui.components.FileSystemObserverWorker;
import fr.eseo.i3.acfjd.securefs.ui.components.SecureFileSystemTree;
import fr.eseo.i3.acfjd.securefs.ui.components.StatusBar;

public class SecureFileSystemUI implements PropertyChangeListener{

	private static final ImageIcon ENCRYPT = new ImageIcon(SecureFileSystemUI.class.getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/encrypt.png"));
	private static final ImageIcon SIGN = new ImageIcon(SecureFileSystemUI.class.getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/sign.png"));
	private static final ImageIcon VERIFY_SIGN = new ImageIcon(SecureFileSystemUI.class.getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/verify_sign.png"));
	private static final ImageIcon DIGEST = new ImageIcon(SecureFileSystemUI.class.getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/digest.png"));
	private static final ImageIcon VERIFY_DIGEST = new ImageIcon(SecureFileSystemUI.class.getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/verify_digest.png"));
	private static final ImageIcon DELETE = new ImageIcon(SecureFileSystemUI.class.getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/delete.png"));
	private static final ImageIcon EXIT = new ImageIcon(SecureFileSystemUI.class.getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/exit.png"));
	private static final ImageIcon REFRESH = new ImageIcon(SecureFileSystemUI.class.getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/refresh.png"));
	
	
	private JFrame jf;
	private final String rootFolder;
	private JMenuBar menuBar;
	private JToolBar toolBar;
	private SecureFileSystemTree securefsTree;
	private StatusBar statusBar;
	private JPopupMenu popup;
	
	private Action encryptDecrypt;
	private Action safeDelete;
	private Action createSignature;
	private Action verifySignature;
	private Action createDigest;
	private Action verifyDigest;
	private Action exit;
	private final AbstractSecureFileSystemManager manager;
	private AbstractAction refresh;
	
	public SecureFileSystemUI(String rootFolder, AbstractSecureFileSystemManager manager) {
		this.rootFolder = rootFolder;
		this.manager = manager;
		manager.setSecureFileSystemUI(this);
		if(!manager.authorise()) {
			System.exit(1);
		}
		this.initActions();
		this.initComponents();
		this.layoutComponents();
		try {
			this.initWatcher();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void display() {
		this.jf.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});
		this.jf.pack();
		this.jf.setLocationRelativeTo(null);
		this.jf.setVisible(true);
	}
	
	public String getRootFolder() {
		return this.rootFolder;
	}
	
	private void initActions() {
		this.refresh = new AbstractAction(I18n.getI18n("Actions.Refresh"),SecureFileSystemUI.REFRESH) {
			@Override
			public void actionPerformed(ActionEvent event) {
				SecureFileSystemUI.this.securefsTree.updateUI();
			}
		};

		this.encryptDecrypt= new AbstractAction(I18n.getI18n("Actions.EncryptDecrypt"),SecureFileSystemUI.ENCRYPT) {
			@Override
			public void actionPerformed(ActionEvent event) {
				SecureFileSystemUI.this.getManager().encryptDecrypt(SecureFileSystemUI.this.securefsTree.getFiles());
				SecureFileSystemUI.this.securefsTree.updateUI();
			}
		};
		this.encryptDecrypt.putValue(Action.SHORT_DESCRIPTION, I18n.getI18n("Actions.EncryptDecrypt.Short"));
		this.encryptDecrypt.setEnabled(false);

		this.safeDelete= new AbstractAction(I18n.getI18n("Actions.SafeDelete"),SecureFileSystemUI.DELETE) {
			@Override
			public void actionPerformed(ActionEvent event) {
				SecureFileSystemUI.this.getManager().delete(SecureFileSystemUI.this.securefsTree.getFiles());
				SecureFileSystemUI.this.securefsTree.updateUI();
			}
		};
		this.safeDelete.putValue(Action.SHORT_DESCRIPTION, I18n.getI18n("Actions.SafeDelete.Short"));
		this.safeDelete.setEnabled(false);

		this.createSignature = new AbstractAction(I18n.getI18n("Actions.CreateSignature"),SecureFileSystemUI.SIGN) {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					SecureFileSystemUI.this.getManager().sign(SecureFileSystemUI.this.securefsTree.getFiles());
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SecureFileSystemUI.this.securefsTree.updateUI();
			}
		};
		this.createSignature.putValue(Action.SHORT_DESCRIPTION, I18n.getI18n("Actions.CreateSignature.Short"));
		this.createSignature.setEnabled(false);

		this.verifySignature= new AbstractAction(I18n.getI18n("Actions.VerifySignature"),SecureFileSystemUI.VERIFY_SIGN) {
			@Override
			public void actionPerformed(ActionEvent event) {
				SecureFileSystemUI.this.getManager().verify(SecureFileSystemUI.this.securefsTree.getFiles());
				SecureFileSystemUI.this.securefsTree.updateUI();
			}
		};
		this.verifySignature.putValue(Action.SHORT_DESCRIPTION, I18n.getI18n("Actions.VerifySignature.Short"));
		this.verifySignature.setEnabled(false);

		this.createDigest= new AbstractAction(I18n.getI18n("Actions.CreateDigest"),SecureFileSystemUI.DIGEST) {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					SecureFileSystemUI.this.getManager().createDigest(SecureFileSystemUI.this.securefsTree.getFiles());
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SecureFileSystemUI.this.securefsTree.updateUI();
			}
		};
		this.createDigest.putValue(Action.SHORT_DESCRIPTION, I18n.getI18n("Actions.CreateDigest.Short"));
		this.createDigest.setEnabled(false);

		this.verifyDigest= new AbstractAction(I18n.getI18n("Actions.VerifyDigest"),SecureFileSystemUI.VERIFY_SIGN) {
			@Override
			public void actionPerformed(ActionEvent event) {
				SecureFileSystemUI.this.getManager().verifyDigest(SecureFileSystemUI.this.securefsTree.getFiles());
				SecureFileSystemUI.this.securefsTree.updateUI();
			}
		};
		this.verifyDigest.putValue(Action.SHORT_DESCRIPTION, I18n.getI18n("Actions.VerifyDigest.Short"));
		this.verifyDigest.setEnabled(false);

		this.exit = new AbstractAction(I18n.getI18n("Actions.Exit"),SecureFileSystemUI.EXIT) {
			@Override
			public void actionPerformed(ActionEvent event) {
				for(final Frame frame : Frame.getFrames()) {
					if(frame.isActive()) {
						final WindowEvent windowClosing = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
						frame.dispatchEvent(windowClosing);
					}
				}
			}
		};
		this.exit.putValue(Action.SHORT_DESCRIPTION, I18n.getI18n("Actions.Exit.Short"));
		
	}
	
	private void initComponents() {
		this.initMenus();
		this.initToolBar();
		this.initTree();
		this.initStatusBar();
	}
	
	private void initStatusBar() {
		this.statusBar = new StatusBar(this);
		this.securefsTree.addTreeSelectionListener(this.statusBar);
	}
	
	private void initTree() {
		this.securefsTree = new SecureFileSystemTree(this.getRootFolder());
		this.securefsTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent event) {
				boolean enabled = SecureFileSystemUI.this.securefsTree.getSelectionCount()>0;
				SecureFileSystemUI.this.encryptDecrypt.setEnabled(enabled);
				SecureFileSystemUI.this.safeDelete.setEnabled(enabled);
				SecureFileSystemUI.this.createSignature.setEnabled(enabled);
				SecureFileSystemUI.this.verifySignature.setEnabled(enabled);
				SecureFileSystemUI.this.createDigest.setEnabled(enabled);
				SecureFileSystemUI.this.verifyDigest.setEnabled(enabled);
				
			}
		});
		ToolTipManager.sharedInstance().registerComponent(this.securefsTree);
		this.securefsTree.setSecureFileSystemTreePopup(popup);
	}
	
	private void initToolBar() {
		this.toolBar = new JToolBar();
		this.toolBar.add(this.encryptDecrypt);
		this.toolBar.addSeparator();
		this.toolBar.add(this.safeDelete);
		this.toolBar.addSeparator();
		this.toolBar.add(this.createSignature);
		this.toolBar.add(this.verifySignature);
		this.toolBar.addSeparator();
		this.toolBar.add(this.createDigest);
		this.toolBar.add(this.verifyDigest);
		this.toolBar.addSeparator();
		this.toolBar.add(this.refresh);
		this.toolBar.addSeparator();
		this.toolBar.add(this.exit);
		
	}
	
	private void initMenus() {
		popup = new JPopupMenu();
		this.popup.add(this.encryptDecrypt);
		this.popup.addSeparator();
		this.popup.add(this.safeDelete);
		this.popup.addSeparator();
		this.popup.add(this.createSignature);
		this.popup.add(this.verifySignature);
		this.popup.addSeparator();
		this.popup.add(this.createDigest);
		this.popup.add(this.verifyDigest);
		this.popup.addSeparator();
		this.popup.add(this.refresh);
		this.menuBar = new JMenuBar();
		final JMenu fileMenu = new JMenu(I18n.getI18n("Menu.File"));
		fileMenu.add(new JMenuItem(this.encryptDecrypt));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(this.safeDelete));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(this.createSignature));
		fileMenu.add(new JMenuItem(this.verifySignature));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(this.createDigest));
		fileMenu.add(new JMenuItem(this.verifyDigest));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(this.refresh));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(this.exit));
		this.menuBar.add(fileMenu);
	}
	
	private void layoutComponents() {
		this.jf = new JFrame(I18n.getI18n("Application.Title"));
		this.jf.setJMenuBar(menuBar);
		this.securefsTree.setPreferredSize(new Dimension(400,400));
		this.jf.add(this.toolBar,BorderLayout.NORTH);
		this.jf.add(this.statusBar,BorderLayout.SOUTH);
		this.jf.add(this.securefsTree,BorderLayout.CENTER);
	}
	
	public JFrame getWindow() {
		return this.jf;
	}
	
	public AbstractSecureFileSystemManager getManager() {
		return this.manager;
	}
	
	private void initWatcher() throws IOException {
		final FileSystemObserverWorker fsObserverWorker = new FileSystemObserverWorker(new File(this.getRootFolder()));
		fsObserverWorker.addPropertyChangeListener(this);
		fsObserverWorker.execute();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		this.securefsTree.updateUI();
		
	}
	
	

}
