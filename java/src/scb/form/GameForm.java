package scb.form;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import scb.base.Base;
import scb.base.Config;
import sun.audio.AudioPlayer;

@SuppressWarnings("serial")
public class GameForm extends Base {
	
	private boolean isPlaying = true;

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
		JPanel cp = new JPanel(new GridLayout(2, 1));
		CapturePane capturePane = new CapturePane();
		cp.add(capturePane);
		
		add(cp, BorderLayout.CENTER);
		
		//STDOUT, STDERR
		System.setOut(new PrintStream(new StreamCapturer("로그", capturePane, System.out)));
		System.setErr(new PrintStream(new StreamCapturer("로그", capturePane, System.err)));
		
		System.out.println("test1");
		System.err.println("test2");
		
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

	    public CapturePane() {
	        setLayout(new BorderLayout());
	        output = new JLabel("<html>");
	        JScrollPane jScrollPane = new JScrollPane(output);
	        jScrollPane.setAutoscrolls(true);
	        add(jScrollPane);
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
	    private String prefix;
	    private Consumer consumer;
	    private PrintStream old;

	    public StreamCapturer(String prefix, Consumer consumer, PrintStream old) {
	        this.prefix = prefix;
	        buffer = new StringBuilder(128);
	        buffer.append("[").append(prefix).append("] ");
	        this.old = old;
	        this.consumer = consumer;
	    }

	    @Override
	    public void write(int b) throws IOException {
	        char c = (char) b;
	        String value = Character.toString(c);
	        buffer.append(value);
	        if (value.equals("\n")) {
	            consumer.appendText(buffer.toString());
	            buffer.delete(0, buffer.length());
	            buffer.append("[").append(prefix).append("] ");
	        }
	        old.print(c);
	    }
	    
	    @Override
	    public void write(byte[] buf, int off, int len) {
	    	String value = new String(Arrays.copyOfRange(buf, off, off + len));
	    	buffer.append(value);
	    	if (value.equals("\n")) {
	            consumer.appendText(buffer.toString());
	            buffer.delete(0, buffer.length());
	            buffer.append("[").append(prefix).append("] ");
	        }
	    	old.print(value);
	    }
	    
	    @Override
	    public void write(byte[] buf) {
	    	String value = new String(buf);
	    	buffer.append(value);
	    	if (value.equals("\n")) {
	            consumer.appendText(buffer.toString());
	            buffer.delete(0, buffer.length());
	            buffer.append("[").append(prefix).append("] ");
	        }
	    	old.print(value);
	    }
	}
	
}
