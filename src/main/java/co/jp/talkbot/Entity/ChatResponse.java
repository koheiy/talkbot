package co.jp.talkbot.Entity;

import lombok.Data;

import java.util.List;

@Data
public class ChatResponse {
    private String status;
    private String message;
    private List<Results> results;
}
