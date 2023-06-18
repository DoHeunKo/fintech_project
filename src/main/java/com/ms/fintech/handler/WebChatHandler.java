package com.ms.fintech.handler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class WebChatHandler extends TextWebSocketHandler {

//	private final List<HashMap<String, WebSocketSession>> roomList = new ArrayList<>();
	
	private final HashMap<String, WebSocketSession> sessions = new HashMap<>();
	private HashMap<String, WebSocketSession> rooms[] = new HashMap[5];
	private int roomNo;
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String path = session.getUri().getPath();
		roomNo = Integer.parseInt(path.charAt(path.length()-1) + "") - 1;
		if (rooms[roomNo] == null) {
			rooms[roomNo] = new HashMap<String, WebSocketSession>();
			rooms[roomNo].put(session.getId(), session);
		} else {
			rooms[roomNo].put(session.getId(), session);
		}
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		 String msg = message.getPayload();
		 JSONObject json = (JSONObject)new JSONParser().parse(msg);
		 HashMap<String, WebSocketSession> room = rooms[roomNo];
		 for (var v : room.values()) {
			 if (v.isOpen()) {
				 v.sendMessage(message);
			 }
		 }
	}
	

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		rooms[roomNo].remove(session.getId());
	}
	
	
	
	
}
