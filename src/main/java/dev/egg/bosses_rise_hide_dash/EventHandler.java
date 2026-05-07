package dev.egg.bosses_rise_hide_dash;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.unusual.block_factorys_bosses.attachment.entity.RollAttachment;

import static dev.egg.bosses_rise_hide_dash.BossesRiseHideDash.*;

@EventBusSubscriber({Dist.CLIENT})
public class EventHandler {

    @SubscribeEvent
    public static void onClientJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.isClientSide)
        {
            lastRollCount = RollAttachment.fromPlayer(event.getEntity()).rollCount();
        }
    }

    //implementation inspired by from eerussionguy's Combat Music mod
    @SubscribeEvent
    public static void onClientTick(final ClientTickEvent.Post event)
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.isClientSide) {
            LocalPlayer player = mc.player;
            if (player != null) {
                if (ShouldShowDash()) {
                    if (mc.level.getGameTime() % 20 == 0 && getEntities(player) == 0) {
                        decayTimer++;
                    }
                }

                //if rolling
                RollAttachment attachment = RollAttachment.fromPlayer(player);
                if(attachment.isRolling())
                {
                    ShowDash(true);
                    return;
                }

                // check for change in roll count (equipping armor)
                int count = attachment.rollCount();
                if (count != lastRollCount)
                {
                    lastRollCount = count;
                    ShowDash(true);
                    return;
                }

                // if dashes are recharging
                float ready = 0;
                for (int i = 0; i < count; ++i) {
                    ready += 1.0F - attachment.getCooldown(i);
                }
                if (ready < count)
                    ShowDash(true);
            }
        }
    }

    @SubscribeEvent
    public static void onAttack(final AttackEntityEvent event)
    {
        LivingEntity entity = event.getEntity();
        Entity source = event.getTarget();
        if (entity instanceof LocalPlayer && source instanceof Monster)
        {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && mc.level.isClientSide && mc.level.getDifficulty() != Difficulty.PEACEFUL) {
                ShowDash(true);
            }
        }
    }

    @SubscribeEvent
    private static void onDamage(final LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        Entity source = event.getSource().getEntity();
        if (entity instanceof Player && source instanceof Monster) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && mc.level.isClientSide) {
                ShowDash(true);
            }
        }
    }

    private static int getEntities(LocalPlayer player)
    {
        return player.clientLevel.getEntitiesOfClass(Monster.class, new AABB(-12D, -10D, -12D, 12D, 10D, 12D).move(player.blockPosition()), mob -> mob.canAttack(player, TargetingConditions.DEFAULT)).size();
    }
}
