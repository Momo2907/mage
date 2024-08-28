package mage.cards.s;

import java.util.UUID;

import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.ContinuousEffectImpl;
import mage.abilities.token.FoodAbility;
import mage.constants.*;
import mage.abilities.keyword.FlashAbility;
import mage.abilities.effects.common.AttachEffect;
import mage.filter.FilterPermanent;
import mage.filter.predicate.Predicates;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.target.TargetPermanent;
import mage.abilities.keyword.EnchantAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;

/**
 *
 * @author Momo2907
 */
public final class SugarCoat extends CardImpl {

    private static final FilterPermanent filter = new FilterPermanent("creature or food");

    static {
        filter.add(Predicates.or(
                CardType.CREATURE.getPredicate(),
                SubType.FOOD.getPredicate()
        ));
    }

    public SugarCoat(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ENCHANTMENT}, "{2}{U}");
        
        this.subtype.add(SubType.AURA);

        // Flash
        this.addAbility(FlashAbility.getInstance());

        // Enchant creature or Food
        TargetPermanent auraTarget = new TargetPermanent(filter);
        this.getSpellAbility().addTarget(auraTarget);
        this.getSpellAbility().addEffect(new AttachEffect(Outcome.Detriment));
        this.addAbility(new EnchantAbility(auraTarget));

        // Enchanted permanent is a colorless Food artifact with "{2}, {T}, Sacrifice this artifact: You gain 3 life" and loses all other card types and abilities.
        this.addAbility(new SimpleStaticAbility(new SugarCoatEffect()));
    }

    private SugarCoat(final SugarCoat card) {
        super(card);
    }

    @Override
    public SugarCoat copy() {
        return new SugarCoat(this);
    }
}

class SugarCoatEffect extends ContinuousEffectImpl {

    public SugarCoatEffect() {
        super(Duration.WhileOnBattlefield, Outcome.Detriment);
        staticText = "Enchanted permanent is a colorless Food artifact with " +
                "\"{2}, {T}, Sacrifice this artifact: You gain 3 life\" and loses all other card types and abilities";
    }

    public SugarCoatEffect(final SugarCoatEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return false;
    }

    @Override
    public SugarCoatEffect copy() {
        return new SugarCoatEffect(this);
    }

    @Override
    public boolean apply(Layer layer, SubLayer sublayer, Ability source, Game game) {
        Permanent enchantment = source.getSourcePermanentIfItStillExists(game);
        if (enchantment == null) {
            return false;
        }
        Permanent permanent = game.getPermanent(enchantment.getAttachedTo());
        if (permanent == null) {
            return false;
        }
        switch (layer) {
            case TypeChangingEffects_4:
                permanent.removeAllCardTypes(game);
                permanent.addCardType(game, CardType.ARTIFACT);
                permanent.removeAllSubTypes(game);
                permanent.addSubType(game, SubType.FOOD);
                break;
            case ColorChangingEffects_5:
                permanent.getColor(game).setWhite(false);
                permanent.getColor(game).setBlue(false);
                permanent.getColor(game).setBlack(false);
                permanent.getColor(game).setRed(false);
                permanent.getColor(game).setGreen(false);
                break;
            case AbilityAddingRemovingEffects_6:
                permanent.removeAllAbilities(source.getSourceId(), game);
                permanent.addAbility(new FoodAbility(false), source.getSourceId(), game);
                break;
        }
        return true;
    }

    @Override
    public boolean hasLayer(Layer layer) {
        return layer == Layer.AbilityAddingRemovingEffects_6
                || layer == Layer.ColorChangingEffects_5
                || layer == Layer.TypeChangingEffects_4;
    }
}