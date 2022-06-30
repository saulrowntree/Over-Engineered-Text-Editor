package srowntree;

import org.eclipse.microprofile.config.inject.ConfigProperty;
//import srowntree.tasks.ReverseProxyMonitorTask;
import srowntree.tasks.ReverseProxyMonitorTask;
import srowntree.tasks.ServiceMonitorTask;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class MonitorExecutor {

    @Inject
    @ConfigProperty(name="monitor.service.task.poll.period")
    private int mServicePollPeriod;
    @Inject
    @ConfigProperty(name="monitor.service.task.initial.delay")
    private int mServiceInitialDelay;
    @Inject
    @ConfigProperty(name="monitor.proxy.task.poll.period")
    private int mProxyPollPeriod;
    @Inject
    @ConfigProperty(name="monitor.proxy.task.initial.delay")
    private int mProxyInitialDelay;

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);


    private void onStartup(@Observes @Initialized(ApplicationScoped.class) final Object event) {
        ContextRunnableWrapper serviceMonitorTask = new ContextRunnableWrapper(new ServiceMonitorTask());
        executorService.scheduleAtFixedRate(serviceMonitorTask, mServiceInitialDelay, mServicePollPeriod, TimeUnit.MINUTES);

        ContextRunnableWrapper reverseProxyMonitorTask = new ContextRunnableWrapper(new ReverseProxyMonitorTask());
        executorService.scheduleAtFixedRate(reverseProxyMonitorTask, mProxyInitialDelay, mProxyPollPeriod, TimeUnit.MINUTES);
    }

}
