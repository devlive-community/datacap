package io.edurt.datacap.service.entity.itransient.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserEditorEntity
{
    private Integer fontSize = 12;
    private String theme = "chrome";
}
