package mage.abilities.common;

import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.effects.Effect;
import mage.constants.SetTargetPointer;
import mage.constants.TargetController;
import mage.constants.Zone;
import mage.filter.FilterPermanent;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.ZoneChangeBatchEvent;
import mage.game.events.ZoneChangeEvent;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.targetpointer.FixedTarget;

public class SacrificeOneOrMorePermanentsTriggeredAbility extends TriggeredAbilityImpl {

    private final FilterPermanent filter;

    private final TargetController sacrificingPlayer;

    /**
     * Whenever you sacrifice one or more "[filter]", "[effect]".
     * zone = battlefield, setTargetPointer = NONE, optional = false
     */
    public SacrificeOneOrMorePermanentsTriggeredAbility(Effect effect, FilterPermanent filter) {
        this(Zone.BATTLEFIELD, effect, filter, TargetController.YOU, false);
    }

    public SacrificeOneOrMorePermanentsTriggeredAbility(Zone zone, Effect effect, FilterPermanent filter,
                                              TargetController sacrificingPlayer,
                                               boolean optional) {
        super(zone, effect, optional);
        if (Zone.BATTLEFIELD.match(zone)) {
            setLeavesTheBattlefieldTrigger(true);
        }
        this.filter = filter;
        this.sacrificingPlayer = sacrificingPlayer;
        setTriggerPhrase(generateTriggerPhrase());
    }

    protected SacrificeOneOrMorePermanentsTriggeredAbility(final SacrificeOneOrMorePermanentsTriggeredAbility ability) {
        super(ability);
        this.filter = ability.filter;
        this.sacrificingPlayer = ability.sacrificingPlayer;
    }


    @Override
    public SacrificeOneOrMorePermanentsTriggeredAbility copy() {
        return new SacrificeOneOrMorePermanentsTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game){
        return event.getType() == GameEvent.EventType.ZONE_CHANGE_BATCH;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        ZoneChangeBatchEvent events = (ZoneChangeBatchEvent) event;
        switch (sacrificingPlayer) {
            case YOU:
                if (events.getEvents().stream().noneMatch(e -> e.getPlayerId().equals(getControllerId()))) {
                    return false;
                }
                break;
            case OPPONENT:
                Player controller = game.getPlayer(getControllerId());
                if (controller == null || !controller.hasOpponent(event.getPlayerId(), game)) {
                    return false;
                }
                break;
            case ANY:
                break;
            default:
                throw new IllegalArgumentException("Unsupported TargetController in SacrificePermanentTriggeredAbility: " + sacrificingPlayer);
        }
        for (ZoneChangeEvent zEvent: ((ZoneChangeBatchEvent) event).getEvents()) {
            if (!(zEvent.getType() == GameEvent.EventType.SACRIFICED_PERMANENT)){
                continue;
            }
            Permanent permanent = game.getPermanentOrLKIBattlefield(zEvent.getTargetId());
            if (permanent != null && filter.match(permanent, getControllerId(), this, game)){
                return true;
            }
        }
        return false;
    }

    private String generateTriggerPhrase(){
        StringBuilder sb = new StringBuilder();
        switch (sacrificingPlayer) {
            case YOU:
                sb.append("you sacrifice one or more ");
                break;
            case OPPONENT:
                sb.append("an opponent sacrifices one or more");
                break;
            case ANY:
                sb.append("a player sacrifices one or more");
                break;
            default:
                throw new IllegalArgumentException("Unsupported TargetController in SacrificePermanentTriggeredAbility: " + sacrificingPlayer);
        }
        return getWhen() + sb.toString() + filter.getMessage() + ", ";
    }

}
