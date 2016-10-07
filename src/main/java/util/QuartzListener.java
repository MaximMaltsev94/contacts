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


@WebListener
public class QuartzListener extends QuartzInitializerListener {
    private final static Logger LOG = LoggerFactory.getLogger(QuartzListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);
        ServletContext ctx = sce.getServletContext();
        StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(QUARTZ_FACTORY_KEY);
        try {
            Scheduler scheduler = factory.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(BirthDateNotifyJob.class).build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("simple")
                    .withSchedule(dailyAtHourAndMinute(9, 0))
                    .startNow()
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (Exception e) {
            LOG.warn("can't start scheduler", e);
        }
    }


}
