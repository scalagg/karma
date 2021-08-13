package com.solexgames.karma.player.impl;

import com.google.gson.annotations.SerializedName;
import com.solexgames.karma.player.KarmaPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author GrowlyX
 * @since 8/13/2021
 */

@Getter
@RequiredArgsConstructor
public class PersistentKarmaPlayer extends KarmaPlayer {

    @SerializedName("_id")
    protected UUID uuidInternal;

    private final UUID uniqueId;
    private final String name;

    @Setter
    private int karma;

}
