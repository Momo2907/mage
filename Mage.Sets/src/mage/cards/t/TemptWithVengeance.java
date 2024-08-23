
package mage.cards.t;

import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.game.Game;
import mage.game.permanent.token.Elemental11HasteToken;
import mage.game.permanent.token.Token;
import mage.players.Player;
import mage.util.CardUtil;

import java.util.UUID;

/**
 *
 * @author LevelX2
 */
public final class TemptWithVengeance extends CardImpl {

    public TemptWithVengeance(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.SORCERY}, "{X}{R}");

        // Tempting offer - create X 1/1 red Elemental creature tokens with haste. Each opponent may create X 1/1 red Elemental creature tokens with haste. For each opponent who does, create X 1/1 red Elemental creature tokens with haste.
        this.getSpellAbility().addEffect(new TemptWithVengeanceEffect());
    }

    private TemptWithVengeance(final TemptWithVengeance card) {
        super(card);
    }

    @Override
    public TemptWithVengeance copy() {
        return new TemptWithVengeance(this);
    }
}

class TemptWithVengeanceEffect extends OneShotEffect {

    TemptWithVengeanceEffect() {
        super(Outcome.PutLandInPlay);
        this.staticText = "<i>Tempting offer</i> &mdash; Create X 1/1 red Elemental creature tokens with haste. Each opponent may create X 1/1 red Elemental creature tokens with haste. For each opponent who does, create X 1/1 red Elemental creature tokens with haste";
    }

    private TemptWithVengeanceEffect(final TemptWithVengeanceEffect effect) {
        super(effect);
    }

    @Override
    public TemptWithVengeanceEffect copy() {
        return new TemptWithVengeanceEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        int xValue = CardUtil.getSourceCostsTag(game, source, "X", 0);
        if (controller != null && xValue > 0) {

            Token tokenCopy = new Elemental11HasteToken();
            tokenCopy.putOntoBattlefield(xValue, game, source, source.getControllerId(), false, false);

            int opponentsAddedTokens = 0;
            for (UUID playerId : game.getOpponents(controller.getId())) {
                Player opponent = game.getPlayer(playerId);
                if (opponent != null) {
                    if (opponent.chooseUse(outcome, "Create " + xValue + " Elemental tokens?", source, game)) {
                        opponentsAddedTokens++;
                        tokenCopy.putOntoBattlefield(xValue, game, source, playerId, false, false);
                    }
                }
            }
            if (opponentsAddedTokens > 0) {
                // Each batch of tokens is independent, per ruling:
                // After each opponent has decided, the effect happens simultaneously for each one who accepted the
                // offer. Then, the effect happens again for you a number of times equal to the number of opponents who
                // accepted.
                // (2013-10-17)
                for (int i = 0; i < opponentsAddedTokens; i++) {
                    tokenCopy.putOntoBattlefield(opponentsAddedTokens, game, source, source.getControllerId(), false, false);
                }
            }
            return true;
        }

        return false;
    }
}
