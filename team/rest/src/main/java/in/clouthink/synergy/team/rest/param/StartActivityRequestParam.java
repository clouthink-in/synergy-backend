package in.clouthink.synergy.team.rest.param;

import in.clouthink.synergy.team.domain.model.ActivityActionType;
import in.clouthink.synergy.team.domain.request.StartActivityRequest;

import java.util.List;

/**
 *
 */
public class StartActivityRequestParam extends AbstractActivityRequestParam implements StartActivityRequest {

    private List<ActivityActionType> disabledActions;

    @Override
    public List<ActivityActionType> getDisabledActions() {
        return disabledActions;
    }

    public void setDisabledActions(List<ActivityActionType> disabledActions) {
        this.disabledActions = disabledActions;
    }
}
