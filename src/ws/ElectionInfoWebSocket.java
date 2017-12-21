package ws;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.server.ServerEndpoint;

import rmi.Election;
import rmi.ServerInterface;

import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint(value = "/ws")
public class ElectionInfoWebSocket {
    private Session session;
    private boolean isUp;
    private ServerInterface rmiCon;
	private String naming =  "//127.0.0.1:7000/server";
    private String elId = "";

    public ElectionInfoWebSocket() {
        
    }

    @OnOpen
    public void start(Session session) {
        this.session = session;
        isUp = true;
        String info = " ";
        String new_info = "";
        while(isUp && this.session.isOpen()){ 
        	try {
				new_info = rmiCon.electionInfoRealTime(this.elId);
				if(!info.equals(new_info)){
	        		new_info = info;
	        		sendMessage(info);
	        	}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    @OnClose
    public void end() {
    	this.isUp=false;
    }

    @OnMessage
    public void receiveMessage(String message) {
		this.elId = message;
    }
    
    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }

    private void sendMessage(String text) {
    	// uses *this* object's session to call sendText()
    	try {
			this.session.getBasicRemote().sendText(text);
		} catch (IOException e) {
			// clean up once the WebSocket connection is closed
			try {
				this.session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    }
    
}