/*
 * Forge: Play Magic: the Gathering.
 * Copyright (C) 2011  Forge Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package forge.game.trigger;

import forge.game.ability.AbilityKey;
import forge.game.card.Card;
import forge.game.spellability.SpellAbility;

import java.util.Map;

/**
 * <p>
 * Trigger_Blocks class.
 * </p>
 * 
 * @author Forge
 * @version $Id$
 */
public class TriggerBlocks extends Trigger {

    /**
     * <p>
     * Constructor for Trigger_Blocks.
     * </p>
     * 
     * @param params
     *            a {@link java.util.HashMap} object.
     * @param host
     *            a {@link forge.game.card.Card} object.
     * @param intrinsic
     *            the intrinsic
     */
    public TriggerBlocks(final java.util.Map<String, String> params, final Card host, final boolean intrinsic) {
        super(params, host, intrinsic);
    }

    /** {@inheritDoc}
     * @param runParams*/
    @Override
    public final boolean performTest(final Map<AbilityKey, Object> runParams) {
        if (hasParam("ValidCard")) {
            String validBlocker = this.mapParams.get("ValidCard");
            if (!matchesValid(runParams.get(AbilityKey.Blocker), validBlocker.split(","), this.getHostCard())) {
                return false;
            }
        }

        if (hasParam("ValidBlocked")) {
            final String[] validBlockedSplit = this.mapParams.get("ValidBlocked").split(",");
            final Object a = runParams.get(AbilityKey.Attackers);
            if (!(a instanceof Iterable<?>)) {
            	return false;
            }

            final Iterable<?> attackers = (Iterable<?>) a;
            boolean foundMatch = false;
            for (final Object o : attackers) {
            	if (!(o instanceof Card)) {
            		continue;
            	}
            	if (matchesValid(o, validBlockedSplit, this.getHostCard())) {
            		foundMatch = true;
            		break;
            	}
            }

            if (!foundMatch) {
                return false;
            }
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public final void setTriggeringObjects(final SpellAbility sa) {
        sa.setTriggeringObjectsFrom(this, AbilityKey.Blocker, AbilityKey.Attackers);
    }

    @Override
    public String getImportantStackObjects(SpellAbility sa) {
        StringBuilder sb = new StringBuilder();
        sb.append("Blocker: ").append(sa.getTriggeringObject(AbilityKey.Blocker));
        return sb.toString();
    }
}
