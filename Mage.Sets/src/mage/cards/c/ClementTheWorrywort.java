package mage.cards.c;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.EntersBattlefieldThisOrAnotherTriggeredAbility;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.ReturnToHandChosenControlledPermanentEffect;
import mage.constants.*;
import mage.abilities.keyword.VigilanceAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.filter.StaticFilters;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.Target;
import mage.target.common.TargetControlledCreaturePermanent;

/**
 *
 * @author Momo2907
 */
public final class ClementTheWorrywort extends CardImpl {

    static final FilterControlledCreaturePermanent filter = new FilterControlledCreaturePermanent();

    public ClementTheWorrywort(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{1}{G}{U}");
        
        this.supertype.add(SuperType.LEGENDARY);
        this.subtype.add(SubType.FROG);
        this.subtype.add(SubType.DRUID);
        this.power = new MageInt(3);
        this.toughness = new MageInt(3);

        // Vigilance
        this.addAbility(VigilanceAbility.getInstance());

        // Whenever Clement, the Worrywort or another creature you control enters, return up to one target creature you control with lesser mana value to its owner's hand.
        Ability ability = new EntersBattlefieldThisOrAnotherTriggeredAbility(
                new ReturnToHandChosenControlledPermanentEffect(), StaticFilters.FILTER_PERMANENT_CREATURE, true, SetTargetPointer.PERMANENT, true
        );
        ability.addTarget(new TargetControlledCreaturePermanent())
        // Frogs you control have "{T}: Add {G} or {U}. Spend this mana only to cast a creature spell."
    }

    private ClementTheWorrywort(final ClementTheWorrywort card) {
        super(card);
    }

    @Override
    public ClementTheWorrywort copy() {
        return new ClementTheWorrywort(this);
    }
}

class ClementTheWorrywortEffect extends OneShotEffect {

    ClementTheWorrywortEffect(){
        super(Outcome.ReturnToHand);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player player = game.getPlayer(source.getControllerId());
        Permanent permanent = getTargetPointer().getFirstTargetPermanentOrLKI(game, source);
        if (permanent == null){
            return false;
        }
        player.moveCardToHandWithInfo()
    }

    @Override
    public ClementTheWorrywortEffect copy() {
        return null;
    }
}