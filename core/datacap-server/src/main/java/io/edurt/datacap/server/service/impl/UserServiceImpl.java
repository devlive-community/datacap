package io.edurt.datacap.server.service.impl;

import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import io.edurt.datacap.server.audit.AuditUserLog;
import io.edurt.datacap.server.body.UserNameBody;
import io.edurt.datacap.server.body.UserPasswordBody;
import io.edurt.datacap.server.body.UserQuestionBody;
import io.edurt.datacap.server.common.AiSupportCommon;
import io.edurt.datacap.server.common.JSON;
import io.edurt.datacap.server.common.JwtResponse;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.common.ServiceState;
import io.edurt.datacap.server.entity.RoleEntity;
import io.edurt.datacap.server.entity.SourceEntity;
import io.edurt.datacap.server.entity.UserChatEntity;
import io.edurt.datacap.server.entity.UserEntity;
import io.edurt.datacap.server.repository.RoleRepository;
import io.edurt.datacap.server.repository.SourceRepository;
import io.edurt.datacap.server.repository.UserChatRepository;
import io.edurt.datacap.server.repository.UserRepository;
import io.edurt.datacap.server.security.JwtService;
import io.edurt.datacap.server.security.UserDetailsService;
import io.edurt.datacap.server.service.UserService;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl
        implements UserService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserChatRepository userChatRepository;
    private final SourceRepository sourceRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisTemplate redisTemplate;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserChatRepository userChatRepository, SourceRepository sourceRepository, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtService jwtService, RedisTemplate redisTemplate)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userChatRepository = userChatRepository;
        this.sourceRepository = sourceRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Response<UserEntity> saveOrUpdate(UserEntity configure)
    {
        Optional<UserEntity> userOptional = this.userRepository.findByUsername(configure.getUsername());
        if (userOptional.isPresent()) {
            return Response.failure(ServiceState.USER_EXISTS);
        }

        UserEntity user = new UserEntity();
        user.setUsername(configure.getUsername());
        user.setPassword(encoder.encode(configure.getPassword()));

        Set<RoleEntity> userRoles = configure.getRoles();
        Set<RoleEntity> roles = new HashSet<>();
        if (ObjectUtils.isEmpty(userRoles)) {
            Optional<RoleEntity> userRoleOptional = roleRepository.findByName("User");
            if (!userRoleOptional.isPresent()) {
                return Response.failure(ServiceState.USER_ROLE_NOT_FOUND);
            }
            roles.add(userRoleOptional.get());
        }
        user.setRoles(roles);
        return Response.success(userRepository.save(user));
    }

    @AuditUserLog
    @Override
    public Response<JwtResponse> authenticate(UserEntity configure)
    {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(configure.getUsername(), configure.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateJwtToken(authentication);

        UserDetailsService userDetails = (UserDetailsService) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return Response.success(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
    }

    @Override
    public Response<UserEntity> info(Long userId)
    {
        if (ObjectUtils.isEmpty(userId)) {
            userId = UserDetailsService.getUser().getId();
        }
        return Response.success(this.userRepository.findById(userId).get());
    }

    @Override
    public Response<Long> changePassword(UserPasswordBody configure)
    {
        Optional<UserEntity> userOptional = this.userRepository.findById(UserDetailsService.getUser().getId());
        if (!userOptional.isPresent()) {
            return Response.failure(ServiceState.USER_NOT_FOUND);
        }
        UserEntity user = userOptional.get();
        if (!encoder.matches(configure.getOldPassword(), user.getPassword())) {
            return Response.failure(ServiceState.USER_PASSWORD_INCORRECT);
        }
        if (!configure.getNewPassword().equals(configure.getConfirmPassword())) {
            return Response.failure(ServiceState.USER_PASSWORD_DIFFERENT);
        }
        user.setPassword(encoder.encode(configure.getNewPassword()));
        this.userRepository.save(user);
        return Response.success(user.getId());
    }

    @Override
    public Response<Long> changeUsername(UserNameBody configure)
    {
        Optional<UserEntity> userOptional = this.userRepository.findById(UserDetailsService.getUser().getId());
        if (!userOptional.isPresent()) {
            return Response.failure(ServiceState.USER_NOT_FOUND);
        }
        UserEntity user = userOptional.get();
        if (!encoder.matches(configure.getPassword(), user.getPassword())) {
            return Response.failure(ServiceState.USER_PASSWORD_INCORRECT);
        }
        if (configure.getNewUsername().equals(user.getUsername())) {
            return Response.failure(ServiceState.USER_NAME_EQUALS);
        }
        user.setUsername(configure.getNewUsername());
        this.userRepository.save(user);
        return Response.success(user.getId());
    }

    @Override
    public Response<Long> changeThirdConfigure(Map<String, Map<String, Object>> configure)
    {
        Optional<UserEntity> userOptional = this.userRepository.findById(UserDetailsService.getUser().getId());
        if (!userOptional.isPresent()) {
            return Response.failure(ServiceState.USER_NOT_FOUND);
        }
        UserEntity user = userOptional.get();
        user.setThirdConfigure(JSON.toJSON(configure));
        this.userRepository.save(user);
        return Response.success(user.getId());
    }

    @Override
    public Response<Object> startChat(UserQuestionBody configure)
    {
        Optional<UserEntity> userOptional = this.userRepository.findById(UserDetailsService.getUser().getId());
        if (!userOptional.isPresent()) {
            return Response.failure(ServiceState.USER_NOT_FOUND);
        }

        if (!configure.getType().equals("ChatGPT")) {
            return Response.failure("Not supported");
        }
        UserEntity user = userOptional.get();
        UserChatEntity userChat = UserChatEntity.builder()
                .question(configure.getContent())
                .user(user)
                .name(UUID.randomUUID().toString())
                .type(configure.getType())
                .createTime(Timestamp.valueOf(LocalDateTime.now()))
                .isNew(configure.isNewChat())
                .build();

        List<String> content = new ArrayList<>();
        if (StringUtils.isNotEmpty(user.getThirdConfigure())) {
            Map<String, Object> configureMap = JSON.toMap(user.getThirdConfigure());
            if (configureMap.containsKey("chatgpt")) {
                Map<String, Object> chatGPTMap = (Map<String, Object>) configureMap.get("chatgpt");
                if (chatGPTMap.containsKey("token")) {
                    String token = (String) chatGPTMap.get("token");
                    if (StringUtils.isNotEmpty(token)) {
                        OpenAiClient openAiClient;
                        Object isProxy = chatGPTMap.get("proxy");
                        if (ObjectUtils.isNotEmpty(isProxy) && Boolean.valueOf(isProxy.toString())) {
                            String host = (String) chatGPTMap.get("host");
                            int port = (int) chatGPTMap.get("port");
                            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
                            openAiClient = OpenAiClient.builder()
                                    .apiKey(token)
                                    .proxy(proxy)
                                    .build();
                        }
                        else {
                            openAiClient = OpenAiClient.builder()
                                    .apiKey(token)
                                    .build();
                        }
                        String forwardContent = configure.getContent();
                        try {
                            AiSupportCommon.AiSupportEnum type = AiSupportCommon.AiSupportEnum.valueOf(configure.getTransType());
                            String replaceContent = AiSupportCommon.getValue(configure.getLocale(), type);
                            Properties properties = new Properties();
                            properties.put("sql", configure.getContent());
                            if (ObjectUtils.isNotEmpty(configure.getEngine())) {
                                properties.put("engine", configure.getEngine());
                            }
                            if (ObjectUtils.isNotEmpty(configure.getError())) {
                                properties.put("error", configure.getError());
                            }
                            StrSubstitutor sub = new StrSubstitutor(properties);
                            forwardContent = sub.replace(replaceContent);
                        }
                        catch (Exception exception) {
                            // Ignore it
                        }
                        List<Message> messages = new ArrayList<>();
                        // Extract database history for recording context if source is dialog mode
                        boolean saved = false;
                        if (StringUtils.isNotEmpty(configure.getFrom()) && configure.getFrom().equalsIgnoreCase("chat")) {
                            if (!configure.isNewChat()) {
                                this.userChatRepository.findTop5ByUserOrderByIdDesc(UserDetailsService.getUser())
                                        .stream()
                                        .sorted(Comparator.comparing(UserChatEntity::getId))
                                        .forEach(userChatHistory -> {
                                            Message question = Message.builder()
                                                    .role(Message.Role.USER)
                                                    .content(userChatHistory.getQuestion())
                                                    .build();
                                            messages.add(question);
                                            Message answer = Message.builder()
                                                    .role(Message.Role.ASSISTANT)
                                                    .content(userChatHistory.getAnswer())
                                                    .build();
                                            messages.add(answer);
                                        });
                            }
                            saved = true;
                        }
                        Message message = Message.builder()
                                .role(Message.Role.USER)
                                .content(forwardContent)
                                .build();
                        messages.add(message);
                        ChatCompletion chatCompletion = ChatCompletion.builder()
                                .messages(messages)
                                .build();
                        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
                        chatCompletionResponse.getChoices().forEach(e -> content.add(e.getMessage().getContent()));
                        userChat.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
                        userChat.setAnswer(String.join("\n", content));
                        if (saved) {
                            this.userChatRepository.save(userChat);
                        }
                    }
                }
            }
        }
        return Response.success(content);
    }

    @Override
    public Response<List<Object>> getSugs(Long id)
    {
        Optional<SourceEntity> sourceEntityOptional = sourceRepository.findById(id);
        if (!sourceEntityOptional.isPresent()) {
            return Response.failure(ServiceState.SOURCE_NOT_FOUND);
        }

        SourceEntity entity = sourceEntityOptional.get();
        String key = String.join("_", entity.getType(), entity.getId().toString());
        return Response.success(redisTemplate.opsForSet().members(key));
    }
}
