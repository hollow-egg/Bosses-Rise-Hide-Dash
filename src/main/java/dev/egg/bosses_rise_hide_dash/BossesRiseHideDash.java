package dev.egg.bosses_rise_hide_dash;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(BossesRiseHideDash.MODID)
public class BossesRiseHideDash {
    public static final String MODID = "bosses_rise_hide_dash";
    private static final Logger LOGGER = LogUtils.getLogger();
    protected static int decayTimer = 0;
    private static final int decaySeconds = 3;
    protected static int lastRollCount = 1;

    public BossesRiseHideDash(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(EventHandler.class);
    }

    public static boolean ShouldShowDash() {
        return decayTimer < decaySeconds;
    }

    public static void ShowDash(boolean flag)
    {
        decayTimer = flag ? 0 : decaySeconds;
    }
}

