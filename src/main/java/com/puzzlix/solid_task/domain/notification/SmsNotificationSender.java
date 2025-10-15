package com.puzzlix.solid_task.domain.notification;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.exception.SolapiEmptyResponseException;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.exception.SolapiUnknownException;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.model.MessageType;
import com.solapi.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Component;

@Component
public class SmsNotificationSender implements NotificationSender {

    DefaultMessageService messageService
            = SolapiClient.INSTANCE.createInstance("NCS2GQTHPWU31YLY","55WEKWT2LQFUW6AHZMHIE2UPLKWQP2AQ");

    @Override
    public void send(String message) {
        Message smsMessage = new Message();
        smsMessage.setFrom("01045312438");  // 발신 번호
        smsMessage.setTo("01045312438");    // 수신 번호
        smsMessage.setType(MessageType.SMS);
        smsMessage.setText("SMS - 문자 발송 테스트");  // 보낼 메세지
        System.out.println("SMS 알림 발송 " + message);
        try {
            messageService.send(smsMessage);
        } catch (SolapiMessageNotReceivedException e) {
            throw new RuntimeException(e);
        } catch (SolapiEmptyResponseException e) {
            throw new RuntimeException(e);
        } catch (SolapiUnknownException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean supports(String type) {
        return "SMS".equalsIgnoreCase(type);
    }
}
