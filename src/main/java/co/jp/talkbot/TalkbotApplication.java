package co.jp.talkbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"me.ramswaroop.jbot", "co.jp.*"})
public class TalkbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalkbotApplication.class, args);
    }
}
