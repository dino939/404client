package com.denger.client.another.hooks;

import com.mojang.authlib.minecraft.SocialInteractionsService;

import java.util.UUID;

public class SocialInteractionsServiceHook implements SocialInteractionsService {
    @Override
    public boolean serversAllowed() {
        return true;
    }

    @Override
    public boolean realmsAllowed() {
        return false;
    }

    @Override
    public boolean chatAllowed() {
        return true;
    }

    @Override
    public boolean isBlockedPlayer(UUID playerID) {
        return false;
    }


}
