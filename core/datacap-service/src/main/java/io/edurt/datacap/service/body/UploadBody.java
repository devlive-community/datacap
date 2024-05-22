package io.edurt.datacap.service.body;

import io.edurt.datacap.service.enums.UploadMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UploadBody
{
    private Long id;
    private String code;
    private UploadMode mode;
    private MultipartFile file;
}
