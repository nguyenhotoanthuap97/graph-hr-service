package com.thunguyen.graphhrservice.schedule;

import com.thunguyen.graphhrservice.services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class OfflineSchedledTask {

  @Autowired
  private RecommendationService recommendationService;

  @Autowired
  private ThreadPoolTaskScheduler scheduler;

  private Runnable triggerOfflineCalculation() {
    return () -> recommendationService.startImprovedOfflineCalculating();
  }

  @EventListener(ApplicationReadyEvent.class)
  public void recalculateSimilarities() {
    CronTrigger cronTrigger
        = new CronTrigger("0 0 0 * * ?");
    scheduler.initialize();
    scheduler.schedule(triggerOfflineCalculation(), cronTrigger);
  }
}
