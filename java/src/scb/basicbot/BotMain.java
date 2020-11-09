package scb.basicbot;

public class BotMain {
    public static void main() {
    	try{
            new MyBotModule().run();   		
    	} catch(Exception e) {
    		throw new RuntimeException(e);
    	}
    }
}