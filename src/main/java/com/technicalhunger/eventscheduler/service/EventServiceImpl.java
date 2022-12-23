package com.technicalhunger.eventscheduler.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.technicalhunger.eventscheduler.entity.Event;
import com.technicalhunger.eventscheduler.job.JobUtils;

@Service
public class EventServiceImpl implements EventService {

	private List<Event> events = new ArrayList<>();

	public Map<String, List<SseEmitter>> emitters = new HashMap<>();
	

	@Autowired
	private JobUtils jobUtils;
	
	@Autowired
	private Scheduler scheduler;
	
	@PostConstruct
	public void init() {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			System.err.println(e.getMessage());
		}
	}
	
	@Override
	public Event scheduleEvent(Event event) {
		System.out.println(event);
		event.setId(events.size());
		events.add(event);
		
		JobDetail job = jobUtils.buildJobDetails(event.getId(), this);
		Trigger trigger = jobUtils.buildJobTrigger(job, event.getTime());
		
		try {
			scheduler.scheduleJob(job,trigger);
		} catch (SchedulerException e) {
			System.err.println(e.getMessage());
		}
		
		return event;
	}

	@Override
	public List<Event> getEvents() {
		return events.stream()
						.filter(event -> !event.isDeleted())
						.collect(Collectors.toList());
	}

	@PreDestroy
	public void clean() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void subscribe(SseEmitter emitter, String eventId) {
		if(emitters.containsKey(eventId)) {
			emitters.get(eventId).add(emitter);
		}else {
			List<SseEmitter> e = new ArrayList<>();
			e.add(emitter);
			emitters.put(eventId, e);
		}
	}

	public Map<String, List<SseEmitter>> getEmitters() {
		return emitters;
	}

	public void setEmitters(Map<String, List<SseEmitter>> emitters) {
		this.emitters = emitters;
	}

	@Override
	public void remove(int id) {
		events.get(id).setDeleted(true);
	}

	@Override
	public Event getEvent(Integer id) {
		return events.get(id);
	}
	

	

}
