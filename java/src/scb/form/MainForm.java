package scb.form;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import scb.base.Base;
import scb.base.Config;
import scb.base.Config.TRIBE;
import scb.base.Config.UNIT_TYPE;

@SuppressWarnings("serial")
public class MainForm extends Base {
	
	JPanel selectPanel = new JPanel(new FlowLayout());
	JPanel boardPanel = new JPanel(new BorderLayout());
	JScrollPane boardPane;
	
	Font verticalFont = new Font("Times New Roman", Font.BOLD, 18).deriveFont(AffineTransform.getRotateInstance((0.45 * Math.PI)));
	TRIBE selectedTribe = Config.tribe;
	UNIT_TYPE selectedUnitType = UNIT_TYPE.UNIT;
	JButton selectedBtn;
	
	LinkedHashMap<TRIBE, JButton> tribeBtns = new LinkedHashMap<>();
	LinkedHashMap<UNIT_TYPE, JButton> typeBtns = new LinkedHashMap<>();
	LinkedHashMap<TRIBE, LinkedHashMap<UNIT_TYPE, JScrollPane>> selectPanes = new LinkedHashMap<>();
	LinkedHashMap<String, JButton> selectBtns = new LinkedHashMap<>();
	
	//20개마다 새로운 LinkedList생성 - 검색뿐만 아니라 추가/삭제도 빈번하기 떄문
	ArrayList<LinkedList<JButton>> buildList = new ArrayList<>();
	int list1Idx = 0;	//0~~
	int list2Idx = 0;	//0~19
	
	public MainForm() {
		super("스타크래프트 봇 프로그래밍", 960, 620, 0, 0);
		
		//West
		JPanel wp = new JPanel(new BorderLayout());
		wp.setPreferredSize(new Dimension(340, 620));

		JPanel tribeSelectPanel = new JPanel(new FlowLayout());
		tribeSelectPanel.setPreferredSize(new Dimension(50, 580));
		for(TRIBE tribe : TRIBE.values())
			createTribeBtn(tribeSelectPanel, tribe);
		wp.add(tribeSelectPanel, BorderLayout.WEST);
		
		
		JPanel typeSelectPanel = new JPanel(new FlowLayout());
		typeSelectPanel.setPreferredSize(new Dimension(50, 580));
		for(UNIT_TYPE unitType : UNIT_TYPE.values())
			createTypeBtn(typeSelectPanel, unitType);
		wp.add(typeSelectPanel, BorderLayout.CENTER);
		
		
		selectPanel.setPreferredSize(new Dimension(225, 580));
		selectPanel.setBorder(new LineBorder(Color.black));
		for(TRIBE tribe : TRIBE.values()) {
			selectPanes.put(tribe, new LinkedHashMap<>());
			
			for(UNIT_TYPE unitType : UNIT_TYPE.values())
				createSelectPanel(tribe, unitType);
		}
		wp.add(selectPanel, BorderLayout.EAST);
		
		this.add(wp, BorderLayout.WEST);
		
		
		//Center
		JPanel cp = null;
		try {
			final BufferedImage background = ImageIO.read(new File(Config.IMAGE_PATH + "background.png"));

			cp = new JPanel(new BorderLayout()) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					if (background != null) {
						Graphics2D g2 = (Graphics2D) g.create();
						g2.setComposite(AlphaComposite.SrcOver.derive(0.25f));
						g2.drawImage(background.getScaledInstance(960, 620, BufferedImage.TYPE_4BYTE_ABGR), 0, 0, this);
						g2.dispose();
					}
				}
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
		cp.setPreferredSize(new Dimension(660, 580));
		cp.setBackground(Color.lightGray);

		buildList.add(new LinkedList<>());
		initCenterPanel(cp);
		
		this.add(cp, BorderLayout.CENTER);
	}

