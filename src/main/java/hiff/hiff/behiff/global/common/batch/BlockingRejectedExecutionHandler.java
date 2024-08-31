package hiff.hiff.behiff.global.common.batch;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class BlockingRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            // 큐에 여유가 생길 때까지 대기
            executor.getQueue().put(r);
        } catch (InterruptedException e) {
            // 대기 중 인터럽트가 발생한 경우, 현재 스레드의 interrupt 상태를 복구
            Thread.currentThread().interrupt();
            throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString());
        }
    }
}
