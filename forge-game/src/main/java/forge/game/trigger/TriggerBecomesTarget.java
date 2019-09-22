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
 * Trigger_BecomesTarget class.
 * </p>
 * 
 * @author Forge
 * @version $Id$
 * @since 1.0.15
 */
public class TriggerBecomesTarget extends Trigger {

    /**
     * <p>
     * Constructor for Trigger_BecomesTarget.
     * </p>
     * 
     * @param params
     *            a {@link java.util.HashMap} object.
     * @param host
     *            a {@link forge.game.card.Card} object.
     * @param intrinsic
     *            the intrinsic
     */
    public TriggerBecomesTarget(final java.util.Map<String, String> params, final Card host, final boolean intrinsic) {
        super(params, host, intrinsic);
    }

    /** {@inheritDoc}
     * @param runParams*/
    @Override
    public final boolean performTest(final Map<AbilityKey, Object> runParams) {
        if (hasParam("SourceType")) {
            final SpellAbility sa = (SpellAbility) runParams.get(AbilityKey.SourceSA);
            if (this.mapParams.get("SourceType").equalsIgnoreCase("spell")) {
                if (!sa.isSpell()) {
                    return false;
                }
            } else if (this.mapParams.get("SourceType").equalsIgnoreCase("ability")) {
                if (!sa.isAbility()) {
                    return false;
                }
            }
        }
        if (hasParam("ValidSource")) {
            if (!matchesValid(((SpellAbility) runParams.get(AbilityKey.SourceSA)).getHostCard(), this.mapParams
                    .get("ValidSource").split(","), this.getHostCard())) {
                return false;
            }
        }
        if (hasParam("ValidTarget")) {
            if (!matchesValid(runParams.get(AbilityKey.Target), this.mapParams.get("ValidTarget").split(","),
                    this.getHostCard())) {
                return false;
            }
        }
        if (hasParam("FirstTime")) {
            if (!runParams.containsKey(AbilityKey.FirstTime)) {
                return false;
            }
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public final void setTriggeringObjects(final SpellAbility sa) {
        sa.setTriggeringObject(AbilityKey.Source, ((SpellAbility) getFromRunParams(AbilityKey.SourceSA)).getHostCard());
        sa.setTriggeringObjectsFrom(this, AbilityKey.SourceSA, AbilityKey.Target);
    }

    @Override
    public String getImportantStackObjects(SpellAbility sa) {
        StringBuilder sb = new StringBuilder();
        sb.append("Source: ").append(((SpellAbility) sa.getTriggeringObject(AbilityKey.SourceSA)).getHostCard()).append(", ");
        sb.append("Target: ").append(sa.getTriggeringObject(AbilityKey.Target));
        return sb.toString();
    }
}
