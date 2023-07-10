package io.edurt.datacap.service.service.impl;

import com.google.common.collect.Lists;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.service.entity.ChatEntity;
import io.edurt.datacap.service.entity.MessageEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.enums.MessageType;
import io.edurt.datacap.service.model.AiModel;
import io.edurt.datacap.service.repository.MessageRepository;
import io.edurt.datacap.service.repository.UserRepository;
import io.edurt.datacap.service.service.MessageService;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.devlive.sdk.openai.OpenAiClient;
import org.devlive.sdk.openai.entity.CompletionChatEntity;
import org.devlive.sdk.openai.entity.CompletionMessageEntity;
import org.devlive.sdk.openai.entity.UsageEntity;
import org.devlive.sdk.openai.model.CompletionMessageModel;
import org.devlive.sdk.openai.response.CompleteChatResponse;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@SuppressFBWarnings(value = {"RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class MessageServiceImpl
        implements MessageService
{
    private final MessageRepository repository;
    private final UserRepository userRepository;
    private final Environment environment;

    public MessageServiceImpl(MessageRepository repository, UserRepository userRepository, Environment environment)
    {
        this.repository = repository;
        this.userRepository = userRepository;
        this.environment = environment;
    }

    @Override
    public CommonResponse<MessageEntity> saveOrUpdate(PagingAndSortingRepository repository, MessageEntity configure)
    {
        Optional<UserEntity> userOptional = this.userRepository.findById(configure.getUser().getId());
        if (!userOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.USER_NOT_FOUND);
        }
        UserEntity user = userOptional.get();
        MessageEntity questionMessage = MessageEntity.builder()
                .user(user)
                .chat(configure.getChat())
                .model(configure.getModel())
                .content(configure.getContent())
                .name(UUID.randomUUID().toString())
                .type(MessageType.question)
                .build();
        this.repository.save(questionMessage);
        String openApiHost = environment.getProperty("datacap.openai.backend");
        String openApiToken = environment.getProperty("datacap.openai.token");
        String openApiModel = environment.getProperty("datacap.openai.model");
        long openApiTimeout = Long.parseLong(environment.getProperty("datacap.openai.timeout"));
        if (StringUtils.isNotEmpty(configure.getModel())) {
            openApiModel = configure.getModel();
        }
        // If user-defined configuration, use user configuration information
        if (StringUtils.isNotEmpty(user.getThirdConfigure())) {
            AiModel aiModel = JsonUtils.toObject(user.getThirdConfigure(), AiModel.class);
            if (StringUtils.isNotEmpty(aiModel.getToken())) {
                openApiHost = aiModel.getHost();
                openApiToken = aiModel.getToken();
            }
            if (aiModel.getTimeout() > 0) {
                openApiTimeout = aiModel.getTimeout();
            }
        }
//        try {
//            AiSupportUtils.AiSupportEnum type = AiSupportUtils.AiSupportEnum.valueOf(configure.getTransType());
//            String replaceContent = AiSupportUtils.getValue(configure.getLocale(), type);
//            Properties properties = new Properties();
//            properties.put("sql", configure.getContent());
//            if (ObjectUtils.isNotEmpty(configure.getEngine())) {
//                properties.put("engine", configure.getEngine());
//            }
//            if (ObjectUtils.isNotEmpty(configure.getError())) {
//                properties.put("error", configure.getError());
//            }
//            StrSubstitutor sub = new StrSubstitutor(properties);
//            forwardContent = sub.replace(replaceContent);
//        }
//        catch (Exception exception) {
//            log.warn("Ai type not set, ignore .");
//        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(openApiTimeout, TimeUnit.SECONDS)
                .writeTimeout(openApiTimeout, TimeUnit.SECONDS)
                .readTimeout(openApiTimeout, TimeUnit.SECONDS)
                .build();
        try (OpenAiClient openAiClient = OpenAiClient.builder()
                .apiHost(openApiHost)
                .apiKey(openApiToken)
                .client(okHttpClient)
                .build()) {
            List<CompletionMessageEntity> messages = new ArrayList<>();
            // Extract database history for recording context if source is dialog mode
//            if (StringUtils.isNotEmpty(configure.getFrom()) && configure.getFrom().equalsIgnoreCase("chat")) {
//                if (!configure.isNewChat()) {
//                    this.userChatRepository.findTop5ByUserOrderByIdDesc(UserDetailsService.getUser())
//                            .stream()
//                            .sorted(Comparator.comparing(UserChatEntity::getId))
//                            .forEach(userChatHistory -> {
//                                CompletionMessageEntity question = CompletionMessageEntity.builder()
//                                        .role(CompletionMessageModel.USER.getName())
//                                        .content(userChatHistory.getQuestion())
//                                        .build();
//                                messages.add(question);
//                                CompletionMessageEntity answer = CompletionMessageEntity.builder()
//                                        .role(CompletionMessageModel.ASSISTANT.getName())
//                                        .content(userChatHistory.getAnswer())
//                                        .build();
//                                messages.add(answer);
//                            });
//                }
//                saved = true;
//            }
            CompletionMessageEntity message = CompletionMessageEntity.builder()
                    .role(CompletionMessageModel.USER.getName())
                    .content(questionMessage.getContent())
                    .build();
            messages.add(message);
            CompletionChatEntity chatCompletion = CompletionChatEntity.builder()
                    .messages(messages)
                    .model(openApiModel)
                    .build();
            CompleteChatResponse chatCompletionResponse = openAiClient.createChatCompletion(chatCompletion);
            List<String> answer = Lists.newArrayList();
            chatCompletionResponse.getChoices()
                    .forEach(e -> answer.add(e.getMessage().getContent()));
            UsageEntity usage = chatCompletionResponse.getUsage();
            UserEntity aiUser = this.userRepository.findByUsernameAndSystemTrue("Ai");
            MessageEntity answerEntity = MessageEntity.builder()
                    .user(aiUser)
                    .chat(configure.getChat())
                    .content(String.join("\n", answer))
                    .type(MessageType.answer)
                    .promptTokens(usage.getPromptTokens())
                    .completionTokens(usage.getCompletionTokens())
                    .totalTokens(usage.getTotalTokens())
                    .messages(Arrays.asList(questionMessage))
                    .name(UUID.randomUUID().toString())
                    .build();
            this.repository.save(answerEntity);
            return CommonResponse.success(answerEntity);
        }
        catch (Exception e) {
            return CommonResponse.failure(e.getMessage());
        }
    }

    @Override
    public CommonResponse<List<MessageEntity>> getMessageByChatAndUser(Long chatId)
    {
        ChatEntity chat = ChatEntity.builder()
                .id(chatId)
                .build();
        return CommonResponse.success(this.repository.findAllByChatOrderByCreateTimeAsc(chat));
    }
}