	private void initCenterPanel(JPanel cp) {
		if(cp == null)
			return;
		
		//North
		JPanel tempPanel1 = new JPanel(new BorderLayout());
		tempPanel1.setBorder(new EmptyBorder(5, 5, 5, 5));
		tempPanel1.setBackground(new Color(255, 0, 0, 0));
		
		JPanel tempPanel2 = new JPanel(new BorderLayout());
		tempPanel2.setBackground(new Color(255, 0, 0, 0));
		
		JLabel label = createLabel("Start!", JLabel.CENTER, new Font("Times New Roman", Font.BOLD, 25));
		label.setBounds(10, 10, 100, 35);
		label.setHorizontalAlignment(JLabel.LEFT);
		tempPanel2.add(label, BorderLayout.NORTH);
		
		boardPanel.setBorder(new LineBorder(Color.black));
		boardPane = new JScrollPane(boardPane);
		boardPane.setOpaque(false);
		boardPane.getViewport().setOpaque(false);
		boardPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		boardPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		tempPanel2.add(boardPane, BorderLayout.CENTER);
		
		tempPanel1.add(tempPanel2, BorderLayout.CENTER);
		cp.add(tempPanel1, BorderLayout.CENTER);
		
		//South
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		panel.setBackground(new Color(255, 0, 0, 0));
		Font font = new Font("나눔 고딕", Font.BOLD, 20);
		Dimension dimension = new Dimension(75, 35);
		
		JButton button = createStarButton("◀", leftBtnClicked());
		button.setPreferredSize(dimension);
		button.setFont(font);
		panel.add(button);
		
		button = createStarButton("-", minusBtnClicked());
		button.setPreferredSize(dimension);
		button.setFont(font);
		panel.add(button);
		
		button = createStarButton("+", plusBtnClicked());
		button.setPreferredSize(dimension);
		button.setFont(font);
		panel.add(button);
		
		button = createStarButton("▶", rightBtnClicked());
		button.setPreferredSize(dimension);
		button.setFont(font);
		panel.add(button);
		
		cp.add(panel, BorderLayout.SOUTH);
	}
	
	private ActionListener leftBtnClicked() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.playSound("btnClick.wav");
				
				if(list2Idx == 0) {
					if(list1Idx != 0) {
						buildList.get(list1Idx--).get(list2Idx).setBackground(Color.darkGray);

						list2Idx = 19;
						buildList.get(list1Idx).get(list2Idx).setBackground(Config.starBackColor);
					}
				}
				
