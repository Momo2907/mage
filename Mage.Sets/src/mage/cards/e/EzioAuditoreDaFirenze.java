package mage.cards.e;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.DealsCombatDamageToAPlayerTriggeredAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.common.delayed.ReflexiveTriggeredAbility;
import mage.abilities.condition.Condition;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.decorator.ConditionalInterveningIfTriggeredAbility;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.DoWhenCostPaid;
import mage.abilities.effects.common.LoseGameTargetPlayerEffect;
import mage.abilities.effects.common.continuous.GainAbilityControlledSpellsEffect;
import mage.abilities.keyword.FreerunningAbility;
import mage.constants.*;
import mage.abilities.keyword.MenaceAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.filter.common.FilterNonlandCard;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.mageobject.AbilityPredicate;
import mage.game.Game;
import mage.players.Player;

/**
 *
 * @author Momo2907
 */
public final class EzioAuditoreDaFirenze extends CardImpl {

    private static final FilterNonlandCard filter = new FilterNonlandCard("Assassin spells you cast");

    static {
        filter.add(SubType.ASSASSIN.getPredicate());
        filter.add(Predicates.not(new AbilityPredicate(FreerunningAbility.class)));
    }

    public EzioAuditoreDaFirenze(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{1}{B}");
        
        this.supertype.add(SuperType.LEGENDARY);
        this.subtype.add(SubType.HUMAN);
        this.subtype.add(SubType.ASSASSIN);
        this.power = new MageInt(3);
        this.toughness = new MageInt(2);

        // Menace
        this.addAbility(new MenaceAbility());

        // Assassin spells you cast have freerunning {B}{B}.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new GainAbilityControlledSpellsEffect(new FreerunningAbility(new ManaCostsImpl<>("{B}{B}")), filter)));
        // Whenever Ezio deals combat damage to a player, you may pay {W}{U}{B}{R}{G} if that player has 10 or less life. When you do, that player loses the game.
        ReflexiveTriggeredAbility reflexive = new ReflexiveTriggeredAbility(
                new LoseGameTargetPlayerEffect(), false
        );
        ConditionalInterveningIfTriggeredAbility intervening = new ConditionalInterveningIfTriggeredAbility(
                new DealsCombatDamageToAPlayerTriggeredAbility(
                        new DoWhenCostPaid(reflexive, new ManaCostsImpl<>("{W}{U}{B}{R}{G}"), "Pay {W}{U}{B}{R}{G}", true),
                true, true)
        )
    }

    private EzioAuditoreDaFirenze(final EzioAuditoreDaFirenze card) {
        super(card);
    }

    @Override
    public EzioAuditoreDaFirenze copy() {
        return new EzioAuditoreDaFirenze(this);
    }
}

enum EzioAuditoreDaFirenzeCondition implements Condition {
    ;

    @Override
    public boolean apply(Game game, Ability source) {

    }

}