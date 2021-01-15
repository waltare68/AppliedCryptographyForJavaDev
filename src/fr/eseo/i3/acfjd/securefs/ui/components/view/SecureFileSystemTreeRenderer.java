package fr.eseo.i3.acfjd.securefs.ui.components.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

public class SecureFileSystemTreeRenderer extends JLabel implements TreeCellRenderer{

	
	private final Color textSelectionColour;
	private final Color textNonSelectionColour;
	private final Color backSelectionColour;
	private final Color backNonSelectionColour;
	
	private boolean isSelected;
	
	private LinearGradientPaint selectionFade;
	
	private static final ImageIcon FOLDER_SHUT = new ImageIcon(SecureFileSystemTreeRenderer.class.getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/folder_shut.png"));
	private static final ImageIcon FOLDER_OPEN = new ImageIcon(SecureFileSystemTreeRenderer.class.getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/folder_open.png"));
	private static final ImageIcon FILE = new ImageIcon(SecureFileSystemTreeRenderer.class.getClassLoader().getResource("fr/eseo/i3/acfjd/securefs/ui/components/res/file.png"));
		
	
	public SecureFileSystemTreeRenderer() {
		super();
		this.textSelectionColour = UIManager.getColor("Tree.selectionForeground");
		this.textNonSelectionColour = UIManager.getColor("Tree.textForeground");
		this.backSelectionColour = UIManager.getColor("Tree.selectionBackground");
		this.backNonSelectionColour = UIManager.getColor("Tree.textBackground");
		
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		final File file = (File)value;
		final String componentLabel = file.getName();
		this.setText(componentLabel);
		this.setFont(tree.getFont());
		this.setForeground(selected? this.textSelectionColour : this.textNonSelectionColour);
		this.setBackground(selected? this.backSelectionColour : this.backNonSelectionColour);
		if(file.isDirectory()) {
			if(expanded) {
				this.setIcon(SecureFileSystemTreeRenderer.FOLDER_OPEN);
			}
			else {
				this.setIcon(SecureFileSystemTreeRenderer.FOLDER_SHUT);
			}
		}
		else {
			this.setIcon(SecureFileSystemTreeRenderer.FILE);
		}
		this.isSelected = selected;
		return this;
		
	}

	@Override
	public void paintComponent(Graphics g) {
		final Graphics2D g2 = (Graphics2D)g.create();
		final Color backColour = this.getBackground();
		final Icon icon = this.getIcon();
		int offset = 0;
		if(icon!=null && this.getText()!=null) {
			if(this.isSelected) {
				offset = (icon.getIconWidth()+this.getIconTextGap());
				selectionFade = new LinearGradientPaint(new Point2D.Double(offset-2,this.getHeight()/2),new Point2D.Double(this.getWidth()+offset,this.getHeight()/2),new float[] {0.0f,0.1f},new Color[] {this.backSelectionColour,this.backNonSelectionColour});
				g2.setPaint(this.selectionFade);
				g2.fillRoundRect(offset -2, 1, (this.getWidth()-offset)+4,this.getHeight()-2,6,6);
				selectionFade = new LinearGradientPaint(new Point2D.Double(offset-2,this.getHeight()/2),new Point2D.Double(this.getWidth()+offset,this.getHeight()/2),new float[] {0.0f,0.1f},new Color[] {this.backSelectionColour.darker().darker(),this.backNonSelectionColour});
				g2.setPaint(this.selectionFade);
				g2.drawRoundRect(offset -2, 1, (this.getWidth()-offset)+4,this.getHeight()-2,6,6);
				
			}
		}
		g2.dispose();
		super.paintComponent(g);
	}
}
