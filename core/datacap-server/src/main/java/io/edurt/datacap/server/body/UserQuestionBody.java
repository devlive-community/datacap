package io.edurt.datacap.server.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserQuestionBody
{
    private String locale;
    private String content;
    private String type;
    private String engine;
    private String error;
    private String transType;
}
