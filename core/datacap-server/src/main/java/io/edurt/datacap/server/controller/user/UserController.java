package io.edurt.datacap.server.controller.user;

import com.google.common.collect.Sets;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.server.controller.BaseController;
import io.edurt.datacap.service.annotation.DynamicJsonView;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.UploadBody;
import io.edurt.datacap.service.body.UserNameBody;
import io.edurt.datacap.service.body.UserPasswordBody;
import io.edurt.datacap.service.body.user.UserRole;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.RoleEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.entity.UserLogEntity;
import io.edurt.datacap.service.entity.convert.AvatarEntity;
import io.edurt.datacap.service.entity.itransient.user.UserEditorEntity;
import io.edurt.datacap.service.model.AiModel;
import io.edurt.datacap.service.record.TreeRecord;
import io.edurt.datacap.service.repository.RoleRepository;
import io.edurt.datacap.service.repository.UserRepository;
import io.edurt.datacap.service.service.UserLogService;
import io.edurt.datacap.service.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController
        extends BaseController<UserEntity>
{
    private final UserRepository repository;
    private final UserService service;
    private final UserLogService userLogService;
    private final RoleRepository roleRepository;

    public UserController(UserRepository repository, UserService service, UserLogService userLogService, RoleRepository roleRepository)
    {
        super(repository, service);
        this.repository = repository;
        this.service = service;
        this.userLogService = userLogService;
        this.roleRepository = roleRepository;
    }

    @GetMapping(value = {"{code}", ""})
    @DynamicJsonView
    public CommonResponse<UserEntity> info(@PathVariable(required = false) String code)
    {
        return this.service.info(code);
    }

    @PutMapping(value = "changePassword")
    public CommonResponse<Long> changePassword(@Validated @RequestBody UserPasswordBody configure)
    {
        return this.service.changePassword(configure);
    }

    @GetMapping(value = "checkUsername")
    public CommonResponse<Boolean> checkUsername(@RequestParam(value = "username") String username)
    {
        return service.checkUsername(username);
    }

    @PutMapping(value = "changeUsername")
    public CommonResponse<Long> changeUsername(@Validated @RequestBody UserNameBody configure)
    {
        return this.service.changeUsername(configure);
    }

    @PutMapping(value = "changeThirdConfigure")
    public CommonResponse<Long> changeThirdConfigure(@Validated @RequestBody AiModel configure)
    {
        return this.service.changeThirdConfigure(configure);
    }

    @PostMapping(value = "log")
    public CommonResponse<PageEntity<UserLogEntity>> getAllLogByFilter(@RequestBody FilterBody filter)
    {
        return this.userLogService.getAllByFilter(filter);
    }

    @GetMapping(value = "menus")
    @DynamicJsonView
    public CommonResponse<List<TreeRecord>> getMenus()
    {
        return this.service.getMenus();
    }

    @PutMapping(value = "allocationRole")
    public CommonResponse<UserEntity> allocationRole(@RequestBody UserRole configure)
    {
        UserEntity user = new UserEntity();
        user.setId(configure.getUserId());
        Set<RoleEntity> roles = Sets.newHashSet();
        configure.getRoles()
                .forEach(code -> roleRepository.findByCode(code)
                        .ifPresent(roles::add));
        user.setRoles(roles);
        return this.service.saveOrUpdate(user);
    }

    @PutMapping(value = "changeEditorConfigure")
    public CommonResponse<UserEntity> changeEditorConfigure(@Validated @RequestBody UserEditorEntity configure)
    {
        return this.service.changeEditorConfigure(configure);
    }

    @SneakyThrows
    @PostMapping("uploadAvatar")
    public CommonResponse<AvatarEntity> uploadAvatar(@ModelAttribute UploadBody configure)
    {
        return this.service.uploadAvatar(configure);
    }

    @PutMapping(value = "change-notify")
    public CommonResponse<UserEntity> changeNotify(@RequestBody UserEntity configure)
    {
        return this.service.changeNotify(configure);
    }
}
