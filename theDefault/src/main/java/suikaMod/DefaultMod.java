package suikaMod;

import basemod.*;
import basemod.devcommands.ConsoleCommand;
import basemod.eventUtil.AddEventParams;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import suikaMod.cards.*;
import suikaMod.characters.TheDefault;
import suikaMod.events.IdentityCrisisEvent;
import suikaMod.potions.PlaceholderPotion;
import suikaMod.relics.BottledPlaceholderRelic;
import suikaMod.relics.DefaultClickableRelic;
import suikaMod.relics.PlaceholderRelic;
import suikaMod.relics.PlaceholderRelic2;
import suikaMod.util.IDCheckDontTouchPls;
import suikaMod.util.TextureLoader;
import suikaMod.variables.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
// Please don't just mass replace "theDefault" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 4 places.
// I comment those places below, under the place where you set your ID.

//TODO: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "theDefault:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "theDefault", and change to "yourmodname" rather than "thedefault".
// You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories, and press alt+c to make the replace case sensitive (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class DefaultMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        PostBattleSubscriber,
        OnCardUseSubscriber,
        OnPlayerTurnStartSubscriber,
        StartActSubscriber,
        OnStartBattleSubscriber,
        OnPlayerDamagedSubscriber,
        PreMonsterTurnSubscriber,
        PostDrawSubscriber,
        PostExhaustSubscriber,
        PostPotionUseSubscriber

