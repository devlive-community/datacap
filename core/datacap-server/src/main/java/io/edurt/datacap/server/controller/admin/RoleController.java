package io.edurt.datacap.server.controller.admin;

import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.RoleEntity;
import io.edurt.datacap.server.repository.RoleRepository;
import io.edurt.datacap.server.service.RoleService;
import io.edurt.datacap.server.validation.ValidationGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/admin/role")
public class RoleController
{
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    public RoleController(RoleRepository roleRepository, RoleService roleService)
    {
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    @PostMapping(value = "list")
    public Response<PageEntity<RoleEntity>> getAllByFilter(@RequestBody FilterBody filter)
    {
        return this.roleService.getAll(roleRepository, filter);
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public Response<RoleEntity> save(@RequestBody @Validated(ValidationGroup.Crud.Create.class) RoleEntity configure)
    {
        return this.roleService.saveOrUpdate(roleRepository, configure);
    }

    @GetMapping(value = "{id}")
    public Response<RoleEntity> getInfo(@PathVariable(value = "id") Long id)
    {
        return this.roleService.getById(roleRepository, id);
    }
}
