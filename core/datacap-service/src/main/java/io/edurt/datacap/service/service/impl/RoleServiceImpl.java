package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.enums.MenuEnum;
import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.MenuEntity;
import io.edurt.datacap.service.entity.RoleEntity;
import io.edurt.datacap.service.record.TreeRecord;
import io.edurt.datacap.service.repository.RoleRepository;
import io.edurt.datacap.service.repository.admin.MenuRepository;
import io.edurt.datacap.service.service.RoleService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoleServiceImpl
        implements RoleService
{
    private final RoleRepository repository;
    private final MenuRepository menuRepository;

    public RoleServiceImpl(RoleRepository repository, MenuRepository menuRepository)
    {
        this.repository = repository;
        this.menuRepository = menuRepository;
    }

    @Override
    public CommonResponse<Object> getMenusByRoleId(Long roleId)
    {
        Optional<RoleEntity> optionalRole = repository.findById(roleId);
        if (!optionalRole.isPresent()) {
            return CommonResponse.failure(ServiceState.USER_ROLE_NOT_FOUND);
        }
        Map<Long, TreeRecord> treeMap = new ConcurrentHashMap<>();
        // All currently available menus
        List<MenuEntity> activedMenus = menuRepository.findAllByType(MenuEnum.VIEW);
        // The current permission has a menu
        RoleEntity role = optionalRole.get();
        Map<Long, MenuEntity> roleMenus = new ConcurrentHashMap<>();
        // Populate own menu
        role.getMenus().forEach(menu -> roleMenus.put(menu.getId(), menu));
        // Into the list
        List<MenuEntity> menuList = StreamSupport.stream(activedMenus.spliterator(), false)
                .sorted(Comparator.comparing(MenuEntity::getParent))
                .collect(Collectors.toList());
        // Sets the parent menu sort
        menuList.forEach(menu -> {
            TreeRecord parent = new TreeRecord();
            if (menu.getParent() == 0) {
                parent.setId(menu.getId());
                parent.setTitle(menu.getName());
                parent.setUrl(menu.getUrl());
                if (!ObjectUtils.isEmpty(roleMenus.get(menu.getId()))) {
                    parent.setChecked(Boolean.TRUE);
                    parent.setSelected(Boolean.TRUE);
                }
                treeMap.put(menu.getId(), parent);
            }
            else {
                // Sub menu
                TreeRecord temp = treeMap.get(menu.getParent());
                List<TreeRecord> childrens = temp.getChildren();
                // Automatically sets a collection of submenus if the current submenu does not belong to the main menu
                if (ObjectUtils.isEmpty(childrens)) {
                    childrens = new ArrayList<>();
                }
                TreeRecord children = new TreeRecord();
                children.setId(menu.getId());
                children.setTitle(menu.getName());
                children.setUrl(menu.getUrl());
                if (!ObjectUtils.isEmpty(roleMenus.get(menu.getId()))) {
                    children.setChecked(Boolean.TRUE);
                    children.setSelected(Boolean.TRUE);
                }
                childrens.add(children);
                temp.setChildren(childrens);
                treeMap.put(temp.getId(), temp);
            }
        });
        // Convert Map data to List
        List<TreeRecord> tree = new ArrayList<>();
        treeMap.keySet().forEach(v -> tree.add(treeMap.get(v)));
        return CommonResponse.success(tree);
    }
}
