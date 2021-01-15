package fr.eseo.i3.acfjd.securefs.ui.components;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import fr.eseo.i3.acfjd.securefs.i18n.I18n;
import fr.eseo.i3.acfjd.securefs.ui.SecureFileSystemUI;

public class StatusBar extends JPanel implements TreeSelectionListener{

	private static final ImageIcon LOG = new ImageIcon(StatusBar.class.getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/log.png"));
	private static final ImageIcon STATUS = new ImageIcon(StatusBar.class.getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/status.png"));
		
	private Action displayLog;
	private Action displayStatusInfo;
	private JButton btnLog;
	private JButton btnStatus;
	
	private final SecureFileSystemUI ui;
	
	public StatusBar(SecureFileSystemUI ui) {
		this.ui = ui;
		initActions();
		initComponents();
		layoutComponents();
	}
	
	private void initActions() {
		this.displayLog = new AbstractAction(I18n.getI18n("Actions.DisplayLog"),StatusBar.LOG) {
			@Override
			public void actionPerformed(ActionEvent event) {
				StatusBar.this.ui.getManager().displayLog();
			}
		};
		this.displayStatusInfo = new AbstractAction(I18n.getI18n("Actions.DisplayStatus"),StatusBar.STATUS) {
			@Override
			public void actionPerformed(ActionEvent event) {
				StatusBar.this.ui.getManager().displayStatus();
			}
		};
		
	}
	private void initComponents() {
	    this.btnLog = new JButton(this.displayLog);
	    this.btnStatus = new JButton(this.displayStatusInfo);
	    this.btnStatus.setBorderPainted(false);
	    this.btnStatus.setText("");
	    this.btnStatus.setContentAreaFilled(false);
	    this.btnStatus.setFocusPainted(false);
	    this.btnStatus.setFocusable(false);
	    this.btnStatus.setHorizontalAlignment(SwingConstants.LEFT);
	    this.btnStatus.setOpaque(false);
	    this.btnStatus.setRolloverEnabled(false);
	    this.btnStatus.setEnabled(false);

	  }

	  private void layoutComponents() {
	    final GroupLayout layout = new GroupLayout(this);
	    this.setLayout(layout);
	    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	            .addComponent(this.btnStatus, GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
	            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.btnLog)));
	    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	            .addComponent(this.btnLog).addComponent(this.btnStatus, GroupLayout.PREFERRED_SIZE, 25,
	                GroupLayout.PREFERRED_SIZE)));
	  }

	  @Override
	  public void valueChanged(TreeSelectionEvent event) {
	    final SecureFileSystemTree tree = (SecureFileSystemTree) event.getSource();
	    final int selectCount = tree.getSelectionCount();
	    if (selectCount == 0) {
	      this.btnStatus.setEnabled(false);
	      this.btnStatus.setText("");
	    } else {
	      this.btnStatus.setEnabled(true);
	      final StringBuilder sb = new StringBuilder();
	      TreePath path = event.getPaths()[0];
	      if (path != null) {
	        sb.append(path.getLastPathComponent().toString());
	      }
	      for (int i = 1; i < event.getPaths().length; i++) {
	        path = event.getPaths()[i];
	        if (path != null) {
	          sb.append(", ");
	          sb.append(path.getLastPathComponent().toString());
	        }
	      }
	      this.btnStatus.setText(sb.toString());
	    }
	  }
	
	
}
