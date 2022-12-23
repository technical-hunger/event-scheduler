package com.technicalhunger.eventscheduler.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.technicalhunger.eventscheduler.entity.Event;
import com.technicalhunger.eventscheduler.service.EventService;

@RestController
@RequestMapping("/events")
public class EventController {

	@Autowired
	private EventService eventService;
	
	private List<SseEmitter> emitters = new ArrayList<>();
	
	@PostMapping("/add")
	public ResponseEntity<?> createEvent(@RequestBody Event event){
		Event created = eventService.scheduleEvent(event);
		
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<?> getEvents(){
		return new ResponseEntity<>(eventService.getEvents(), HttpStatus.OK);
	}
	
	@GetMapping("/{eventId}")
	public ResponseEntity<?> getEvent(@PathVariable String eventId){
		Event event;
		try {
			event = eventService.getEvent(Integer.valueOf(eventId));
		}catch(IndexOutOfBoundsException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
		}
		return new ResponseEntity<>(event, HttpStatus.OK);
	}
	
	@RequestMapping("/subscribe/{eventId}")
	public SseEmitter subscribe(@PathVariable String eventId) {
		SseEmitter emitter = new SseEmitter(86400000L);
		eventService.subscribe(emitter, eventId);
		emitter.onCompletion(() -> {
			eventService.remove(Integer.valueOf(eventId));
		});
		return emitter;
	}
	

}
