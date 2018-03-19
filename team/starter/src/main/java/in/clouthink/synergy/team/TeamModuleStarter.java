package in.clouthink.synergy.team;

import in.clouthink.synergy.team.engine.TeamEngineConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TeamRepositoryConfiguration.class,
        TeamEngineConfiguration.class,
        TeamServiceConfiguration.class,
        TeamRestConfiguration.class,
        TeamMenuConfiguration.class})
public class TeamModuleStarter {

}
