package dmillerw.lore;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import dmillerw.lore.common.command.CommandLore;
import dmillerw.lore.common.core.GuiHandler;
import dmillerw.lore.common.core.handler.DefaultFileHandler;
import dmillerw.lore.common.core.handler.PlayerSpawnHandler;
import dmillerw.lore.common.core.handler.PlayerTickHandler;
import dmillerw.lore.common.item.ItemJournal;
import dmillerw.lore.common.item.ItemLorePage;
import dmillerw.lore.common.lore.LoreLoader;
import dmillerw.lore.common.network.NetworkEventHandler;
import dmillerw.lore.common.network.packet.PacketHandler;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * @author dmillerw
 */
@Mod(modid = "LoreExp", name = "Lore Expansion", version = "%MOD_VERSION%", dependencies = "required-after:Forge@[%FORGE_VERSION%,)")
public class LoreExpansion {

    private static final String CONFIG_FOLDER = "LoreExpansion";
    private static final String LORE_FOLDER = "lore";
    private static final String AUDIO_FOLDER = "audio";

    public static final Logger logger = LogManager.getLogger("Lore Expansion");

    @Mod.Instance("LoreExp")
    public static LoreExpansion instance;

    @SidedProxy(serverSide = "dmillerw.lore.CommonProxy", clientSide = "dmillerw.lore.ClientProxy")
    public static CommonProxy proxy;

    public static File configFolder;
    public static File loreFolder;
    public static File audioFolder;

    public static Item lorePage;
    public static Item journal;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        lorePage = new ItemLorePage().setUnlocalizedName("page");
        GameRegistry.registerItem(lorePage, "page");

        journal = new ItemJournal().setUnlocalizedName("journal");
        GameRegistry.registerItem(journal, "journal");

        configFolder = new File(event.getModConfigurationDirectory(), CONFIG_FOLDER);
        loreFolder = new File(configFolder, LORE_FOLDER);
        audioFolder = new File(loreFolder, AUDIO_FOLDER);
        if (!configFolder.exists()) {
            configFolder.mkdir();
        }
        if (!loreFolder.exists()) {
            loreFolder.mkdir();
        }
        if (!audioFolder.exists()) {
            audioFolder.mkdir();
        }

        DefaultFileHandler.initialize();
        LoreLoader.initialize();

        PacketHandler.init();

        FMLCommonHandler.instance().bus().register(new NetworkEventHandler());
        FMLCommonHandler.instance().bus().register(new PlayerTickHandler());

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);

        MinecraftForge.EVENT_BUS.register(new PlayerSpawnHandler());
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandLore());
    }
}
