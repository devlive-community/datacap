package io.edurt.datacap.server.controller.user;

import com.google.common.collect.Sets;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.UserNameBody;
import io.edurt.datacap.service.body.UserPasswordBody;
import io.edurt.datacap.service.body.UserQuestionBody;
import io.edurt.datacap.service.body.user.UserRole;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.RoleEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.entity.UserLogEntity;
import io.edurt.datacap.service.record.TreeRecord;
import io.edurt.datacap.service.repository.RoleRepository;
import io.edurt.datacap.service.service.UserLogService;
import io.edurt.datacap.service.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController
{
    private final UserService userService;
    private final UserLogService userLogService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, UserLogService userLogService, RoleRepository roleRepository)
    {
        this.userService = userService;
        this.userLogService = userLogService;
        this.roleRepository = roleRepository;
    }

    @GetMapping(value = {"{id}", ""})
    public CommonResponse<UserEntity> info(@PathVariable(required = false) Long id)
    {
        return this.userService.info(id);
    }

    @PutMapping(value = "changePassword")
    public CommonResponse<Long> changePassword(@Validated @RequestBody UserPasswordBody configure)
    {
        return this.userService.changePassword(configure);
    }

    @PutMapping(value = "changeUsername")
    public CommonResponse<Long> changeUsername(@Validated @RequestBody UserNameBody configure)
    {
        return this.userService.changeUsername(configure);
    }

    @PutMapping(value = "changeThirdConfigure")
    public CommonResponse<Long> changeThirdConfigure(@Validated @RequestBody Map<String, Map<String, Object>> configure)
    {
        return this.userService.changeThirdConfigure(configure);
    }

    @PostMapping(value = "log")
    public CommonResponse<PageEntity<UserLogEntity>> getAllLogByFilter(@RequestBody FilterBody filter)
    {
        return this.userLogService.getAllByFilter(filter);
    }

    @PostMapping(value = "startChat")
    public CommonResponse<Object> startChat(@RequestBody UserQuestionBody configure)
    {
        return this.userService.startChat(configure);
    }

    @GetMapping(value = "sugs/{id}")
    public CommonResponse<List<Object>> getSugs(@PathVariable Long id)
    {
        return this.userService.getSugs(id);
    }

    @GetMapping(value = "menus")
    public CommonResponse<List<TreeRecord>> getMenus()
    {
        return this.userService.getMenus();
    }

    @PostMapping(value = "list")
    public CommonResponse<PageEntity<UserEntity>> getAllByFilter(@RequestBody FilterBody filter)
    {
        return this.userService.getAll(filter);
    }

    @PutMapping(value = "allocationRole")
    public CommonResponse<UserEntity> allocationRole(@RequestBody UserRole configure)
    {
        UserEntity user = new UserEntity();
        user.setId(configure.getUserId());
        Set<RoleEntity> roles = Sets.newHashSet();
        configure.getRoles()
                .stream()
                .forEach(id -> {
                    Optional<RoleEntity> optionalRole = roleRepository.findById(id);
                    if (optionalRole.isPresent()) {
                        roles.add(optionalRole.get());
                    }
                });
        user.setRoles(roles);
        return this.userService.saveOrUpdate(user);
    }
}
