package scb.base;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Base extends JFrame {
	
	public Base(String name, int width, int height, int wgap, int hgap) {
		Config.frames.add(this);
		
		setTitle(name);
		setIconImage(Config.zerglingImageIcon.getImage());
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(3);
//		setResizable(false);
		setLayout(new BorderLayout(wgap, hgap));
	}
	
	public JLabel createLabel(String text, int alignment, Font font) {
		JLabel jl = new JLabel();
		jl.setText(text);
		jl.setHorizontalAlignment(alignment);
		jl.setFont(font);
		return jl;
	}
	
	public JButton createButton(String text, Font font, ActionListener e) {
		JButton button = new JButton(text);
		button.setFont(font);
		button.addActionListener(e);
		button.setHorizontalAlignment(JButton.CENTER);
		button.setVerticalAlignment(JButton.CENTER);
		
		return button;
	}
	
	public JButton createStarButton(String text, ActionListener e) {
		JButton button = this.createButton(text, Config.starFont, e);
		button.setForeground(Config.starForeColor);
		button.setBackground(Config.starBackColor);
		button.setBorder(Config.starLineBorder);
		
		return button;
	}
	
}
