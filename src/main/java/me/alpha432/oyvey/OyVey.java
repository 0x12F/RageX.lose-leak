package me.alpha432.oyvey;

import me.alpha432.oyvey.manager.*;
import me.alpha432.oyvey.util.TextUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import me.alpha432.oyvey.auth.AntiDump;
import me.alpha432.oyvey.auth.Auth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//wtf is this auth?????

public class OyVey implements ModInitializer, ClientModInitializer {
    public static final String NAME = "RageX";
    public static final String VERSION = "v1-beta ";

    public static float TIMER = 1f;

    public static final Logger LOGGER = LogManager.getLogger("RageX");
    public static ServerManager serverManager;
    public static ColorManager colorManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static HoleManager holeManager;
    public static EventManager eventManager;
    public static SpeedManager speedManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;
    public static boolean authed;
    public static AntiDump antiDump;
	public static boolean loaded = false;

    @Override
    public void onInitialize() {
            eventManager = new EventManager();
            serverManager = new ServerManager();
            rotationManager = new RotationManager();
            positionManager = new PositionManager();
            friendManager = new FriendManager();
            colorManager = new ColorManager();
            commandManager = new CommandManager();
            moduleManager = new ModuleManager();
            speedManager = new SpeedManager();
            holeManager = new HoleManager();
            TextUtil.init();
    }

    @Override
    public void onInitializeClient() {
        eventManager.init();
        moduleManager.init();

        configManager = new ConfigManager();
        configManager.load();
        colorManager.init();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> configManager.save()));
    }
}
