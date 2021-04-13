package scb.form;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import scb.base.Base;
import scb.base.Config;
import sun.audio.AudioPlayer;

@SuppressWarnings("serial")
public class GameForm extends Base {
	
	private boolean isPlaying = true;
	public static TimerTask timerTask;

	public GameForm() {
		super("스타크래프트 봇 프로그래밍", 960, 640, 0, 0);
		
		//North
		JPanel np = new JPanel(new BorderLayout());
		JLabel label = createLabel("프로그램 로그", JLabel.CENTER, new Font("나눔 고딕", Font.BOLD, 25));
		np.add(label, BorderLayout.CENTER);
		
		JPanel n_ep = new JPanel(new FlowLayout());
		Font font = new Font("나눔 고딕", Font.BOLD, 15);
		
		JButton playButton = createButton("▶||", font, null);
		JButton resetButton = createButton(" ■ ", font, null);
		
		playButton.addActionListener(playBtnClicked(playButton));
		resetButton.addActionListener(resetBtnClicked());
		n_ep.add(playButton);
		n_ep.add(resetButton);
		np.add(n_ep, BorderLayout.EAST);
		
		add(np, BorderLayout.NORTH);
		
		//Center
		JPanel cp = new JPanel(new BorderLayout());
		cp.setBorder(new EmptyBorder(10, 10, 10, 10));
		CapturePane capturePane = new CapturePane();
		cp.add(capturePane);
		
		add(cp, BorderLayout.CENTER);
		
		//STDOUT, STDERR		
		System.setOut(new PrintStream(new StreamCapturer(capturePane, "black", System.out)));
		System.setErr(new PrintStream(new StreamCapturer(capturePane, "red", System.err)));
		
		System.out.println("Connecting to Broodwar...");
		
		Timer timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				System.err.println("Game table mapping not found.");
			}
		};
		timer.scheduleAtFixedRate(timerTask, 0, 1000);
		
		Config.playSound("Terran Theme.wav");
	}
	
	private ActionListener playBtnClicked(JButton playBtn) {
		return new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isPlaying) {
					AudioPlayer.player.stop(Config.audioStream);
					isPlaying = false;
				}
				
				else {
					AudioPlayer.player.start(Config.audioStream);
					isPlaying = true;	
				}
			}
		};
	}
	
	private ActionListener resetBtnClicked() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AudioPlayer.player.stop(Config.audioStream);
				
				try {
					Config.audioStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				Config.playSound("Terran Theme.wav");
				isPlaying = true;
			}
		};
	}
	
	private class CapturePane extends JPanel implements Consumer {

	    private JLabel output;
	    JScrollPane jScrollPane;

	    public CapturePane() {
	        setLayout(new BorderLayout());
	        
	        output = new JLabel("<html>");
	        output.setFont(new Font("나눔 고딕", Font.BOLD, 14));
	        output.setBorder(new EmptyBorder(5, 5, 5, 5));
	        
	        jScrollPane = new JScrollPane(output);
	        jScrollPane.setWheelScrollingEnabled(true);
	        jScrollPane.getVerticalScrollBar().setUnitIncrement(32);
	        add(jScrollPane);
	        
	        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
	        jScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
	        	@Override
	            public void adjustmentValueChanged(AdjustmentEvent e) {
	        		e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
	            }
	        });
	    }

	    @Override
	    public void appendText(final String text) {
	        if (EventQueue.isDispatchThread()) {
	            output.setText(output.getText() + text + "<br>");
	        } else {
	            EventQueue.invokeLater(new Runnable() {
	                @Override
	                public void run() {
	                    appendText(text);
	                }
	            });
	        }
	    }
	    
	}

	public interface Consumer {        
	    public void appendText(String text);        
	}
	
	private class StreamCapturer extends OutputStream {

	    private StringBuilder buffer;
	    private Consumer consumer;
	    private String spanStyle;
	    private PrintStream old;

	    public StreamCapturer(Consumer consumer, String spanStyle, PrintStream old) {
	        this.spanStyle = spanStyle;
	        buffer = new StringBuilder(128);
	        this.consumer = consumer;
	        this.old = old;
	    }

	    @Override
	    public void write(int b) throws IOException {
	        char c = (char) b;
	        buffer.append("<span style=\"color:").append(spanStyle).append("\">");
	        String value = Character.toString(c);
	        buffer.append(value);
	        buffer.append("</span>");
	        if (value.equals("\n")) {
	            consumer.appendText(buffer.toString());
	            buffer.delete(0, buffer.length());
	        }
	        old.print(c);
	    }
	    
	}
	
}
