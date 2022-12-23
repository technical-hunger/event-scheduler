package com.technicalhunger.eventscheduler.job;

import java.io.IOException;
import java.util.List;
import java.util.Observable;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.technicalhunger.eventscheduler.service.EventService;

@Component
public class EventJob implements Job {

	@Autowired
	private EventService eventService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("Event Started!");
		String eventId = context.getJobDetail().getJobDataMap().get("eventId").toString();
		System.err.println("Event Started!"+eventId);
		List<SseEmitter> emitters= eventService.getEmitters().get(eventId );
		
		if(emitters != null && !emitters.isEmpty()) {
			for(SseEmitter emitter : emitters) {
				try {
					emitter.send(SseEmitter.event().name("started").data(eventId));
					emitter.complete();
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
			
			eventService.getEmitters().remove(eventId);
		}else {
			System.out.println("Service not injected");
		}
	}

}
