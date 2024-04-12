package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "active")
    private boolean active = true;

    @Column(name = "create_time")
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "update_time")
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public void setCode(String code)
    {
        if (this.code == null) {
            this.code = code;
        }
    }

    @PrePersist
    public void generateCode()
    {
        if (this.code == null) {
            this.code = UUID.randomUUID()
                    .toString()
                    .replace("-", "");
        }
    }
}
