package mage.cards.c;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SacrificeOneOrMorePermanentsTriggeredAbility;
import mage.abilities.common.SacrificePermanentTriggeredAbility;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.common.ForageCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.abilities.effects.common.continuous.GainAbilityControlledEffect;
import mage.abilities.effects.common.counter.AddCountersAllEffect;
import mage.constants.Duration;
import mage.constants.SubType;
import mage.constants.SuperType;
import mage.abilities.keyword.MenaceAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.counters.CounterType;
import mage.filter.FilterPermanent;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.predicate.mageobject.AnotherPredicate;
import mage.game.permanent.token.SquirrelToken;

/**
 *
 * @author Momo2907
 */
public final class CamelliaTheSeedmiser extends CardImpl {

    private static final FilterControlledCreaturePermanent filterSquirrels = new FilterControlledCreaturePermanent(SubType.SQUIRREL, "Squirrels you control");
    private static final FilterPermanent filterFood = new FilterPermanent(SubType.FOOD, "Foods");
    private static final FilterControlledCreaturePermanent filterOtherSquirrel = new FilterControlledCreaturePermanent(SubType.SQUIRREL, "other Squirrel you control");
    static {
        filterOtherSquirrel.add(AnotherPredicate.instance);
    }

    public CamelliaTheSeedmiser(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{1}{B}{G}");
        
        this.supertype.add(SuperType.LEGENDARY);
        this.subtype.add(SubType.SQUIRREL);
        this.subtype.add(SubType.WARLOCK);
        this.power = new MageInt(3);
        this.toughness = new MageInt(3);

        // Menace
        this.addAbility(new MenaceAbility());

        // Other Squirrels you control have menace.
        this.addAbility(new SimpleStaticAbility(new GainAbilityControlledEffect(
                new MenaceAbility(), Duration.WhileOnBattlefield, filterSquirrels, true
        )));
        // Whenever you sacrifice one or more Foods, create a 1/1 green Squirrel creature token.
        this.addAbility(new SacrificeOneOrMorePermanentsTriggeredAbility(
                new CreateTokenEffect(new SquirrelToken()),
                filterFood
        ));
        // {2}, Forage: Put a +1/+1 counter on each other Squirrel you control.
        Ability ability = new SimpleActivatedAbility(
                new AddCountersAllEffect(CounterType.P1P1.createInstance(), filterOtherSquirrel),
                new ManaCostsImpl<>("{2}")
        );

        ability.addCost(new ForageCost());
        this.addAbility(ability);
    }

    private CamelliaTheSeedmiser(final CamelliaTheSeedmiser card) {
        super(card);
    }

    @Override
    public CamelliaTheSeedmiser copy() {
        return new CamelliaTheSeedmiser(this);
    }
}
