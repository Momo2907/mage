package mage.cards.a;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.AttacksTriggeredAbility;
import mage.abilities.effects.OneShotEffect;
import mage.cards.*;
import mage.constants.*;
import mage.abilities.keyword.FirstStrikeAbility;
import mage.counters.Counter;
import mage.counters.CounterType;
import mage.filter.common.FilterCreatureCard;
import mage.game.Exile;
import mage.game.ExileZone;
import mage.game.Game;
import mage.players.Player;
import mage.target.Target;
import mage.target.common.TargetCardInYourGraveyard;

/**
 *
 * @author Momo2907
 */
public final class AltairIbnLaAhad extends CardImpl {

    private static final FilterCreatureCard filter = new FilterCreatureCard("Assassin creature card");

    static {
        filter.add(SubType.ASSASSIN.getPredicate());
    }

    public AltairIbnLaAhad(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{R}{W}{B}");
        
        this.supertype.add(SuperType.LEGENDARY);
        this.subtype.add(SubType.HUMAN);
        this.subtype.add(SubType.ASSASSIN);
        this.power = new MageInt(3);
        this.toughness = new MageInt(3);

        // First strike
        this.addAbility(FirstStrikeAbility.getInstance());

        // Whenever Altair Ibn-La'Ahad attacks, exile up to one target Assassin creature card from your graveyard with a memory counter on it. Then for each creature card you own in exile with a memory counter on it, create a tapped and attacking token that's a copy of it. Exile those tokens at end of combat.
        Ability ability = new AttacksTriggeredAbility(new AltairIbnLaAhadEffect());
        ability.addTarget(new TargetCardInYourGraveyard(0, 1, filter));
    }

    private AltairIbnLaAhad(final AltairIbnLaAhad card) {
        super(card);
    }

    @Override
    public AltairIbnLaAhad copy() {
        return new AltairIbnLaAhad(this);
    }
}

class AltairIbnLaAhadEffect extends OneShotEffect {

    AltairIbnLaAhadEffect() {
        super(Outcome.Benefit);
    }

    private AltairIbnLaAhadEffect(final AltairIbnLaAhadEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        Cards cards = new CardsImpl(getTargetPointer().getTargets(game, source));
        if (controller == null || cards.isEmpty()){
            return false;
        }
        if(!controller.moveCards(cards, Zone.EXILED, source, game)){
            return false;
        }
        cards.getCards(game)
                .forEach(card -> card.addCounters(CounterType.MEMORY.createInstance(), source, game));
        ExileZone exile = game.getExile().getExileZone(source.getControllerId());
    }

    @Override
    public AltairIbnLaAhadEffect copy() {
        return new AltairIbnLaAhadEffect(this);
    }
}