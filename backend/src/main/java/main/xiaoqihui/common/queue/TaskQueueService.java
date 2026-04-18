package main.xiaoqihui.common.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TaskQueueService {

    private static final Logger log = LoggerFactory.getLogger(TaskQueueService.class);

    @Async("taskQueueExecutor")
    public void submit(String taskName, Runnable task) {
        try {
            task.run();
        } catch (Exception ex) {
            log.error("任务执行失败: {}", taskName, ex);
        }
    }
}
