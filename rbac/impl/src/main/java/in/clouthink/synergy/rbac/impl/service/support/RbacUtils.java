package in.clouthink.synergy.rbac.impl.service.support;

import in.clouthink.synergy.account.domain.model.Role;
import in.clouthink.synergy.account.domain.model.RoleType;
import in.clouthink.synergy.account.domain.model.Roles;
import in.clouthink.synergy.rbac.impl.model.TypedRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

/**
 */
public class RbacUtils {

    public static String buildRoleCode(GrantedAuthority role) {
        String result = role.getAuthority();
        if (role instanceof Role && Roles.isSysRole((Role) role)) {
            result = RoleType.SYS_ROLE.name() + ":" + result;
        }
        else {
            result = RoleType.APP_ROLE.name() + ":" + result;
        }
        return result;
    }

    public static TypedRole convertToTypedRole(GrantedAuthority authority) {
        if (authority instanceof Role) {
            Role role = (Role) authority;
            if (Roles.isSysRole(role)) {
                TypedRole typedRole = TypedRole.newSysRole();
                typedRole.setCode(authority.getAuthority());
                typedRole.setName(((Role) authority).getName());
                return typedRole;
            }

            TypedRole typedRole = TypedRole.newAppRole();
            typedRole.setCode(authority.getAuthority());
            typedRole.setName(((Role) authority).getName());
            return typedRole;
        }
        return null;
    }

    public static boolean checkTypedRoleCodeFormat(String roleCode) {
        String[] splitRoleCode = roleCode.split(":");
        if (splitRoleCode.length != 2) {
            return false;
        }
        if (!RoleType.APP_ROLE.name().equalsIgnoreCase(splitRoleCode[0].toUpperCase()) &&
                !RoleType.SYS_ROLE.name().equalsIgnoreCase(splitRoleCode[0].toUpperCase())) {
            return false;
        }
        if (StringUtils.isEmpty(splitRoleCode[1])) {
            return false;
        }
        return true;
    }

}
