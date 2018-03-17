package in.clouthink.synergy.account.service.impl;

import in.clouthink.synergy.account.domain.model.*;
import in.clouthink.synergy.account.domain.request.SaveRoleRequest;
import in.clouthink.synergy.account.domain.request.UserQueryRequest;
import in.clouthink.synergy.account.exception.RoleException;
import in.clouthink.synergy.account.exception.RoleNotFoundException;
import in.clouthink.synergy.account.exception.UserException;
import in.clouthink.synergy.account.exception.UserNotFoundException;
import in.clouthink.synergy.account.repository.RoleRepository;
import in.clouthink.synergy.account.repository.UserRepository;
import in.clouthink.synergy.account.repository.UserRoleRelationshipRepository;
import in.clouthink.synergy.account.service.RoleService;
import in.clouthink.synergy.account.spi.AppRoleReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.ValidationException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRelationshipRepository relationshipRepository;

    @Autowired(required = false)
    private List<AppRoleReference> roleReferenceList;

    @Override
    public Role requireSysAdminRole() {
        Role result = roleRepository.findByCode(Roles.SYS_ROLE_NAME_ADMIN);
        if (result == null) {
            throw new RoleNotFoundException("ROLE_ADMIN not found");
        }
        if (RoleType.SYS_ROLE != result.getType()) {
            throw new RoleException("ROLE_ADMIN found but not a SYS_ROLE");
        }
        return result;
    }

    @Override
    public Role requireSysMgrRole() {
        Role result = roleRepository.findByCode(Roles.SYS_ROLE_NAME_MGR);
        if (result == null) {
            throw new RoleNotFoundException("ROLE_MGR not found");
        }
        if (RoleType.SYS_ROLE != result.getType()) {
            throw new RoleException("ROLE_MGR found but not a SYS_ROLE");
        }
        return result;
    }

    @Override
    public Role requireSysUserRole() {
        Role result = roleRepository.findByCode(Roles.SYS_ROLE_NAME_USER);
        if (result == null) {
            throw new RoleNotFoundException("ROLE_USER not found");
        }
        if (RoleType.SYS_ROLE != result.getType()) {
            throw new RoleException("ROLE_USER found but not a SYS_ROLE");
        }
        return result;
    }

    @Override
    public List<Role> listRoles(RoleType roleType) {
        return roleRepository.findListByType(roleType);
    }

    @Override
    public Role findById(String id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role findByCode(String code) {
        if (code == null) {
            return null;
        }
        return roleRepository.findByCode(code.toUpperCase());
    }

    @Override
    public Role createRole(SaveRoleRequest request, RoleType type) {
        if (null == type) {
            throw new RoleException("角色类型不能为空");
        }
        if (StringUtils.isEmpty(request.getCode())) {
            throw new RoleException("角色编码不能为空");
        }
        if (request.getCode().toUpperCase().startsWith("ROLE_")) {
            throw new RoleException("角色编码不需要以ROLE_作为前缀");
        }
        if (Roles.isIllegal(request.getCode())) {
            throw new RoleException("不能使用内置角色编码");
        }
        if (StringUtils.isEmpty(request.getName())) {
            throw new RoleException("角色名称不能为空");
        }

        Role roleByCode = roleRepository.findByCode(request.getCode());
        if (roleByCode != null) {
            throw new RoleException("角色编码不能重复");
        }

        Role roleByName = roleRepository.findByName(request.getName());
        if (roleByName != null) {
            throw new RoleException("角色名称不能重复");
        }

        Role role = new Role();
        role.setCode(request.getCode());
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setType(type);
        role.setCreatedAt(new Date());
        role.setModifiedAt(new Date());
        return roleRepository.save(role);
    }

    @Override
    public void updateRole(String id, SaveRoleRequest request) {
        if (StringUtils.isEmpty(request.getName())) {
            throw new RoleException("角色名称不能为空");
        }

        Role target = findById(id);
        if (target == null) {
            throw new RoleNotFoundException(id);
        }

        Role roleByName = roleRepository.findByName(request.getName());
        if (roleByName != null && !roleByName.getId().equals(target.getId())) {
            throw new RoleException("角色名称不能重复");
        }

        target.setName(request.getName());
        target.setDescription(request.getDescription());
        target.setModifiedAt(new Date());
        roleRepository.save(target);
    }

    @Override
    public void deleteRole(String id) {
        Role role = roleRepository.findById(id);
        if (role == null) {
            return;
        }

        if (relationshipRepository.findFirstByRole(role) != null) {
            throw new RoleException("该角色下已绑定用户,请解除和用户的关系后再进行删除角色操作");
        }

        if (roleReferenceList != null) {
            roleReferenceList.forEach(ref -> {
                if (ref.hasReference(role)) {
                    throw new ValidationException("该数据已经被其他数据引用,不能删除");
                }
            });
        }

        roleRepository.delete(role);
    }

    @Override
    public Page<User> listBindUsers(String roleId, UserQueryRequest request) {
        Role role = findById(roleId);
        if (role == null) {
            throw new RoleNotFoundException();
        }
        Page<UserRoleRelationship> relationships = relationshipRepository.findByRole(role,
                                                                                     new PageRequest(request.getStart(),
                                                                                                     request.getLimit()));
        return new PageImpl<>(relationships.getContent()
                                           .stream()
                                           .map(UserRoleRelationship::getUser)
                                           .collect(Collectors.toList()),
                              new PageRequest(request.getStart(), request.getLimit()),
                              relationships.getTotalElements());
    }

    @Override
    public List<Role> listBindRoles(User user) {
        return relationshipRepository.findListByUser(user).stream().map(r -> r.getRole()).collect(Collectors.toList());
    }

    @Override
    public void bindRoleAndUsers(String roleId, List<String> userIds) {
        Role role = findById(roleId);
        if (role == null) {
            throw new RoleNotFoundException(roleId);
        }
        userIds.forEach(userId -> {
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new UserNotFoundException(userId);
            }
            if (Users.isAdministrator(user)) {
                throw new UserException("Change Administrator's Role is not allowed.");
            }
            tryRelationship(user, role);
        });
    }

    @Override
    public void unbindRoleAndUsers(String roleId, List<String> userIds) {
        Role role = findById(roleId);
        if (role == null) {
            throw new RoleNotFoundException(roleId);
        }
        userIds.forEach(userId -> {
            User user = userRepository.findById(userId);
            if (user == null) {
                return;
            }
            if (Users.isAdministrator(user)) {
                throw new UserException("Change Administrator's Role is not allowed.");
            }
            UserRoleRelationship relationship = relationshipRepository.findByUserAndRole(user, role);
            if (relationship != null) {
                relationshipRepository.delete(relationship);
            }
        });
    }

    @Override
    public void bindUserAndRoles(String userId, List<String> roleIds) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        if (Users.isAdministrator(user)) {
            throw new UserException("Change Administrator's Role is not allowed.");
        }

        roleIds.forEach(roleId -> {
            Role role = roleRepository.findById(roleId);
            if (role == null) {
                throw new RoleNotFoundException(roleId);
            }

            tryRelationship(user, role);
        });
    }

    @Override
    public void unbindUserAndRoles(String userId, List<String> roleIds) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        if (Users.isAdministrator(user)) {
            throw new UserException("Change Administrator's Role is not allowed.");
        }

        roleIds.forEach(roleId -> {
            Role role = roleRepository.findById(roleId);
            if (role == null) {
                return;
            }

            UserRoleRelationship relationship = relationshipRepository.findByUserAndRole(user, role);
            if (relationship != null) {
                relationshipRepository.delete(relationship);
            }
        });
    }

    //*************************************************
    // private
    //*************************************************

    private void tryRelationship(User user, Role role) {
        UserRoleRelationship relationship = relationshipRepository.findByUserAndRole(user, role);
        if (relationship == null) {
            relationship = new UserRoleRelationship();
            relationship.setRole(role);
            relationship.setUser(user);
            relationship.setCreatedAt(new Date());
            relationshipRepository.save(relationship);
        }
    }

}
