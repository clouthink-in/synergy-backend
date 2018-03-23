package in.clouthink.synergy.menu.rest.support.impl;

import in.clouthink.synergy.account.domain.model.User;
import in.clouthink.synergy.menu.rest.view.MenuView;
import in.clouthink.synergy.menu.rest.support.UserProfileExtensionRestSupport;
import in.clouthink.synergy.rbac.model.Action;
import in.clouthink.synergy.rbac.model.Resource;
import in.clouthink.synergy.rbac.model.ResourceChild;
import in.clouthink.synergy.rbac.service.PermissionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserProfileExtensionRestSupportImpl implements UserProfileExtensionRestSupport {

	private static final Log logger = LogFactory.getLog(UserProfileExtensionRestSupportImpl.class);

	@Autowired
	private PermissionService permissionService;

	@Override
	public List<MenuView> getGrantedMenus(User user) {
		List<Resource> grantedResourceList = permissionService.getGrantedResources((List) user.getAuthorities());

		//flatten menu cache
		Map<String,MenuView> menuRepository = grantedResourceList.stream()
																 .collect(Collectors.toMap(resource -> resource.getCode(),
																						   MenuView::from));

		//build the tree
		grantedResourceList.stream().forEach(resource -> {
			if (resource instanceof ResourceChild) {
				MenuView parent = menuRepository.get(((ResourceChild) resource).getParentCode());
				if (parent != null) {
					parent.getChildren().add(menuRepository.get(resource.getCode()));
				}
				else {
					logger.warn(String.format("The parent menu[code=%] for child[code=%s] not found ",
											  ((ResourceChild) resource).getParentCode(),
											  resource.getCode()));
				}
			}
		});


		//return the root
		return grantedResourceList.stream()
								  .filter(resource -> !(resource instanceof ResourceChild))
								  .map(resource -> menuRepository.get(resource.getCode()))
								  .collect(Collectors.toList());
	}

	@Override
	public List<Action> getGrantedActions(String resourceCode, User user) {
		return permissionService.getGrantedActions(resourceCode, (List) user.getAuthorities());
	}
}
