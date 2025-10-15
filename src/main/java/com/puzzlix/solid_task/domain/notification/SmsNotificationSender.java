package com.puzzlix.solid_task.domain.notification;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Component;

@Component
public class SmsNotificationSender implements NotificationSender {

    DefaultMessageService messageService
            = SolapiClient.INSTANCE.createInstance(
                    "NCS2GQTHPWU31YLY", "55WEKWT2LQFUW6AHZMHIE2UPLKWQP2AQ");

    @Override
    public void send(String message) {

        Message smsMessage = new Message();
        smsMessage.setFrom("01045312438");  // 발신 번호
        smsMessage.setTo("01045312438");    // 수신 번호
        smsMessage.setText("SMS - 문자 발송 테스트");

        try {
            // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
            messageService.send(smsMessage);
            System.out.println("보내짐");
        } catch (SolapiMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
            System.out.println("안보내짐");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.out.println("안보내짐");
        }
    }

    @Override
    public boolean supports(String type) {
        return "SMS".equalsIgnoreCase(type);
    }
}
