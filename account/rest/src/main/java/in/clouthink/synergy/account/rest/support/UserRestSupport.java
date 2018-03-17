package in.clouthink.synergy.account.rest.support;

import in.clouthink.synergy.account.domain.model.User;
import in.clouthink.synergy.account.rest.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 *
 */
public interface UserRestSupport {

    Page<UserSummary> listUsers(UserQueryParameter queryRequest);

    UserDetail getUserDetail(String id);

    User createUser(SaveUserParameter request);

    void updateUser(String id, SaveUserParameter request);

    void deleteUser(String id, User byWho);

    void changePassword(String id, ChangePasswordRequest request);

    void enableUser(String id);

    void disableUser(String id);

    void lockUser(String id);

    void unlockUser(String id);

    List<GroupOfAppUser> listGroupsOfUser(String userId);

    void updateUserGroupRelationship(String userId, String[] split);

}
