package in.clouthink.synergy.team.actor;

import akka.actor.UntypedActor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @auther dz
 */
@Component
@Scope("prototype")
public class StartActivityActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {

    }
}
