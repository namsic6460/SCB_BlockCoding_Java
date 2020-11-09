package scb.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import scb.base.Base;
import scb.base.Config;
import scb.base.Config.UNIT_TYPE;
import scb.basicbot.BotMain;

@SuppressWarnings("serial")
public class AttackCheckForm extends Base {
	
	Font font = new Font("나눔 고딕", Font.BOLD, 25);
	Font engFont = new Font("Times New Roman", Font.BOLD, 18);
	Font verticalFont = new Font("Times New Roman", Font.BOLD, 18).deriveFont(AffineTransform.getRotateInstance((0.45 * Math.PI)));
			
	public AttackCheckForm() {
		super("스타크래프트 봇 프로그래밍", 500, 640, 0, 0);
		
		add(createLabel("공격 타이밍 설정", JLabel.LEFT, new Font("나눔 고딕", Font.BOLD, 40)), BorderLayout.NORTH);
		
		//Center
		JPanel cp = new JPanel(new BorderLayout());
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JPanel c_cp = new JPanel(new BorderLayout());
		JPanel c_cp2 = new JPanel(new GridLayout(5, 1, 0, 15));
		c_cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JPanel innerPanel = null;
		for(int i = 0; i < 5; i++) {
			innerPanel = new JPanel(new FlowLayout());
			innerPanel.setBorder(new LineBorder(Color.black));
			
			JComboBox<String> comboBox = new JComboBox<>(
					Config.unitList.get(Config.tribe).get(UNIT_TYPE.UNIT).toArray(new String[0]));
			comboBox.setPreferredSize(new Dimension(270, 60));
			comboBox.setFont(font);
			comboBox.setSelectedIndex(-1);
			comboBox.setBorder(new EmptyBorder(4, 0, 0, 0));
			comboBox.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					Config.playSound("btnClick.wav");
				}
			});
			Config.comboBoxes.add(comboBox);
			innerPanel.add(comboBox);
			
			JPanel countPanel = new JPanel(new FlowLayout());
			
			JLabel numLabel = createLabel("0", JLabel.CENTER, engFont);
			Config.numLabels.add(numLabel);
			countPanel.add(numLabel);
			
			JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
			
			JButton button = createStarButton("▲", upBtnClicked(numLabel));
			button.setFont(engFont);
			buttonPanel.add(button);
			
			button = createStarButton("▼", downBtnClicked(numLabel));
			button.setFont(engFont);
			buttonPanel.add(button);
			
			countPanel.add(buttonPanel);
			innerPanel.add(countPanel);
			
			c_cp2.add(innerPanel);
		}
		
		c_cp.add(c_cp2);
		cp.add(c_cp);
		add(cp, BorderLayout.CENTER);
		
		//East
		JPanel ep = new JPanel(new BorderLayout());
		ep.setBorder(new EmptyBorder(10, 0, 10, 5));
		ep.setOpaque(false);
		
		JButton button = createStarButton("<html>B<br>A<br>C<br>k</html>", backBtnClicked());
		button.setFont(verticalFont);
		button.setPreferredSize(new Dimension(75, 250));
		ep.add(button, BorderLayout.NORTH);
		
		button = createStarButton("<html>C<br>O<br>M<br>P<br>L<br>E<br>T<br>E</html>", completeBtnClicked());
		button.setFont(verticalFont);
		button.setPreferredSize(new Dimension(75, 250));
		ep.add(button, BorderLayout.SOUTH);
		
		add(ep, BorderLayout.EAST);
	}

	private ActionListener completeBtnClicked() {
		return new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.playSound("btnClick.wav");
				
				Boolean flag = false;
				int num;
				for(int i = 0; i < 5; i++) {
					num = Integer.parseInt(Config.numLabels.get(i).getText());
					
					if(Config.comboBoxes.get(i).getSelectedIndex() != -1) {
						if(num == 0) {
							flag = false;
							break;
						}
						
						else
							flag = true;
					}
				}
				
				if(!flag)
					JOptionPane.showMessageDialog(Config.frames.get(1), "공격 타이밍이 비어있습니다", "경고!", JOptionPane.WARNING_MESSAGE);
				
				else {
					JFrame frame = new GameForm();
					frame.setVisible(true);
					
					for (int i = 0; i < 2; i++) {
						frame = Config.frames.get(0);
						frame.dispose();
						Config.frames.remove(0);
					}
					
					try {
						Runtime.getRuntime().exec(Config.CHAOS_PATH);
					} catch (IOException e_) {
						throw new RuntimeException(e_);
					}
					
					new Thread(new Runnable() {						
						@Override
						public void run() {
							BotMain.main();
						}
					}).start();
				}
			}
		};
	}

	private ActionListener backBtnClicked() {
		return new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.playSound("btnClick.wav");
				
				JFrame frame = Config.frames.get(0);
				frame.setVisible(true);
				
				frame = Config.frames.get(1);
				frame.dispose();
				Config.frames.remove(1);
			}
		};
	}

	private ActionListener upBtnClicked(JLabel numLabel) {
		return new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.playSound("btnClick.wav");
				
				int number = Integer.parseInt(numLabel.getText()) + 1;
				numLabel.setText(Integer.toString(number));
			}
		};
	}
	
	private ActionListener downBtnClicked(JLabel numLabel) {
		return new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.playSound("btnClick.wav");
				
				int number = Integer.parseInt(numLabel.getText()) - 1;
				number = number < 0 ? 0 : number;
				numLabel.setText(Integer.toString(number));
			}
		};
	}

}
