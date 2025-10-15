package com.puzzlix.solid_task.domain.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Component
public class SmsNotificationSender implements NotificationSender {

    @Value("${aligo.api.key}")
    private String apiKey;

    @Value("${aligo.api.user-id}")
    private String userId;

    @Value("${aligo.api.sender}")
    private String senderNumber;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String ALIGO_SMS_URL = "https://apis.aligo.in/send/";

    @Override
    public void send(String message) {
        try {
            // 1. 수신자 번호
            String receiver = "01012345678";

            // 2. 폼 데이터 구성
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("key", apiKey);
            params.add("user_id", userId);
            params.add("sender", senderNumber);
            params.add("receiver", receiver);
            params.add("msg", "커피 내놔");
            params.add("msg_type", "SMS");
            params.add("testmode_yn", "N");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            // 3. 알리고 API 호출
            ResponseEntity<String> response = restTemplate.postForEntity(ALIGO_SMS_URL, request, String.class);

            if(response.getStatusCode().is2xxSuccessful()) {
                System.out.println("SMS API - 성공" + response.getBody());
            } else {
                System.out.println("SMS API - 실패" + response.getBody());
            }

        } catch (Exception e) {
            System.err.println("[알리고 문자 발송 예외 발생]");
            e.printStackTrace();
        }
    }

    @Override
    public boolean supports(String type) {
        return "SMS".equalsIgnoreCase(type);
    }
}

/*
    @Override
    public void send(String message) {
        System.out.println("[SMS 발송]" + message);
    }

    @Override
    public boolean supports(String type) {
        return "SMS".equalsIgnoreCase(type);
    }
*/