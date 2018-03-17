package in.clouthink.synergy.account.rest.support.impl;

import in.clouthink.synergy.account.domain.model.Group;
import in.clouthink.synergy.account.domain.model.SysRole;
import in.clouthink.synergy.account.domain.model.User;
import in.clouthink.synergy.account.exception.UserNotFoundException;
import in.clouthink.synergy.account.rest.dto.*;
import in.clouthink.synergy.account.service.AccountService;
import in.clouthink.synergy.account.rest.support.UserRestSupport;
import in.clouthink.synergy.account.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserRestSupportImpl implements UserRestSupport {

    @Autowired
    private AccountService accountService;

    @Autowired
    private GroupService groupService;

    @Override
    public Page<UserSummary> listUsers(UserQueryParameter queryRequest) {
        Page<User> userPage = accountService.listUsers(queryRequest);
        return new PageImpl<>(userPage.getContent().stream().map(UserSummary::from).collect(Collectors.toList()),
                              new PageRequest(queryRequest.getStart(), queryRequest.getLimit()),
                              userPage.getTotalElements());
    }

    @Override
    public UserDetail getUserDetail(String id) {
        User user = accountService.findById(id);
        return UserDetail.from(user);
    }

    @Override
    public User createUser(SaveUserParameter request) {
        return accountService.createAccount(request, SysRole.ROLE_USER);
    }

    @Override
    public void updateUser(String id, SaveUserParameter request) {
        accountService.updateAccount(id, request);
    }

    @Override
    public void deleteUser(String id, User byWho) {
        accountService.archiveAccount(id, byWho);
    }

    @Override
    public void changePassword(String id, ChangePasswordRequest request) {
        accountService.changePassword(id, request.getNewPassword());
    }

    @Override
    public void enableUser(String id) {
        accountService.enable(id);
    }

    @Override
    public void disableUser(String id) {
        accountService.disable(id);
    }

    @Override
    public void lockUser(String id) {
        accountService.lock(id);
    }

    @Override
    public void unlockUser(String id) {
        accountService.unlock(id);
    }

    @Override
    public List<GroupOfAppUser> listGroupsOfUser(String userId) {
        User user = accountService.findById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        List<Group> groups = groupService.listBindGroups(user);
        if (groups == null) {
            return null;
        }
        return groups.stream().map(GroupOfAppUser::from).collect(Collectors.toList());
    }

    @Override
    public void updateUserGroupRelationship(String userId, String[] groupIds) {
        groupService.bindUserAndGroups(userId, groupIds);
    }

}
