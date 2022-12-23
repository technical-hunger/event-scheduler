package com.technicalhunger.eventscheduler.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.technicalhunger.eventscheduler.entity.Event;

@Service
public interface EventService {

	Event scheduleEvent(Event event);

	List<Event> getEvents();

	void subscribe(SseEmitter emitter, String eventId);
	
	Map<String, List<SseEmitter>> getEmitters();
	
	public void setEmitters(Map<String, List<SseEmitter>> emitters);

	void remove(int valueOf);

	Event getEvent(Integer valueOf);
	
}
