package io.edurt.datacap.service.service.impl;

import com.google.common.collect.Lists;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.common.Usage;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.response.JwtResponse;
import io.edurt.datacap.common.utils.AiSupportUtils;
import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.audit.AuditUserLog;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.UserNameBody;
import io.edurt.datacap.service.body.UserPasswordBody;
import io.edurt.datacap.service.body.UserQuestionBody;
import io.edurt.datacap.service.entity.MenuEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.RoleEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.entity.UserChatEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.model.AiModel;
import io.edurt.datacap.service.record.TreeRecord;
import io.edurt.datacap.service.repository.RoleRepository;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.repository.UserChatRepository;
import io.edurt.datacap.service.repository.UserRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.JwtService;
import io.edurt.datacap.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressFBWarnings(value = {"NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"})
@Service
@Slf4j
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
    private final Environment environment;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserChatRepository userChatRepository, SourceRepository sourceRepository, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtService jwtService, RedisTemplate redisTemplate, Environment environment)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userChatRepository = userChatRepository;
        this.sourceRepository = sourceRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.redisTemplate = redisTemplate;
        this.environment = environment;
    }

    @Override
    public CommonResponse<UserEntity> saveOrUpdate(UserEntity configure)
    {
        UserEntity user = new UserEntity();
        if (ObjectUtils.isEmpty(configure.getId())) {
            Optional<UserEntity> userOptional = this.userRepository.findByUsername(configure.getUsername());
            if (userOptional.isPresent()) {
                return CommonResponse.failure(ServiceState.USER_EXISTS);
            }
            user.setUsername(configure.getUsername());
            user.setPassword(encoder.encode(configure.getPassword()));
            Set<RoleEntity> userRoles = configure.getRoles();
            Set<RoleEntity> roles = new HashSet<>();
            if (ObjectUtils.isEmpty(userRoles)) {
                Optional<RoleEntity> userRoleOptional = roleRepository.findByName("User");
                if (!userRoleOptional.isPresent()) {
                    return CommonResponse.failure(ServiceState.USER_ROLE_NOT_FOUND);
                }
                roles.add(userRoleOptional.get());
            }
            user.setRoles(roles);
        }
        else {
            user = userRepository.findById(configure.getId()).get();
            user.setRoles(configure.getRoles());
        }
        return CommonResponse.success(userRepository.save(user));
    }

    @AuditUserLog
    @Override
    public CommonResponse<JwtResponse> authenticate(UserEntity configure)
    {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(configure.getUsername(), configure.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateJwtToken(authentication);

        UserDetailsService userDetails = (UserDetailsService) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return CommonResponse.success(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
    }

    @Override
    public CommonResponse<UserEntity> info(Long userId)
    {
        if (ObjectUtils.isEmpty(userId)) {
            userId = UserDetailsService.getUser().getId();
        }
        return CommonResponse.success(this.userRepository.findById(userId).get());
    }

    @Override
    public CommonResponse<Long> changePassword(UserPasswordBody configure)
    {
        Optional<UserEntity> userOptional = this.userRepository.findById(UserDetailsService.getUser().getId());
        if (!userOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.USER_NOT_FOUND);
        }
        UserEntity user = userOptional.get();
        if (!encoder.matches(configure.getOldPassword(), user.getPassword())) {
            return CommonResponse.failure(ServiceState.USER_PASSWORD_INCORRECT);
        }
        if (!configure.getNewPassword().equals(configure.getConfirmPassword())) {
            return CommonResponse.failure(ServiceState.USER_PASSWORD_DIFFERENT);
        }
        user.setPassword(encoder.encode(configure.getNewPassword()));
        this.userRepository.save(user);
        return CommonResponse.success(user.getId());
    }

    @Override
    public CommonResponse<Long> changeUsername(UserNameBody configure)
    {
        Optional<UserEntity> userOptional = this.userRepository.findById(UserDetailsService.getUser().getId());
        if (!userOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.USER_NOT_FOUND);
        }
        UserEntity user = userOptional.get();
        if (!encoder.matches(configure.getPassword(), user.getPassword())) {
            return CommonResponse.failure(ServiceState.USER_PASSWORD_INCORRECT);
        }
        if (configure.getNewUsername().equals(user.getUsername())) {
            return CommonResponse.failure(ServiceState.USER_NAME_EQUALS);
        }
        user.setUsername(configure.getNewUsername());
        this.userRepository.save(user);
        return CommonResponse.success(user.getId());
    }

    @Override
    public CommonResponse<Long> changeThirdConfigure(AiModel configure)
    {
        Optional<UserEntity> userOptional = this.userRepository.findById(UserDetailsService.getUser().getId());
        if (!userOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.USER_NOT_FOUND);
        }
        UserEntity user = userOptional.get();
        user.setThirdConfigure(JsonUtils.toJSON(configure));
        this.userRepository.save(user);
        return CommonResponse.success(user.getId());
    }

    @Override
    public CommonResponse<Object> startChat(UserQuestionBody configure)
    {
        Optional<UserEntity> userOptional = this.userRepository.findById(UserDetailsService.getUser().getId());
        if (!userOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.USER_NOT_FOUND);
        }

        if (!configure.getType().equals("ChatGPT")) {
            return CommonResponse.failure("Not supported");
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
        String openApiHost = environment.getProperty("datacap.openai.backend");
        String openApiToken = environment.getProperty("datacap.openai.token");
        String openApiModel = environment.getProperty("datacap.openai.model");
        if (StringUtils.isNotEmpty(configure.getModel())) {
            openApiModel = configure.getModel();
        }
        // Build the default Open AI client
        OpenAiClient openAiClient = OpenAiClient.builder()
                .apiHost(openApiHost)
                .apiKey(Collections.singletonList(openApiToken))
                .build();
        // If user-defined configuration, use user configuration information
        if (StringUtils.isNotEmpty(user.getThirdConfigure())) {
            AiModel aiModel = JsonUtils.toObject(user.getThirdConfigure(), AiModel.class);
            if (StringUtils.isNotEmpty(aiModel.getToken())) {
                openAiClient = OpenAiClient.builder()
                        .apiHost(aiModel.getHost())
                        .apiKey(Collections.singletonList(aiModel.getToken()))
                        .build();
            }
        }
        List<String> content = new ArrayList<>();
        String forwardContent = configure.getContent();
        try {
            AiSupportUtils.AiSupportEnum type = AiSupportUtils.AiSupportEnum.valueOf(configure.getTransType());
            String replaceContent = AiSupportUtils.getValue(configure.getLocale(), type);
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
            log.warn("Ai analysis failed", exception);
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
                .model(openApiModel)
                .build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
        chatCompletionResponse.getChoices()
                .forEach(e -> content.add(e.getMessage().getContent()));
        userChat.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
        userChat.setAnswer(String.join("\n", content));
        userChat.setModel(chatCompletionResponse.getModel());
        Usage usage = chatCompletionResponse.getUsage();
        userChat.setPromptTokens(usage.getPromptTokens());
        userChat.setCompletionTokens(usage.getCompletionTokens());
        userChat.setTotalTokens(usage.getTotalTokens());
        if (saved) {
            this.userChatRepository.save(userChat);
        }
        return CommonResponse.success(userChat);
    }

    @Override
    public CommonResponse<List<Object>> getSugs(Long id)
    {
        Optional<SourceEntity> sourceEntityOptional = sourceRepository.findById(id);
        if (!sourceEntityOptional.isPresent()) {
            return CommonResponse.failure(ServiceState.SOURCE_NOT_FOUND);
        }

        SourceEntity entity = sourceEntityOptional.get();
        String key = String.join("_", entity.getType(), entity.getId().toString());
        int sugsMaxSize = Integer.parseInt(environment.getProperty("datacap.editor.sugs.maxSize"));
        List<Object> sugs = (List<Object>) redisTemplate.opsForSet()
                .members(key)
                .stream()
                .limit(sugsMaxSize)
                .collect(Collectors.toList());
        return CommonResponse.success(sugs);
    }

    @Override
    public CommonResponse<List<TreeRecord>> getMenus()
    {
        Map<Long, TreeRecord> treeMap = new ConcurrentHashMap<>();
        Optional<UserEntity> optionalUser = userRepository.findById(UserDetailsService.getUser().getId());
        UserEntity user = optionalUser.get();
        List<TreeRecord> tree = new ArrayList<>();
        user.getRoles().forEach(role -> {
            List<MenuEntity> menuList = StreamSupport.stream(role.getMenus().spliterator(), false)
                    .sorted(Comparator.comparing(MenuEntity::getParent))
                    .collect(Collectors.toList());
            // Sets the parent menu sort
            menuList.forEach(menu -> {
                if (menu.getParent() == 0) {
                    TreeRecord parent = TreeRecord.of(menu, true, true, Lists.newArrayList());
                    parent.setNew(menu.isNew());
                    treeMap.put(menu.getId(), parent);
                }
                else {
                    TreeRecord temp = treeMap.get(menu.getParent());
                    List<TreeRecord> childrens = temp.getChildren();
                    if (ObjectUtils.isEmpty(childrens)) {
                        childrens = Lists.newArrayList();
                    }
                    TreeRecord children = TreeRecord.of(menu, true, true, Lists.newArrayList());
                    children.setNew(menu.isNew());
                    childrens.add(children);
                    childrens.sort(Comparator.comparing(TreeRecord::getSorted));
                    temp.setChildren(childrens);
                    treeMap.put(temp.getId(), temp);
                }
            });
            treeMap.keySet().forEach(v -> tree.add(treeMap.get(v)));
        });
        tree.sort(Comparator.comparing(TreeRecord::getSorted));
        return CommonResponse.success(tree);
    }

    @Override
    public CommonResponse<PageEntity<UserEntity>> getAll(FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(this.userRepository.findAll(pageable)));
    }
}