{
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(DefaultMod.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties theDefaultDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true; // The boolean we'll be setting on/off (true/false)

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Suika Mod";
    private static final String AUTHOR = "Suika"; // And pretty soon - You!
    private static final String DESCRIPTION = "A base for Slay the Spire to start your own mod from, feat. the Default.";

    public static String MODID = "suikaMod"; //IMPORTANT ID

    // =============== INPUT TEXTURE LOCATION =================

    // Colors (RGB)
    // Character Color
    public static final Color DEFAULT_GRAY = CardHelper.getColor(64.0f, 70.0f, 70.0f);

    // Potion Colors in RGB
    public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
    public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
    public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown

    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!

    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_DEFAULT_GRAY = MODID + "Resources/images/512/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY = MODID + "Resources/images/512/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY = MODID + "Resources/images/512/bg_power_default_gray.png";

    private static final String ENERGY_ORB_DEFAULT_GRAY = MODID + "Resources//images/512/card_default_gray_orb.png";
    private static final String CARD_ENERGY_ORB = MODID + "Resources//images/512/card_small_orb.png";

    private static final String ATTACK_DEFAULT_GRAY_PORTRAIT = MODID + "Resources//images/1024/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY_PORTRAIT = MODID + "Resources//images/1024/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY_PORTRAIT = MODID + "Resources//images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = MODID + "Resources//images/1024/card_default_gray_orb.png";

    // Character assets
    private static final String THE_DEFAULT_BUTTON = MODID + "Resources//images/charSelect/DefaultCharacterButton.png";
    private static final String THE_DEFAULT_PORTRAIT = MODID + "Resources//images/charSelect/DefaultCharacterPortraitBG.png";
    public static final String THE_DEFAULT_SHOULDER_1 = MODID + "Resources//images/char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = MODID + "Resources//images/char/defaultCharacter/shoulder2.png";
    public static final String THE_DEFAULT_CORPSE = MODID + "Resources//images/char/defaultCharacter/corpse.png";

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = MODID + "Resources//images/Badge.png";

    // Atlas and JSON files for the Animations
    public static final String THE_DEFAULT_SKELETON_ATLAS = MODID + "Resources/images/char/defaultCharacter/skeleton.atlas";
    public static final String THE_DEFAULT_SKELETON_JSON = MODID + "Resources/images/char/defaultCharacter/skeleton.json";

    // =============== MAKE IMAGE PATHS =================

    public static String makeCardPath(String resourcePath)
    {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath)
    {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath)
    {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath)
    {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath)
    {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath)
    {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    // =============== /MAKE IMAGE PATHS/ =================

    // =============== /INPUT TEXTURE LOCATION/ =================


    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================

    public DefaultMod()
    {
        logger.info("Subscribe to BaseMod hooks");
        BaseMod.subscribe(this);

      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */

        setModID(MODID);
        // cool
        // TODO: NOW READ THIS!!!!!!!!!!!!!!!:

        // 1. Go to your resources folder in the project panel, and refactor> rename theDefaultResources to
        // yourModIDResources.

        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project) and press alt+c (or mark the match case option)
        // replace all instances of theDefault with yourModID, and all instances of thedefault with yourmodid (the same but all lowercase).
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        // It's important that the mod ID prefix for keywords used in the cards descriptions is lowercase!

        // 3. Scroll down (or search for "ADD CARDS") till you reach the ADD CARDS section, and follow the TODO instructions

        // 4. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than theDefault. They get loaded before getID is a thing.

        logger.info("Done subscribing");

        logger.info("Creating the color " + TheDefault.Enums.COLOR_GRAY.toString());

        BaseMod.addColor(TheDefault.Enums.COLOR_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);

        logger.info("Done creating the color");


        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theDefaultDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE"); // This is the default setting. It's actually set...
        try
        {
            SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        logger.info("Done adding mod settings");

    }

    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP
    public static void setModID(String ID)
    { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = DefaultMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID))
        { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID))
        { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else
        { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO

    public static String getModID()
    { // NO
        return modID; // DOUBLE NO
    } // NU-UH

    private static void pathCheck()
    { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = DefaultMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = DefaultMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID))
        { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID()))
            { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists())
            { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO

    // ====== YOU CAN EDIT AGAIN ======

    public static void initialize()
    {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        DefaultMod defaultmod = new DefaultMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");

    }

    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================


    // =============== LOAD THE CHARACTER =================

    @Override
    public void receiveEditCharacters()
    {
        logger.info("Beginning to edit characters. " + "Add " + TheDefault.Enums.THE_DEFAULT.toString());

        BaseMod.addCharacter(new TheDefault("the Default", TheDefault.Enums.THE_DEFAULT),
                THE_DEFAULT_BUTTON, THE_DEFAULT_PORTRAIT, TheDefault.Enums.THE_DEFAULT);

        receiveEditPotions();
        logger.info("Added " + TheDefault.Enums.THE_DEFAULT.toString());
    }

    // =============== /LOAD THE CHARACTER/ =================


    // =============== POST-INITIALIZE =================

    @Override
    public void receivePostInitialize()
    {
        logger.info("Loading badge image and mod options");

        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) ->
                {
                }, // thing??????? idk
                (button) ->
                { // The actual button:

                    enablePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try
                    {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings);
                        config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                        config.save();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });

        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        // =============== EVENTS =================
        // https://github.com/daviscook477/BaseMod/wiki/Custom-Events

        // You can add the event like so:
        // BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);
        // Then, this event will be exclusive to the City (act 2), and will show up for all characters.
        // If you want an event that's present at any part of the game, simply don't include the dungeon ID

        // If you want to have more specific event spawning (e.g. character-specific or so)
        // deffo take a look at that basemod wiki link as well, as it explains things very in-depth
        // btw if you don't provide event type, normal is assumed by default

        // Create a new event builder
        // Since this is a builder these method calls (outside of create()) can be skipped/added as necessary
        AddEventParams eventParams = new AddEventParams.Builder(IdentityCrisisEvent.ID, IdentityCrisisEvent.class) // for this specific event
                .dungeonID(TheCity.ID) // The dungeon (act) this event will appear in
                .playerClass(TheDefault.Enums.THE_DEFAULT) // Character specific event
                .create();

        // Add the event
        BaseMod.addEvent(eventParams);

        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
        ConsoleCommand.addCommand("test", CommandTest.class);


    }

    // =============== / POST-INITIALIZE/ =================

    // ================ ADD POTIONS ===================

    public void receiveEditPotions()
    {
        logger.info("Beginning to edit potions");

        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_DEFAULT".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, TheDefault.Enums.THE_DEFAULT);

        logger.info("Done editing potions");
    }

    // ================ /ADD POTIONS/ ===================


    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics()
    {
        logger.info("Adding relics");

        // Take a look at https://github.com/daviscook477/BaseMod/wiki/AutoAdd
        // as well as
        // https://github.com/kiooeht/Bard/blob/e023c4089cc347c60331c78c6415f489d19b6eb9/src/main/java/com/evacipated/cardcrawl/mod/bard/BardMod.java#L319
        // for reference as to how to turn this into an "Auto-Add" rather than having to list every relic individually.
        // Of note is that the bard mod uses it's own custom relic class (not dissimilar to our AbstractDefaultCard class for cards) that adds the 'color' field,
        // in order to automatically differentiate which pool to add the relic too.

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new PlaceholderRelic(), TheDefault.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new BottledPlaceholderRelic(), TheDefault.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new DefaultClickableRelic(), TheDefault.Enums.COLOR_GRAY);

        // This adds a relic to the Shared pool. Every character can find this relic.
        BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);

        // Mark relics as seen - makes it visible in the compendium immediately
        // If you don't have this it won't be visible in the compendium until you see them in game
        // (the others are all starters so they're marked as seen in the character file)
        UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID);
        logger.info("Done adding relics!");
    }

    // ================ /ADD RELICS/ ===================


    // ================ ADD CARDS ===================

    @Override
    public void receiveEditCards()
    {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variables");
        // Add the Custom Dynamic variables
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());
        BaseMod.addDynamicVariable(new DPE());
        BaseMod.addDynamicVariable(new DVAMP());
        BaseMod.addDynamicVariable(new DPAP());
        BaseMod.addDynamicVariable(new DPSH());
        BaseMod.addDynamicVariable(new DTP());
        BaseMod.addDynamicVariable(new XDPE());
        BaseMod.addDynamicVariable(new XDPAP());
        BaseMod.addDynamicVariable(new XDPSH());

        // BaseMod.addCard(new Suika());

        logger.info("Adding cards");
        // Add the cards
        // Don't delete these default cards yet. You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        // This method automatically adds any cards so you don't have to manually load them 1 by 1
        // For more specific info, including how to exclude cards from being added:
        // https://github.com/daviscook477/BaseMod/wiki/AutoAdd

        // The ID for this function isn't actually your modid as used for prefixes/by the getModID() method.
        // It's the mod id you give MTS in ModTheSpire.json - by default your artifact ID in your pom.xml

        //TODO: Rename the "DefaultMod" with the modid in your ModTheSpire.json file
        //TODO: The artifact mentioned in ModTheSpire.json is the artifactId in pom.xml you should've edited earlier
        new AutoAdd(MODID) // ${project.artifactId}
                .packageFilter(AbstractDefaultCard.class) // filters to any class in the same package as AbstractDefaultCard, nested packages included
                .setDefaultSeen(true)
                .cards();

        // .setDefaultSeen(true) unlocks the cards
        // This is so that they are all "seen" in the library,
        // for people who like to look at the card list before playing your mod

        logger.info("Done adding cards!");
    }

    // ================ /ADD CARDS/ ===================


    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings()
    {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Power-Strings.json");

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Relic-Strings.json");

        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Event-Strings.json");

        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Potion-Strings.json");

        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Character-Strings.json");

        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Orb-Strings.json");

        logger.info("Done edittting strings");
    }

    // ================ /LOAD THE TEXT/ ===================

    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords()
    {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword

        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/DefaultMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null)
        {
            for (Keyword keyword : keywords)
            {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }

    // ================ /LOAD THE KEYWORDS/ ===================    

    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText)
    {
        return getModID() + ":" + idText;
    }

    Map<String, Integer> usedCardCounter = new HashMap<>();
    ArrayList<String> usedCards = new ArrayList<>();
    ArrayList<String> usedCardsTotal = new ArrayList<>();

    Map<String, Integer> powerStackCounter = new HashMap<>();
    ArrayList<String> powerList = new ArrayList<>();
    int dmgDealtPerTurn;
    int totalDmg;
    AbstractRoom currRoom;
    int test;

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) //IT WORKS
    {
        // String className= abstractCard.getClass().getSuperclass().getSuperclass().getSimpleName();
        usedCards.add(abstractCard.name);
        usedCardsTotal.add(abstractCard.name);
 /*       if(className.equals("AbstractDefaultCard")){
            AbstractDefaultCard a = (AbstractDefaultCard) abstractCard;
            test += a.damage+a.+a.dmgPerEnergy* EnergyPanel.totalCount;
        }*/
    }

    int turn;
    int healthPreDmg = 0;
    Map<AbstractMonster, Integer> monsterHealth = new HashMap<>();
    Map<AbstractMonster, Integer> monsterBlock = new HashMap<>();

    int dmgReceivedPerTurn;
    int dmgReceivedTotal;
    int blockPerTurn;
    int totalBlock;
    int monsterBlockGain;
    Boolean blockSaved = false;

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo)
    {

        dmgReceivedPerTurn += i;
        dmgReceivedTotal += i;
        return i;
    }

    Map<AbstractMonster, Integer> monsterHpPreHealing = new HashMap<>();

    @Override
    public boolean receivePreMonsterTurn(AbstractMonster abstractMonster)
    {
        if (!blockSaved)
        {
            blockPerTurn = AbstractDungeon.player.currentBlock;
            blockSaved = true;
        }
        monsterHpPreHealing.put(abstractMonster, abstractMonster.currentHealth);
        return true;
    }

    Map<AbstractMonster, Integer> monsterHealing = new HashMap<>();


    void dmgCalculation()
    {
        int dmgCal;
        int healing;

        for (AbstractMonster x : AbstractDungeon.getCurrRoom().monsters.monsters)
        {
            monsterHealing.put(x, 0);
            if(!monsterHealth.containsKey(x)){
                monsterHealth.put(x,x.maxHealth);
                monsterHpPreHealing.put(x, x.currentHealth);
            }
            dmgCal = monsterHealth.get(x) - monsterHpPreHealing.get(x);
            System.out.println("Prehealth " + monsterHealth.get(x));

            if (monsterHpPreHealing.get(x) >= x.currentHealth)
            {
                if (x.lastDamageTaken > 0)
                    dmgDealtPerTurn += dmgCal;
                x.lastDamageTaken = 0;
            } else
            {
                healing = x.currentHealth - monsterHpPreHealing.get(x);
                monsterHealing.put(x, healing);
                if (x.lastDamageTaken > 0)
                {
                    if (x.currentHealth <= monsterHpPreHealing.get(x))
                    {
                        dmgDealtPerTurn += dmgCal + healing;
                    } else
                        dmgDealtPerTurn += dmgCal;
                }
                x.lastDamageTaken = 0;
            }
            healthPreDmg = x.currentHealth;
            monsterHealth.put(x, healthPreDmg);
            System.out.println("this turn: " + x.currentHealth);
        }
        totalDmg += dmgDealtPerTurn;
        totalBlock += blockPerTurn;
    }

    @Override
    public void receiveOnPlayerTurnStart()
    {
        TurnUpdate();
        dmgDealtPerTurn = 0;
        dmgReceivedPerTurn = 0;
        blockPerTurn = 0;
        blockSaved = false;
        monsterBlockSaved = false;

    }


    void TurnUpdate()
    {

        dmgCalculation();
        playerHP = AbstractDungeon.player.currentHealth;
        for (AbstractPower power : AbstractDungeon.player.powers)
        {
            //powerList.add(power.name);
            powerStackCounter.put(power.name, power.amount);
        }

        usedCardCounter = hashMapAdder(usedCards);
        cardDrawCounter = hashMapAdder(drawnCards);
        exhDrawCounter = hashMapAdder(exhCards);
        potionCounter = hashMapAdder(usedPotions);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("BattleData.txt", true)))
        {
            if (turn > 0)
            {
                bw.write("TURN " + turn + " RESULT:");
                bw.newLine();
                bw.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                bw.newLine();
                bw.write("\t PLAYER & MONSTER INFO ");
                bw.newLine();
                bw.newLine();
                bw.write("Player's HP: " + playerHP);
                bw.newLine();
                bw.write("Potion: ");
                for (AbstractPotion x : AbstractDungeon.player.potions)
                {
                    if (x.name.equals("Potion Slot"))
                        bw.write("[Empty] ");
                    else
                        bw.write("[" + x.name + "] ");
                }
                bw.newLine();
                for (AbstractMonster x : AbstractDungeon.getCurrRoom().monsters.monsters)
                {
                    if(monsterHealing.get(x)>=1)
                    bw.write(" |" + x.name + "(HP: " + x.currentHealth+ ") {Healed: "+ monsterHealing.get(x) +"}|"); // {Block Gained: " + monsterBlock.get(x) + "}
                    else
                        bw.write(" |" + x.name + "(HP: " + x.currentHealth+ ")|");
                }
                bw.newLine();
                bw.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                bw.newLine();

                bw.write("          CURRENT POWER ");
                bw.newLine();
                bw.newLine();
                for (String key : powerStackCounter.keySet())
                {
                    bw.write("Power: [" + key + "] | Stack: " + powerStackCounter.get(key));
                    bw.newLine();
                }
                bw.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                bw.newLine();

                bw.write("          PLAYER'S STATS ");
                bw.newLine();
                bw.newLine();
                bw.write("Unblocked Damage Dealt: " + dmgDealtPerTurn);
                bw.newLine();
                bw.write("Block Gained: " + blockPerTurn);
                bw.newLine();
                bw.write("Damage Received: " + dmgReceivedPerTurn);
                bw.newLine();
                bw.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                bw.newLine();
                //region used cards counter
                bw.write("          Used Cards ");
                bw.newLine();
                bw.newLine();
                for (String key : usedCardCounter.keySet())
                {
                    if (usedCardCounter.get(key) < 2)
                    {
                        bw.write("Card [" + key + "] used: " + usedCardCounter.get(key) + " Time");
                        bw.newLine();
                    } else
                    {
                        bw.write("Card [" + key + "] used: " + usedCardCounter.get(key) + " Times");
                        bw.newLine();
                    }
                }
                //endregion
                bw.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                bw.newLine();
                //region card draw counter
                bw.write("          Drawn Cards ");
                bw.newLine();
                bw.newLine();
                for (String key : cardDrawCounter.keySet())
                {
                    if (cardDrawCounter.get(key) < 2)
                    {
                        bw.write("Card [" + key + "] drawn: " + cardDrawCounter.get(key) + " Time");
                        bw.newLine();
                    } else
                    {
                        bw.write("Card [" + key + "] drawn: " + cardDrawCounter.get(key) + " Times");
                        bw.newLine();
                    }
                }
                //endregion
                bw.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                bw.newLine();
                //region card exh counter
                bw.write("          Exhausted Cards ");
                bw.newLine();
                bw.newLine();
                for (String key : exhDrawCounter.keySet())
                {
                    if (exhDrawCounter.get(key) < 2)
                    {
                        bw.write("Card [" + key + "] exhausted: " + exhDrawCounter.get(key) + " Time");
                        bw.newLine();
                    } else
                    {
                        bw.write("Card [" + key + "] exhausted: " + exhDrawCounter.get(key) + " Times");
                        bw.newLine();
                    }
                }
                //endregion
                bw.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                bw.newLine();
                //region potion counter
                bw.write("          Potion Used ");
                bw.newLine();
                bw.newLine();
                for (String key : potionCounter.keySet())
                {
                    if (potionCounter.get(key) < 2)
                    {
                        bw.write("Potion [" + key + "] used: " + potionCounter.get(key) + " Time");
                        bw.newLine();
                    } else
                    {
                        bw.write("Potion [" + key + "] used: " + potionCounter.get(key) + " Times");
                        bw.newLine();
                    }
                }
                //endregion
                bw.write("----------------------------------------------------------");
                bw.newLine();
                bw.newLine();

                //region list clear
                usedCards.clear();
                usedCardCounter.clear();
                drawnCards.clear();
                cardDrawCounter.clear();
                exhCards.clear();
                exhDrawCounter.clear();
                usedPotions.clear();
                potionCounter.clear();
                powerStackCounter.clear();
                //endregion

            }
            turn++;

        } catch (IOException exp)
        {
            exp.printStackTrace();
        }
    }

    HashMap<String, Integer> hashMapAdder(ArrayList<String> list)
    {
        ArrayList<String> temp = list;
        HashMap<String, Integer> tempHash = new HashMap<String, Integer>();
        for (String x : temp)
        {
            if (!tempHash.containsKey(x))
            {
                tempHash.put(x, 1);
            } else
            {
                tempHash.put(x, tempHash.get(x) + 1);
            }
        }
        return tempHash;
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom)
    {
        TurnUpdate();
        turn = 0;
        usedCardCounter = hashMapAdder(usedCardsTotal);
        cardDrawCounter = hashMapAdder(drawnCardsTotal);
        exhDrawCounter = hashMapAdder(exhCardsTotal);
        potionCounter = hashMapAdder(usedPotionsTotal);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("BattleData.txt", true)))
        {
            bw.write("          BATTLE SUMMARY: ");
            bw.newLine();
            bw.newLine();
            bw.write("Total Unblocked Damage Dealt: " + totalDmg);
            bw.newLine();
            bw.write("Total Block Gained: " + totalBlock);
            bw.newLine();
            bw.write("Total Damage Received: " + dmgReceivedTotal);
            bw.newLine();
            bw.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            bw.newLine();
            //region used cards
            bw.write("        Cards Used Total ");
            bw.newLine();
            for (String key : usedCardCounter.keySet())
            {
                if (usedCardCounter.get(key) < 2)
                {
                    bw.write("Card [" + key + "] used: " + usedCardCounter.get(key) + " Time");
                    bw.newLine();
                } else
                {
                    bw.write("Card [" + key + "] used: " + usedCardCounter.get(key) + " Times");
                    bw.newLine();
                }
            }
            //endregion
            bw.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            bw.newLine();
            //region card draw counter
            bw.write("        Cards Drawn Total ");
            bw.newLine();
            bw.newLine();
            for (String key : cardDrawCounter.keySet())
            {
                if (cardDrawCounter.get(key) < 2)
                {
                    bw.write("Card [" + key + "] drawn: " + cardDrawCounter.get(key) + " Time");
                    bw.newLine();
                } else
                {
                    bw.write("Card [" + key + "] drawn: " + cardDrawCounter.get(key) + " Times");
                    bw.newLine();
                }
            }
            //endregion
            bw.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            bw.newLine();
            //region card exh counter
            bw.write("      Exhausted Cards Total ");
            bw.newLine();
            bw.newLine();
            for (String key : exhDrawCounter.keySet())
            {
                if (exhDrawCounter.get(key) < 2)
                {
                    bw.write("Card [" + key + "] exhausted: " + exhDrawCounter.get(key) + " Time");
                    bw.newLine();
                } else
                {
                    bw.write("Card [" + key + "] exhausted: " + exhDrawCounter.get(key) + " Times");
                    bw.newLine();
                }
            }
            //endregion
            bw.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            bw.newLine();
            //region potion counter
            bw.write("        Potion Used Total");
            bw.newLine();
            bw.newLine();
            for (String key : potionCounter.keySet())
            {
                if (potionCounter.get(key) < 2)
                {
                    bw.write("Potion [" + key + "] used: " + potionCounter.get(key) + " Time");
                    bw.newLine();
                } else
                {
                    bw.write("Potion [" + key + "] used: " + potionCounter.get(key) + " Times");
                    bw.newLine();
                }
            }
            //endregion
            bw.write("+++++++++++++++++++++++++++++++++++++++++++++++");
            bw.newLine();
            usedCardsTotal.clear();
            drawnCardsTotal.clear();
            exhCardsTotal.clear();
            usedPotionsTotal.clear();
            powerStackCounter.clear();
            dmgDealtPerTurn = 0;
            dmgReceivedPerTurn = 0;
            blockPerTurn = 0;
            blockSaved = false;
        } catch (IOException exp)
        {
            exp.printStackTrace();
        }
    }

    int act;

    @Override
    public void receiveStartAct()
    {
        act = AbstractDungeon.actNum;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("BattleData.txt", true)))
        {
            bw.write("***********************************");
            bw.newLine();
            bw.write("Act: " + act);
            bw.newLine();
        } catch (IOException exp)
        {
            exp.printStackTrace();
        }
    }

    int BattleNum = 1;
    int playerHP;
    int lastRoundBlock;

    int firstTurnHp;

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom)
    {
        monsterBlockSaved = false;
        monsterHealth.clear();
        monsterBlock.clear();
        monsterHealing.clear();
        monsterHpPreHealing.clear();
        firstTurnHp = AbstractDungeon.player.currentHealth;
        dmgDealtPerTurn = 0;
        totalDmg = 0;
        dmgReceivedPerTurn = 0;
        dmgReceivedTotal = 0;
        totalBlock = 0;
        blockPerTurn = 0;
        turn = 0;//?
        currRoom = abstractRoom;
        for (AbstractMonster x : abstractRoom.monsters.monsters)
        {
            healthPreDmg = x.maxHealth;
            lastRoundBlock = x.currentBlock;
            monsterHealth.put(x, healthPreDmg);
            monsterBlock.put(x, lastRoundBlock);
            monsterHealing.put(x, 0);
            monsterHpPreHealing.put(x, x.currentHealth);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("BattleData.txt", true)))
        {
            bw.write("______________________________________________________________________");
            bw.newLine();
            bw.write("Battle: " + BattleNum++);
            bw.newLine();
            bw.write("Player's HP: " + firstTurnHp);
            bw.newLine();
            bw.write("Potion: ");
            for (AbstractPotion x : AbstractDungeon.player.potions)
            {
                if (x.name.equals("Potion Slot"))
                    bw.write("[Empty] ");
                else
                    bw.write("[" + x.name + "] ");
            }
            bw.newLine();
            bw.write("Fighting:");
            for (AbstractMonster x : abstractRoom.monsters.monsters)
            {
               /* if(monsterBlock.get(x)>0)
                bw.write(" |" + x.name + "(HP: " + x.currentHealth + ")"+ "{Block: " + x.currentBlock +"}|");
                else*/
                    bw.write(" |" + x.name + "(HP: " + x.currentHealth + ")|");
            }
            bw.newLine();
            bw.newLine();

        } catch (IOException exp)
        {
            exp.printStackTrace();
        }
    }


    Map<String, Integer> cardDrawCounter = new HashMap<>();
    ArrayList<String> drawnCards = new ArrayList<>();
    ArrayList<String> drawnCardsTotal = new ArrayList<>();

    Boolean monsterBlockSaved;

    @Override
    public void receivePostDraw(AbstractCard abstractCard)
    {
        drawnCards.add(abstractCard.name);
        drawnCardsTotal.add(abstractCard.name);
        /*if (!monsterBlockSaved)
        {
            for (AbstractMonster x : AbstractDungeon.getCurrRoom().monsters.monsters)
            {
                monsterBlock.put(x, x.currentBlock);
                monsterBlockSaved = true;
                x.

            }
        }*/
    }

    Map<String, Integer> exhDrawCounter = new HashMap<>();
    ArrayList<String> exhCards = new ArrayList<>();
    ArrayList<String> exhCardsTotal = new ArrayList<>();

    @Override
    public void receivePostExhaust(AbstractCard abstractCard)
    {
        exhCards.add(abstractCard.name);
        exhCardsTotal.add(abstractCard.name);
    }

    Map<String, Integer> potionCounter = new HashMap<>();
    ArrayList<String> usedPotions = new ArrayList<>();
    ArrayList<String> usedPotionsTotal = new ArrayList<>();

    @Override
    public void receivePostPotionUse(AbstractPotion abstractPotion)
    {
        usedPotions.add(abstractPotion.name);
        usedPotionsTotal.add(abstractPotion.name);
    }

}