				else {
					buildList.get(list1Idx).get(list2Idx--).setBackground(Color.darkGray);
					buildList.get(list1Idx).get(list2Idx).setBackground(Config.starBackColor);
				}
			}
		};
	}
	
	private ActionListener minusBtnClicked() {
		return new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.playSound("btnClick.wav");
			}
		};
	}
	
	private ActionListener plusBtnClicked() {
		return new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.playSound("btnClick.wav");
				
				JButton button = createStarButton(selectedBtn.getText(), buildBtnClicked());
			}
		};
	}
	
	private ActionListener buildBtnClicked() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.playSound("btnClick.wav");
			}
		};
	}

	private ActionListener rightBtnClicked() {
		return new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.playSound("btnClick.wav");
				
				if(list2Idx != 19 && buildList.get(list1Idx).size() > list2Idx) {
					buildList.get(list1Idx).get(list2Idx++).setBackground(Color.darkGray);
					buildList.get(list1Idx).get(list2Idx).setBackground(Config.starBackColor);
				}
				
				else {
					if(list1Idx + 1 != buildList.size()) {
						buildList.get(list1Idx++).get(list2Idx).setBackground(Color.darkGray);
						
						list2Idx = 0;
						buildList.get(list1Idx).get(list2Idx).setBackground(Config.starBackColor);
					}
				}
			}
		};
	}

	private void createTribeBtn(JPanel tribeSelectPanel, TRIBE tribe) {
		StringBuilder builder = new StringBuilder("<html>");
		String tribeStr = tribe.toString();
		for(int i = 0; i < tribeStr.length(); i++) {
			builder.append(tribeStr.charAt(i));
			builder.append("<br>");
		}
		builder.append("</html>");
		
		JButton button = createStarButton(builder.toString(), tribeBtnClicked(tribe));
		
		button.setFont(verticalFont);
		button.setPreferredSize(new Dimension(45, 190));
		
		if(!tribe.equals(Config.tribe))
			button.setBackground(Color.darkGray);
		
		tribeBtns.put(tribe, button);		
		tribeSelectPanel.add(button);
	}

	private ActionListener tribeBtnClicked(TRIBE newTribe) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.playSound("btnClick.wav");
				if(selectedTribe.equals(newTribe))
					return;
				
				for(TRIBE t : TRIBE.values()) {
					if(t.equals(newTribe))
						tribeBtns.get(t).setBackground(Config.starBackColor);
					else
						tribeBtns.get(t).setBackground(Color.DARK_GRAY);
				}

				changeSelectPanel(newTribe, selectedUnitType);
				changeSelectBtn(Config.unitList.get(newTribe).get(selectedUnitType).get(0));
				selectedTribe = newTribe;
			}
		};
	}
	
	private void createTypeBtn(JPanel typeSelectPanel, UNIT_TYPE unitType) {
		StringBuilder builder = new StringBuilder("<html>");
		String unitTypeStr = unitType.toString();
		for(int i = 0; i < unitTypeStr.length(); i++) {
			builder.append(unitTypeStr.charAt(i));
			builder.append("<br>");
		}
		builder.append("</html>");
		
		JButton button = createStarButton(builder.toString(), typeBtnClicked(unitType));
		
		button.setFont(verticalFont);
		button.setPreferredSize(new Dimension(45, 190));
		
		if(!unitType.equals(UNIT_TYPE.UNIT))
			button.setBackground(Color.DARK_GRAY);

		typeBtns.put(unitType, button);
		typeSelectPanel.add(button);
	}

	private ActionListener typeBtnClicked(UNIT_TYPE newUnitType) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.playSound("btnClick.wav");
				if(newUnitType.equals(selectedUnitType))
					return;
				
				for(UNIT_TYPE u : UNIT_TYPE.values()) {
					if(u.equals(newUnitType))
						typeBtns.get(u).setBackground(Config.starBackColor);
					else
						typeBtns.get(u).setBackground(Color.DARK_GRAY);
				}

				changeSelectPanel(selectedTribe, newUnitType);
				changeSelectBtn(Config.unitList.get(selectedTribe).get(newUnitType).get(0));
				selectedUnitType = newUnitType;
			}
		};
	}
	
	private void createSelectPanel(TRIBE tribe, UNIT_TYPE unitType) {
		JPanel panel = new JPanel(new GridLayout(Config.unitList.get(tribe).get(unitType).size(), 1, 10, 10));;
		panel.setBorder(new EmptyBorder(0, 5, 0, 5));
		
		Font font = new Font("나눔고딕", Font.BOLD, 18);
		JButton button = null;
		for(String str : Config.unitList.get(tribe).get(unitType)) {			
			button = createStarButton(str, selectBtnClicked(str));
			button.setPreferredSize(new Dimension(175, 35));
			button.setFont(font);
			button.setBackground(Color.DARK_GRAY);
			
			panel.add(button);
			selectBtns.put(str, button);
		}
		
		if(tribe.equals(selectedTribe) && unitType.equals(UNIT_TYPE.UNIT)) {			
			button = selectBtns.get(Config.unitList.get(tribe).get(unitType).get(0));
			button.setBackground(Config.starBackColor);
			selectedBtn = button;
		}
		
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(220, 580));
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		if(tribe.equals(selectedTribe) && unitType == UNIT_TYPE.UNIT)
			selectPanel.add(scrollPane);
		
		selectPanes.get(tribe).put(unitType, scrollPane);
	}

	private ActionListener selectBtnClicked(String str) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Config.playSound("btnClick.wav");
				changeSelectBtn(str);
			}
		};
	}
	
	private void changeSelectPanel(TRIBE newTribe, UNIT_TYPE newUnitType) {
		JScrollPane newScrollPane = selectPanes.get(newTribe).get(newUnitType);
		
		selectPanel.removeAll();
		selectPanel.add(newScrollPane);
		
		selectPanel.revalidate();
		selectPanel.repaint();
	}
	
	private void changeSelectBtn(String str) {
		selectedBtn.setBackground(Color.DARK_GRAY);
		
		JButton button = selectBtns.get(str);
		button.setBackground(Config.starBackColor);
		selectedBtn = button;
	}

}
