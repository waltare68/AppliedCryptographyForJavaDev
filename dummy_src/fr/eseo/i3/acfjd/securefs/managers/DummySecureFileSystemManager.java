package fr.eseo.i3.acfjd.securefs.managers;

import java.io.File;

import javax.swing.JOptionPane;

import fr.eseo.i3.acfjd.securefs.i18n.I18n;

public class DummySecureFileSystemManager extends AbstractSecureFileSystemManager{

	@Override
	public void delete(File[] files) {
		JOptionPane.showMessageDialog(this.getSecureFileSystemUI().getWindow(), I18n.getI18n("DummySecureFileSystemManager.Delete"),I18n.getI18n("Application.Title"),JOptionPane.INFORMATION_MESSAGE);
		
		
	}

	@Override
	public void encryptDecrypt(File[] files) {
		JOptionPane.showMessageDialog(this.getSecureFileSystemUI().getWindow(), I18n.getI18n("DummySecureFileSystemManager.EncDec"),I18n.getI18n("Application.Title"),JOptionPane.INFORMATION_MESSAGE);
		
	}

	@Override
	public boolean isPasswordCorrect(String user, char[] password) {
		JOptionPane.showMessageDialog(this.getSecureFileSystemUI().getWindow(), String.format(I18n.getI18n("DummySecureFileSystemManager.Login"), user, new String(password)),I18n.getI18n("Application.Title"),JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

	@Override
	public void sign(File[] files) {
		JOptionPane.showMessageDialog(this.getSecureFileSystemUI().getWindow(), I18n.getI18n("DummySecureFileSystemManager.ApplySignature"),I18n.getI18n("Application.Title"),JOptionPane.INFORMATION_MESSAGE);
		
	}

	@Override
	public void verify(File[] files) {
		JOptionPane.showMessageDialog(this.getSecureFileSystemUI().getWindow(), I18n.getI18n("DummySecureFileSystemManager.VerifySignature"),I18n.getI18n("Application.Title"),JOptionPane.INFORMATION_MESSAGE);
		
	}

	@Override
	public void displayLog() {
		JOptionPane.showMessageDialog(this.getSecureFileSystemUI().getWindow(), I18n.getI18n("DummySecureFileSystemManager.DisplayLog"),I18n.getI18n("Application.Title"),JOptionPane.INFORMATION_MESSAGE);
		
	}

	@Override
	public void displayStatus() {
		JOptionPane.showMessageDialog(this.getSecureFileSystemUI().getWindow(), I18n.getI18n("DummySecureFileSystemManager.DisplayStatus"),I18n.getI18n("Application.Title"),JOptionPane.INFORMATION_MESSAGE);
		
	}

	@Override
	public void createDigest(File[] files) {
		JOptionPane.showMessageDialog(this.getSecureFileSystemUI().getWindow(), I18n.getI18n("DummySecureFileSystemManager.CreateDigest"),I18n.getI18n("Application.Title"),JOptionPane.INFORMATION_MESSAGE);
		
	}

	@Override
	public void verifyDigest(File[] files) {
		JOptionPane.showMessageDialog(this.getSecureFileSystemUI().getWindow(), I18n.getI18n("DummySecureFileSystemManager.VerifyDigest"),I18n.getI18n("Application.Title"),JOptionPane.INFORMATION_MESSAGE);
		
	}

}
