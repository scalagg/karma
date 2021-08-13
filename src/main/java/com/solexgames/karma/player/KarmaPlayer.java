package com.solexgames.karma.player;

import java.util.UUID;

/**
 * @author GrowlyX
 * @since 8/13/2021
 */

public abstract class KarmaPlayer {

    public abstract UUID getUniqueId();

    public abstract String getName();

    public abstract Integer getKarma();

}
