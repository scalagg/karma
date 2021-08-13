package com.solexgames.karma.player.impl;

import com.solexgames.karma.player.KarmaPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * @author GrowlyX
 * @since 8/13/2021
 */

@Getter
@RequiredArgsConstructor
public class CachedKarmaPlayer extends KarmaPlayer {

    private final UUID uniqueId;
    private final String name;

    private final int karma;

}
