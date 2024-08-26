package mage.cards.s;

import java.util.UUID;
import mage.constants.SubType;
import mage.abilities.keyword.FlashAbility;
import mage.abilities.effects.common.AttachEffect;
import mage.constants.Outcome;
import mage.filter.FilterPermanent;
import mage.target.TargetPermanent;
import mage.abilities.keyword.EnchantAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;

/**
 *
 * @author Momo2907
 */
public final class SugarCoat extends CardImpl {

    private static final FilterPermanent filter = new FilterPermanent("creature or food");

    public SugarCoat(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ENCHANTMENT}, "{2}{U}");
        
        this.subtype.add(SubType.AURA);

        // Flash
        this.addAbility(FlashAbility.getInstance());

        // Enchant creature or Food
        TargetPermanent auraTarget = new TargetPermanent(filter);
        this.getSpellAbility().addTarget(auraTarget);
        this.getSpellAbility().addEffect(new AttachEffect(Outcome.BoostCreature));
        this.addAbility(new EnchantAbility(auraTarget));

        // Enchanted permanent is a colorless Food artifact with "{2}, {T}, Sacrifice this artifact: You gain 3 life" and loses all other card types and abilities.


    }

    private SugarCoat(final SugarCoat card) {
        super(card);
    }

    @Override
    public SugarCoat copy() {
        return new SugarCoat(this);
    }
}
