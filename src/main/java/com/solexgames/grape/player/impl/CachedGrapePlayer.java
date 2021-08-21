package com.solexgames.grape.player.impl;

import com.solexgames.grape.player.GrapePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * @author GrowlyX
 * @since 8/13/2021
 */

@Getter
@RequiredArgsConstructor
public class CachedGrapePlayer extends GrapePlayer {

    private final UUID uniqueId;
    private final String name;

    private final int karma;
    private final int coins;

}
