package util;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by maxim on 04.10.2016.
 */
public class BirthDateNotifier {
    private final static Logger LOG = LoggerFactory.getLogger(BirthDateNotifier.class);
    public void start() {
        try {
            JobDetail job = JobBuilder.newJob(BirthDateNotifyJob.class).withIdentity("birthDay notifier", "group1").build();
            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
//                    .withSchedule(calendarIntervalSchedule().withIntervalInSeconds(30)).forJob(job)
                    .withSchedule(dailyAtHourAndMinute(9, 0)).forJob(job)
                    .build();
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            LOG.warn("can't start scheduler", e);
        }
    }
}
