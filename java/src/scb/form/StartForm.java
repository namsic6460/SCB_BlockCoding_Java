package scb.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import scb.base.Base;
import scb.base.Config;
import scb.base.Main;
import scb.base.Config.TRIBE;

@SuppressWarnings("serial")
public class StartForm extends Base {
	
	public StartForm() {
		super("스타크래프트 봇 프로그래밍", 960, 620, 0, 0);
		JFrame frame = this;
		
		ImageIcon icon = new ImageIcon(Config.IMAGE_PATH + "SCB_slow.gif");
		JLabel label = new JLabel(icon);
		label.setOpaque(true);
		label.setPreferredSize(new Dimension(960, 620));
		label.setBackground(Color.white);
		label.setBorder(new EmptyBorder(0, 0, 55, 0));

		this.add(label, BorderLayout.CENTER);
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2500); 
				} catch (InterruptedException e) {
					System.out.println("error!");
					throw new RuntimeException(e);
				}
				
				initFrame(frame, label);
			}
		});
		thread.start();
	}
	
	private void initFrame(JFrame frame, JLabel label) {
		frame.remove(label);
		
		ImageIcon icon = Config.mainImageIcon;
		label = new JLabel(icon);
		label.setOpaque(true);
		label.setPreferredSize(new Dimension(960, 620));
		label.setBackground(Color.white);
		
		frame.add(label, BorderLayout.CENTER);
		
		//North
		JPanel np = new JPanel(new BorderLayout());
		np.setBackground(Color.white);
		np.add(createLabel("본 프로그램은 교육용으로 제작되었습니다", JLabel.LEFT,
				new Font("나눔고딕", Font.BOLD, 15)));
		
		frame.add(np, BorderLayout.NORTH);
		
		//South
		JPanel sp = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		sp.setBackground(Color.white);
		sp.setBorder(new EmptyBorder(0, 0, 20, 0));
		
		for(TRIBE tribe : TRIBE.values()) {
			StringBuilder builder = new StringBuilder(" ");
			builder.append(tribe.toString());
			builder.append(" ");
			
			sp.add(createStarButton(builder.toString(), btnClicked(tribe, frame)));
		}
		
		frame.add(sp, BorderLayout.SOUTH);
		
		//Refresh
		frame.validate();
	}
	
	private ActionListener btnClicked(TRIBE tribe, JFrame frame) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.tribe = tribe;
				Config.playSound("btnClick.wav");
				
				try {
					if(Main.initThread.isAlive()) {
						showDialog(null, "로딩중입니다\n잠시만 기다려주세요!", JOptionPane.WARNING_MESSAGE);
						Main.initThread.wait();
					}
				} catch (InterruptedException e_) {
					e_.printStackTrace();
				}
				
				new MainForm().setVisible(true);
				
				Config.frames.remove(frame);
				frame.dispose();
			}
		};
	}
	
}
