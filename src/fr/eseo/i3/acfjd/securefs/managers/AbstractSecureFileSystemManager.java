package fr.eseo.i3.acfjd.securefs.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import fr.eseo.i3.acfjd.securefs.ui.SecureFileSystemUI;
import fr.eseo.i3.acfjd.securefs.ui.components.PasswordInputDialog;

public abstract class AbstractSecureFileSystemManager {

	private SecureFileSystemUI ui;
	
	public void setSecureFileSystemUI(SecureFileSystemUI ui) {
		this.ui = ui;
	}
	
	public SecureFileSystemUI getSecureFileSystemUI() {
		return this.ui;
	}
	
	public boolean authorise() {
		boolean result = false;
		final PasswordInputDialog passwordDialog = new PasswordInputDialog(this.getSecureFileSystemUI(), this);
		result = passwordDialog.isPasswordValid();
		return result;
	}
	
	public abstract void delete(File [] files);
	
	public abstract void encryptDecrypt(File [] files);
	
	public abstract boolean isPasswordCorrect(String user, char[] password);
	
	public abstract void sign(File[] files) throws NoSuchAlgorithmException;
	public abstract void verify(File[] files);
	public abstract void displayLog();
	public abstract void displayStatus();
	public abstract void createDigest(File [] files) throws NoSuchAlgorithmException;
	public abstract void verifyDigest(File [] files);
}
