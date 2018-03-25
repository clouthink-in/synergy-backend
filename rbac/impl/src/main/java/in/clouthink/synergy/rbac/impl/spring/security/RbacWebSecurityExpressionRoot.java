package in.clouthink.synergy.rbac.impl.spring.security;

import in.clouthink.synergy.account.domain.model.Roles;
import in.clouthink.synergy.rbac.model.Resource;
import in.clouthink.synergy.rbac.service.PermissionService;
import in.clouthink.synergy.rbac.service.ResourceService;
import in.clouthink.synergy.rbac.support.matcher.ResourceMatchers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;

import java.util.Collection;

/**
 * RBAC WebSecurityExpressionRoot impl.
 *
 * @author dz
 */
public class RbacWebSecurityExpressionRoot extends WebSecurityExpressionRoot {

    private FilterInvocation filterInvocation;

    private PermissionService permissionService;

    private ResourceService resourceService;

    public RbacWebSecurityExpressionRoot(Authentication a,
                                         FilterInvocation fi,
                                         PermissionService permissionService,
                                         ResourceService resourceService) {
        super(a, fi);
        this.filterInvocation = fi;
        this.permissionService = permissionService;
        this.resourceService = resourceService;
    }

    /**
     * <p>
     * http.authorizeRequests()
     * .accessDecisionManager(accessDecisionManager())
     * .antMatchers("put the wanted url here")
     * .access("passRbacCheck")
     * <p>
     *
     * @return
     */
    public boolean isPassRbacCheck() {
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication);
        //no permission if the request is not from system role user
        if (!authorities.contains(Roles.ROLE_USER)) {
            return false;
        }
        //the admin role will get the permission automatically
        if (authorities.contains(Roles.ROLE_ADMIN)) {
            return true;
        }

        // Attempt to find a matching granted authority
        String requestUrl = filterInvocation.getRequestUrl();
        Resource resource = resourceService.getFirstMatchedResource(ResourceMatchers.matchAntPath(requestUrl));
        if (resource != null) {
            for (GrantedAuthority authority : authorities) {
                if (permissionService.isGranted(resource.getCode(), authority)) {
                    return true;
                }
            }
        }

        return false;
    }

    Collection<? extends GrantedAuthority> extractAuthorities(Authentication authentication) {
        return authentication.getAuthorities();
    }

}
