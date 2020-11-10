package scb.base;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import bwapi.UnitType;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Config {

	public final static String SYSTEM_PATH = System.getenv("SCB_BlockCoding");
	public final static String IMAGE_PATH = SYSTEM_PATH + "scb\\image\\";
	public final static String SOUND_PATH = SYSTEM_PATH + "scb\\sound\\";
	public final static String CHAOS_PATH = SYSTEM_PATH + "Starcraft\\BWAPI\\Chaoslauncher\\Chaoslauncher - MultiInstance.exe";
	
	public static LinkedList<JButton> buildList = new LinkedList<>();
	public static LinkedHashMap<UnitType, Integer> attackTiming = new LinkedHashMap<>();
	
	public static Font starFont = new Font("Times New Roman", Font.BOLD, 40);
	public static Color starForeColor = new Color(10, 110, 10);
	public static Color starBackColor = new Color(5, 5, 5);
	public static LineBorder starLineBorder = new LineBorder(new Color(128, 0, 0), 3);
	public static ImageIcon zerglingImageIcon = new ImageIcon(Config.IMAGE_PATH + "zergling.jpg");
	public static ImageIcon mainImageIcon = new ImageIcon(Config.IMAGE_PATH + "SCB_edit.png");
	
	public static ArrayList<JLabel> numLabels;
	public static ArrayList<JComboBox<String>> comboBoxes;
	
	public static AudioStream audioStream = null;

	public enum TRIBE {
		TERRAN, PROTOSS, ZERG
	}
	
	public enum UNIT_TYPE {
		UNIT, BUILDING, UPGRADE
	}

	public static Boolean stopThread = false;
	public static ArrayList<JFrame> frames = new ArrayList<>();
	public static TRIBE tribe;
	
	//Init in Main
	public static LinkedHashMap<TRIBE, LinkedHashMap<UNIT_TYPE, ArrayList<String>>> unitList = new LinkedHashMap<>();
	public static LinkedHashMap<String, String> toString = new LinkedHashMap<>();
	public static LinkedHashMap<String, Object> toObject = new LinkedHashMap<>();

	public static synchronized void playSound(final String url) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					InputStream inputStream = new FileInputStream(SOUND_PATH + url);
					audioStream = new AudioStream(inputStream);
					AudioPlayer.player.start(audioStream);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		thread.start();
	}

}
