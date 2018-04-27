package in.clouthink.synergy.rbac.annotation;

import in.clouthink.synergy.rbac.annotation.support.EnableResourceImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enable the resource plugin, it means it's a resource plugin , can be registered into the repository by system automatically.
 *
 * @author dz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableResourceImportSelector.class)
public @interface EnableResource {

    /**
     * @return the flatten resource provided by the plugin
     */
    Resource[] resource();

}
