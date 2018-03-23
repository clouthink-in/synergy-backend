package in.clouthink.synergy.rbac.rest.service.impl;

import in.clouthink.synergy.rbac.rest.view.PrivilegedResourceWithChildrenView;
import in.clouthink.synergy.rbac.rest.service.ResourceCacheService;
import in.clouthink.synergy.rbac.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author dz
 */
@Service
public class ResourceCacheServiceMemoryImpl implements ResourceCacheService {

	public static final Object LOCK_OBJECT = new Object();

	@Autowired
	private ResourceService resourceService;

	private String cacheHash = UUID.randomUUID().toString();

	private List<PrivilegedResourceWithChildrenView> cachedValue;

	@Override
	public List<PrivilegedResourceWithChildrenView> listResources() {
		//try get cache
		if (!isDirty()) {
			return cachedValue;
		}

		synchronized (LOCK_OBJECT) {
			//try again
			if (!isDirty()) {
				return cachedValue;
			}

			//else build new one
			List<PrivilegedResourceWithChildrenView> result = resourceService.getRootResources()
																			 .stream()
																			 .filter(resource -> !resource.isOpen())
																			 .map(resource -> PrivilegedResourceWithChildrenView
																				 .from(resource))
																			 .collect(Collectors.toList());

			processChildren(result);

			//cache it
			cachedValue = result;
			cacheHash = resourceService.getHashcode();

			return result;
		}
	}

	@Override
	public List<PrivilegedResourceWithChildrenView> listResources(boolean cached) {
		if (cached) {
			return listResources();
		}

		List<PrivilegedResourceWithChildrenView> result = resourceService.getRootResources()
																		 .stream()
																		 .filter(resource -> !resource.isOpen())
																		 .map(resource -> PrivilegedResourceWithChildrenView
																				 .from(
																			 resource))
																		 .collect(Collectors.toList());

		processChildren(result);

		return result;
	}

	private boolean isDirty() {
		return !resourceService.getHashcode().equals(cacheHash);
	}

	private void processChildren(List<PrivilegedResourceWithChildrenView> result) {
		result.stream().forEach(resource -> {
			List<PrivilegedResourceWithChildrenView> children = resourceService.getResourceChildren(resource.getCode())
																			   .stream()
																			   .filter(child -> !child.isOpen())
																			   .map(child -> PrivilegedResourceWithChildrenView
																					   .from(
																				   child))
																			   .collect(Collectors.toList());
			resource.getChildren().addAll(children);
			processChildren(children);
		});
	}

}
