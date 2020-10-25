package scb.base;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Config {

	public final static String IMAGE_PATH = System.getProperty("user.dir") + "\\src\\scb\\image\\";
	public final static String SOUND_PATH = System.getProperty("user.dir") + "\\src\\scb\\sound\\";
	
	public static Font starFont = new Font("Times New Roman", Font.BOLD, 40);
	public static Color starForeColor = new Color(10, 110, 10);
	public static Color starBackColor = new Color(5, 5, 5);
	public static LineBorder starLineBorder = new LineBorder(new Color(128, 0, 0), 3);
	public static ImageIcon zerglingImageIcon = new ImageIcon(Config.IMAGE_PATH + "zergling.jpg");
	public static ImageIcon mainImageIcon = new ImageIcon(Config.IMAGE_PATH + "SCB_edit.png");

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

	public static synchronized void playSound(final String url) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					InputStream in = new FileInputStream(SOUND_PATH + url);
					AudioStream as = new AudioStream(in);
					AudioPlayer.player.start(as);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		thread.start();
	}

}
