package com.ms.fintech.handler;
import java.util.HashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class WebChatHandler extends TextWebSocketHandler {

	private HashMap<Integer, HashMap<String, WebSocketSession>> map = new HashMap<>();
	private int roomNo;
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String path = session.getUri().getPath();
		System.out.println(path);
		roomNo = Integer.parseInt(path.charAt(path.length()-1) + "");
		if (map.get(roomNo) == null) {
			var room = new HashMap<String, WebSocketSession>();
			room.put(session.getId(), session);
			map.put(roomNo, room);
		} else {
			map.get(roomNo).put(session.getId(), session);
		}
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//		 String msg = message.getPayload();
//		 JSONObject json = (JSONObject)new JSONParser().parse(msg);
		 HashMap<String, WebSocketSession> room = map.get(roomNo);
		 for (var v : room.values()) {
			 if (v.isOpen()) {
				 v.sendMessage(message);
			 }
		 }
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		map.get(roomNo).remove(session.getId());
	}
	
	
	
	
}
