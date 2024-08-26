package mage.cards.a;

import java.util.*;

import mage.MageInt;
import mage.MageObjectReference;
import mage.abilities.Ability;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.common.AttacksTriggeredAbility;
import mage.abilities.condition.Condition;
import mage.abilities.decorator.ConditionalInterveningIfTriggeredAbility;
import mage.abilities.effects.EffectImpl;
import mage.abilities.effects.common.DrawCardSourceControllerEffect;
import mage.abilities.effects.common.continuous.AddCardSubTypeSourceEffect;
import mage.abilities.effects.common.continuous.AddChosenSubtypeEffect;
import mage.abilities.effects.common.continuous.GainAbilitySourceEffect;
import mage.abilities.keyword.ChangelingAbility;
import mage.constants.*;
import mage.abilities.keyword.IslandwalkAbility;
import mage.abilities.keyword.CrewAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;
import mage.watchers.Watcher;

/**
 *
 * @author Momo2907
 */
public final class Adrestia extends CardImpl {

    public Adrestia(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ARTIFACT}, "{3}");
        
        this.supertype.add(SuperType.LEGENDARY);
        this.subtype.add(SubType.VEHICLE);
        this.power = new MageInt(4);
        this.toughness = new MageInt(3);

        // Islandwalk
        this.addAbility(new IslandwalkAbility());

        // Whenever Adrestia attacks, if an Assassin crewed it this turn, draw a card. Adrestia becomes an Assassin in addition to its other types until end of turn.
        Ability ability =  new ConditionalInterveningIfTriggeredAbility(
                        new AttacksTriggeredAbility(
                                new DrawCardSourceControllerEffect(1), false
                        ), AdrestiaCondition.instance, "Whenever {this} attacks, " +
                        "if an Assassin crewed it this turn, draw a card. " +
                        "Adrestia becomes an Assassin in addition to its other types until end of turn."
        );

        ability.addEffect(new AddCardSubTypeSourceEffect(Duration.EndOfTurn, SubType.ASSASSIN));
        this.addAbility(ability);
        // Crew 1
        this.addAbility(new CrewAbility(1));

    }

    private Adrestia(final Adrestia card) {
        super(card);
    }

    @Override
    public Adrestia copy() {
        return new Adrestia(this);
    }
}

enum AdrestiaCondition implements Condition {
    instance;

    @Override
    public boolean apply(Game game, Ability source) {
        AdrestiaWatcher watcher = game.getState().getWatcher(AdrestiaWatcher.class);
        if (watcher == null){
            return false;
        }
        if (!(AdrestiaWatcher.checkVehicle(source, game))){
            return false;
        }
        return true;
    }
}

class AdrestiaWatcher extends Watcher {

    private final Map<MageObjectReference, Set<MageObjectReference>> crewMap = new HashMap<>();

    AdrestiaWatcher() {
        super(WatcherScope.GAME);
    }

    @Override
    public void watch(GameEvent event, Game game) {
        if (event.getType() != GameEvent.EventType.CREWED_VEHICLE){
            return;
        }
        Permanent vehicle = game.getPermanent(event.getSourceId());
        Permanent crewer = game.getPermanent(event.getTargetId());
        if(vehicle == null || crewer == null){
            return;
        }
        crewMap.computeIfAbsent(
                new MageObjectReference(vehicle, game),
                x -> new HashSet<>()
        ).add(new MageObjectReference(crewer, game));
    }

    @Override
    public void reset() {
        super.reset();
        crewMap.clear();
    }

    public static boolean checkVehicle(Ability source, Game game) {
        AdrestiaWatcher watcher = game.getState().getWatcher(AdrestiaWatcher.class);
        MageObjectReference morAdrestia = new MageObjectReference(game.getPermanent(source.getSourceId()), game);
        if (morAdrestia == null){
            return false;
        }
        for (MageObjectReference mor: watcher.crewMap.getOrDefault(morAdrestia, Collections.emptySet())) {
            Permanent permanent = mor.getPermanentOrLKIBattlefield(game);
            if (permanent == null){
                continue;
            }
            if (permanent.hasSubtype(SubType.ASSASSIN, game) || permanent.hasAbility(new ChangelingAbility(), game)){
                return true;
            }
        }
        return false;
    }
}