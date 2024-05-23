package io.edurt.datacap.service.entity.convert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AvatarEntity
{
    private String type;
    private String path;
    private String local;
}
