package com.thunguyen.graphhrservice.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class OfflineSchedledTask {

  private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
}
