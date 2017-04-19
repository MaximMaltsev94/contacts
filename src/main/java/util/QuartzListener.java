package util;

import org.quartz.*;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;


@WebListener
public class QuartzListener extends QuartzInitializerListener {
    private final static Logger LOG = LoggerFactory.getLogger(QuartzListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);
        ServletContext ctx = sce.getServletContext();
        StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(QUARTZ_FACTORY_KEY);
        try {
            JobDetail birthDateDetailJob = JobBuilder.newJob(BirthDateNotifyJob.class).build();
            Trigger birthDateTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("birthDate", "group1")
                    .withSchedule(dailyAtHourAndMinute(9, 0))
//                    .withSchedule(simpleSchedule().withIntervalInSeconds(50).withRepeatCount(1))
                    .startNow()
                    .build();

            JobDetail invalidateTokensDetailJob = JobBuilder.newJob(InvalidateTokensJob.class).build();
            Trigger invalidateTokensTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("invalidateTokens", "group1")
                    .withSchedule(simpleSchedule().withIntervalInMinutes(1).repeatForever())
                    .startNow()
                    .build();

            Scheduler scheduler = factory.getScheduler();
            scheduler.start();
            scheduler.scheduleJob(birthDateDetailJob, birthDateTrigger);
            scheduler.scheduleJob(invalidateTokensDetailJob, invalidateTokensTrigger);

        } catch (Exception e) {
            LOG.warn("can't start scheduler", e);
        }
    }


}
