package fr.eseo.i3.acfjd.securefs.ui.components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

import fr.eseo.i3.acfjd.securefs.i18n.I18n;
import fr.eseo.i3.acfjd.securefs.managers.AbstractSecureFileSystemManager;
import fr.eseo.i3.acfjd.securefs.ui.SecureFileSystemUI;

public class PasswordInputDialog extends JDialog{

	private final AbstractSecureFileSystemManager manager;
	
	private boolean passwordValid;
	
	public PasswordInputDialog(SecureFileSystemUI parent, AbstractSecureFileSystemManager manager) {
		super(parent.getWindow(), true);
		this.manager = manager;
		this.setTitle(I18n.getI18n("PasswordInputDialog.LogonTitle"));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.add(new PasswordInputPanel(this));
		this.pack();
		this.setLocationRelativeTo(parent.getWindow());
		this.setVisible(true);
	}
	
	public AbstractSecureFileSystemManager getManager() {
		return manager;
	}
	
	public boolean isPasswordValid(String username, char[] password) {
		this.passwordValid = this.getManager().isPasswordCorrect(username, password);
		return this.passwordValid;
	}
	
	public boolean isPasswordValid() {
		return this.passwordValid;
	}
	
	private class PasswordInputPanel extends JPanel {
		private final PasswordInputDialog passwordInputDialog;
		private JLabel icon;
		private JLabel lblPassword;
		private JLabel lblUsername;
		private JPasswordField pwdPassword;
		private JTextField txtUsername;
		private JButton btnOk;
		private JButton btnCancel;
		private JLabel txtAbout;
		
		public PasswordInputPanel(PasswordInputDialog passwordInputDialog) {
			this.passwordInputDialog = passwordInputDialog;
			this.initComponents();
			this.layoutComponents();
		}
		
		private void initComponents() {
			this.icon = new JLabel("");
			this.lblUsername = new JLabel(I18n.getI18n("PasswordInputDialog.UsernameLabel"));
			this.lblPassword = new JLabel(I18n.getI18n("PasswordInputDialog.PasswordLabel"));
			this.pwdPassword = new JPasswordField();
			this.txtUsername = new JTextField();
			this.btnOk = new JButton(I18n.getI18n("PasswordInputDialog.OkButton"));
			this.btnCancel = new JButton(I18n.getI18n("PasswordInputDialog.CancelButton"));
			this.icon.setMaximumSize(new Dimension(86,86));
			this.icon.setMaximumSize(new Dimension(86,86));
			this.icon.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/encrypt_large.png")));
			this.txtAbout = new JLabel(System.getProperty("java.version")+"\t"+System.getProperty("java.vendor"));
			txtAbout.setFont(new Font("SansSerif",Font.ITALIC,8));
			this.txtUsername.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent event) {
					PasswordInputPanel.this.pwdPassword.requestFocusInWindow();
					
				}
			});
			this.pwdPassword.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent event) {
					PasswordInputPanel.this.btnOk.requestFocusInWindow();
					
				}
			});
			
			this.btnOk.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent event) {
					PasswordInputPanel.this.processUserPassword();
					
				}
			});
			
			this.btnCancel.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent event) {
					PasswordInputPanel.this.zeroOutPassword();
					System.exit(0);
				}
			});
		}
		
		private void layoutComponents() {
			final GroupLayout layout = new GroupLayout(this);
			this.setLayout(layout);
			layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			          .addGroup(layout.createSequentialGroup().addContainerGap()
			              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			                  .addGroup(layout.createSequentialGroup()
			                      .addComponent(this.icon, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
			                          GroupLayout.PREFERRED_SIZE)
			                      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			                      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			                          .addGroup(layout.createSequentialGroup()
			                              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			                                  .addComponent(this.lblUsername).addComponent(this.lblPassword))
			                              .addGap(0, 0, Short.MAX_VALUE))
			                          .addGroup(layout.createSequentialGroup().addGap(12, 12, 12)
			                              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			                                  .addComponent(this.txtUsername).addComponent(this.pwdPassword,
			                                      GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)))))
			                  .addGroup(GroupLayout.Alignment.TRAILING,
			                      layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
			                      	  .addComponent(txtAbout)
			                          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			                          .addComponent(this.btnOk)
			                          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			                          .addComponent(this.btnCancel)))
			              .addContainerGap()));
			      layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			          .addGroup(layout.createSequentialGroup().addContainerGap()
			              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
			                  .addComponent(this.icon, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
			                      Short.MAX_VALUE)
			                  .addGroup(layout.createSequentialGroup().addComponent(this.lblUsername)
			                      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			                      .addComponent(this.txtUsername, GroupLayout.PREFERRED_SIZE,
			                          GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			                      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			                      .addComponent(this.lblPassword)
			                      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			                      .addComponent(this.pwdPassword, GroupLayout.PREFERRED_SIZE,
			                          GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
			              .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
			                  .addComponent(this.btnCancel).addComponent(this.btnOk).addComponent(txtAbout))
			              .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		}
		
		private void zeroOutPassword() {
			Arrays.fill(this.pwdPassword.getPassword(), '0');
			this.pwdPassword.setText("");;
			this.pwdPassword.selectAll();
			this.pwdPassword.requestFocusInWindow();
		}
		
		private void processUserPassword() {
			if(this.txtUsername.getText()==null || this.txtUsername.getText().equals("")) {
				this.zeroOutPassword();
				JOptionPane.showMessageDialog(this, I18n.getI18n("PasswordInputDialog.ErrorTitle"),I18n.getI18n("PasswordInputDialog.ErrorMessage"),JOptionPane.ERROR_MESSAGE);
				
			}else if(this.passwordInputDialog.isPasswordValid(this.txtUsername.getText(), this.pwdPassword.getPassword())) {
				this.zeroOutPassword();
				this.passwordInputDialog.setVisible(false);
			}
			else {
				this.zeroOutPassword();
				JOptionPane.showMessageDialog(this, I18n.getI18n("PasswordInputDialog.ErrorTitle"),I18n.getI18n("PasswordInputDialog.ErrorMessage"),JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
}
