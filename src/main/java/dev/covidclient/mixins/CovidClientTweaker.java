package dev.covidclient.mixins;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CovidClientTweaker implements ITweaker
{
    /**
     * This stores the Minecraft launch arguments
     */
    private List<String> launchArgs = new ArrayList<>();

    /**
     * This handles the launch arguments passed towards minecraft
     * @param args The launch arguments
     * @param gameDir The game directory (ie: .minecraft)
     * @param assetsDir The assets directory
     * @param profile The game profile
     */
    @Override
    public final void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile)
    {
        // Add the launch arguments to our launchArgs array
        this.launchArgs.addAll(args);

        // Constants
        final String VERSION = "--version";
        final String ASSET_DIR = "--assetDir";
        final String GAME_DIR = "--gameDir";

        // Check if version is passed as a launch argument, if not add it
        if (!args.contains(VERSION) && profile != null)
        {
            launchArgs.add(VERSION);
            launchArgs.add(profile);
        }

        // Check if assetDir is passed as a launch argument, if not add it
        if (!args.contains(ASSET_DIR) && profile != null)
        {
            launchArgs.add(ASSET_DIR);
            launchArgs.add(profile);
        }

        // Check if gameDir is passed as a launch argument, if not add it
        if (!args.contains(GAME_DIR) && profile != null)
        {
            launchArgs.add(GAME_DIR);
            launchArgs.add(profile);
        }
    }

    /**
     * Inject into the MC class loader
     * @param classLoader The class loader
     */
    @Override
    public final void injectIntoClassLoader(LaunchClassLoader classLoader)
    {
        MixinBootstrap.init();

        // Retrieve the default mixin environment and register the config file
        MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();

        environment.addConfiguration("mixins.MixinClientTutorial.json");

        // Check if the obfuscation context is null
        if (environment.getObfuscationContext() == null)
        {
            environment.setObfuscationContext("notch");
        }

        // This is a client side, client :)
        environment.setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override
    public String getLaunchTarget()
    {
        return MixinBootstrap.getPlatform().getLaunchTarget();
    }

    @Override
    public String[] getLaunchArguments()
    {
        return launchArgs.toArray(new String[0]);
    }
}