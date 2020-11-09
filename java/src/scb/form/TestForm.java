package scb.form;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TestForm {
	
	public static void main(String[] args) throws UnsupportedEncodingException
	{
	    CapturePane capturePane = new CapturePane();
	    System.setOut(new PrintStream(new StreamCapturer("STDOUT", capturePane, System.out)));

	    System.out.println("Output test");

	    JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLayout(new BorderLayout());
	    frame.add(capturePane);
	    frame.setSize(200, 200);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);

	    System.out.println("More output test");
	    
	    System.out.println("wtf");
	}

	@SuppressWarnings("serial")
	private static class CapturePane extends JPanel implements Consumer {

	    private JLabel output;

	    public CapturePane() {
	        setLayout(new BorderLayout());
	        output = new JLabel("<html>");
	        add(new JScrollPane(output));
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


	public static class StreamCapturer extends OutputStream {

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
	}
	
}