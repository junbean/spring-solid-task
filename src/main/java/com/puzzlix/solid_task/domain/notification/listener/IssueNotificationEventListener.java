package com.puzzlix.solid_task.domain.notification.listener;

import com.puzzlix.solid_task.domain.issue.Issue;
import com.puzzlix.solid_task.domain.issue.event.IssueStatusChangedEvent;
import com.puzzlix.solid_task.domain.notification.NotificationSender;
import com.puzzlix.solid_task.domain.notification.NotificationSenderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component  // IOC 처리
@RequiredArgsConstructor
public class IssueNotificationEventListener {

    private final NotificationSenderFactory notificationSenderFactory;

    // 알림 정책 -  yml 파일 -> Email
//     NotificationSenderFactory getNotificationSenderFactory() {
//        return notificationSenderFactory;
//    }

    @Value("${notification.policy.on-status-done}")
    private String onStatusDoneType;

    @EventListener
    private void handleIssueStatusChangeEvent(IssueStatusChangedEvent event) {
        Issue issue = event.getIssue();
        String message = "이슈 #" +
                issue.getTitle() + "의 상태가 " +
                issue.getIssueStatus() + "로 변경되었습니다.";

        if("DONE".equalsIgnoreCase(issue.getIssueStatus().name())) {
            // 팩토리 클래스를 활용해서 현재 알림 전략을 가지고 온다.
            // yml --> EMAIL, SMS
            NotificationSender sender
                    = notificationSenderFactory.findSender(onStatusDoneType);
            
            // 이메일 구현 클래스던 , sms 구현 클래스 던 여기서는 send만 호출 하면 됨.
            sender.send(message);
        }
    }

}
