package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.edurt.datacap.service.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.List;

@Data
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "datacap_message")
public class MessageEntity
        extends BaseEntity
{
    @Column(name = "content")
    private String content;

    @Column(name = "model")
    private String model;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column(name = "prompt_tokens")
    private long promptTokens;

    @Column(name = "completion_tokens")
    private long completionTokens;

    @Column(name = "total_tokens")
    private long totalTokens;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "datacap_message_user_relation",
            joinColumns = @JoinColumn(name = "message_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = {"roles", "thirdConfigure"})
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "datacap_message_chat_relation",
            joinColumns = @JoinColumn(name = "message_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = {"user"})
    private ChatEntity chat;

    /**
     * Contextual information
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "datacap_message_link_relation",
            joinColumns = @JoinColumn(name = "message_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "message_link_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = {"user"})
    private List<MessageEntity> messages;
}
