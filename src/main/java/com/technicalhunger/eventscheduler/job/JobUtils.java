package com.technicalhunger.eventscheduler.job;

import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

import com.technicalhunger.eventscheduler.service.EventService;

@Component
public class JobUtils {

	public JobDetail buildJobDetails(long eventId, EventService eventService) {

		return JobBuilder.newJob(EventJob.class)
							.withIdentity("Event-" + eventId)
							.usingJobData("eventId", eventId)
							.build();

	}

	public Trigger buildJobTrigger(JobDetail job, Date time) {
		System.out.println(time.toString());
		return TriggerBuilder.newTrigger().withIdentity(job.getJobDataMap().get("eventId").toString())
				 //.startNow()
				.startAt(time)
				// .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 * * ?"))
				.forJob(job).build();
	}
}
