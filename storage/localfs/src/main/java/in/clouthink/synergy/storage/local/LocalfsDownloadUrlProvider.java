package in.clouthink.synergy.storage.local;

import in.clouthink.daas.fss.core.FileObject;
import in.clouthink.daas.fss.spi.FileObjectService;
import in.clouthink.synergy.storage.LocalfsConfigureProperties;
import in.clouthink.synergy.storage.exception.FileNotFoundException;
import in.clouthink.synergy.storage.spi.DownloadUrlProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author dz
 */
public class LocalfsDownloadUrlProvider implements DownloadUrlProvider {

	@Autowired
	private LocalfsConfigureProperties localfsConfigureProperties;

	@Autowired
	private FileObjectService fileObjectService;

	@Override
	public String getDownloadUrl(String id) {
		FileObject fileObject = fileObjectService.findById(id);
		if (fileObject == null) {
			throw new FileNotFoundException(id);
		}

		return localfsConfigureProperties.getDowloadUrlPrefix() + fileObject.getFinalFilename();
	}

}
