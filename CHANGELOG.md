## [2.7.0]

### Features
- Haunted dimensions are significantly refactored; `notes` dimension is populated with features and opened to the public.
  - "Notes" block is removed as the new implementation does not use it.
- Custom music discs (dropped from chaos creepers when killed by skeletons):
  - are backported to 1.20;
  - empty ones from the creative inventory now have a tooltip and can be burned to a random song by right-clicking them;
  - are integrated with 1.21.8's music toasts.
- Footprints are given a use: throwing them in a portal creates a special exit-only portal that counts as a valid destination for returning from any dimension - helpful if the original portal you went through gets destroyed or overwritten by another player.
- The /warp command now supports a target argument, allowing to warp another player or several at once. (Cass)

### Technical
- The mod now makes use of data generation, improving consistency between versions.
- Block entity data storage is refactored in a way to make multiversioned coding easier. A side effect of this is that if you are updating a 1.21.8 world to this version, all preexisting portals will break; sorry! (this only affects 1.21.8, other versions can easily autoupdate to the new format)
- Amendments can now use excluding mod selectors, i.e. "!minecraft" to affect everything except vanilla minecraft content.
- Several more user-suggested amendments were ratified, fixing rare crashes with the Bumblezone, Iron Spells, Cobblemon and a few other mods. If you have custom amendments passed, back them up before updating - default amendment additions overwrite the file.

### Fixes
- Random dimensions can once again use fluids other than water for their generation.
- Music discs on 1.21.8 are now correctly tinted and actually play.
- Models of altar items now display correctly in 1.21.8.
- Chaos creepers no longer cause the world to close when their biome is bottled.
- Corals no longer crash world generation. (Cass)
- Custom packets are correctly registered on the client. (Cass, 1.21.1 Neoforge)
- A crash on dedicated 1.21.8 servers was fixed. (Cass)
- 1.21.8's new music toasts are added to the mod's music. (Cass)
- Iridescence shaders and spawn egg textures were sometimes displayed as missing on 1.21.8, Cass fised it.
- The 11th amendment was changed to blacklist bumblezone biomes instead of structures.

## [2.6.5 - 25/I/2026]
A crucial crash exclusive to Fabric 1.21.8 is fixed which was triggering a ConcurrentModificationException on every portal activation.

## [2.6.4 - 25/I/2026]
### Fixed
- Create train tracks now respect offsets from the center of the portal; this makes it possible to route two tracks through the same portal. (Lera)
- Chests in dungeons in random dimensions were missing their contents, now they aren't. (Lera)
- Beacon structure from `missing` now generates correctly. (Cassian)
- F4 config screen was bugged on 1.21.8, it is fixed. (Cassian)
- A crash with a "tinted leaves" particle is fixed. (1.21.8, Cassian)
- Chromatic potion brewing recipe is viewable in RRV. (1.21.8, Cassian)

### Technical
- Config file structure is reworked in the jar to make differences between Minecraft versions easier to handle, this should not have any effect on anything user-facing but i'll include this change here just in case it spawns some bug.

## [2.6.3 - 24/I/2026]
### Fixed:
- Iridescence fluid rendering (in world and in EMI) (Forge 1.20)
- Iridescence shaders (1.20, 1.21.8)
- Collision crafting recipes not working (1.20)
- Iridescent potion incorrectly displaying the effect level given

## [2.6.2 - 21/I/2026]
### Fixed:
- Unable to connect to NeoForge dedicated server.
- Crash on Forge dedicated server.
- Added back Chaos Pawns' spawn eggs to the creative inventory.
- Crash generating surface patches (1.21.8)
- Crash generating biomes with music (1.21.8)
- Incorrect tinted mob rendering (1.21.8)
- Replaced EIV integration with RRV, an updated fork with major improvements. (1.21.8)
- Fixed chaos mobs' rendering (1.21.8)

## [2.6.1 - 12/I/2026]
### Fixed:
- Star and sky rendering on 1.21.8. (Lera)
- World shaders now work on 1.21.8 (Lera)
- Loading root chances, which caused too many vanilla biomes in infinite dimensions. (Lera)
- Sunset shading was misaligned on custom skies. (Lera)
- Crash from `alphatree` on NeoForge. (Lera)
- Iridesence tinting in EIV. (Cassian)
- Crash rendering Chaos Pawns. (Cassian)
- Crash on dedicated NeoForge servers. (Cassian)

## [2.6.0 - 31/XII/2025]
### Added:
- Backported all content from Infinite Dimensions v2.5 to 1.20.1. Primarily, this includes:
    - A new amendments system, which allows for easy handling of modular config files - changing weights of any entries in any randomization pools or eliminating them altogether, based on a robust system of selectors. Default amendments bundled with the mod resolve some major earlier incompatibilities (aether, alex's caves, oritech) and it is recommended to have [Mod Menu](https://modrinth.com/mod/modmenu) and [Cloth Config](https://modrinth.com/mod/cloth-config) installed if you want to add more of them.
    - Random lakes are back, ocean vegetation is no longer hardcoded, fossils and Bonus Chests are introduced (rarity of both under review).
    - A `/warp random` command that does what you'd expect (good for datapacks that randomly warp you every `n` minutes)
    - A new "safe mode" setting is introduced that disables all block entities from being used in future dimensions for terrain generation - may be useful in heavily modded environments.
    - Every end gateway in random dimensions now teleports to a different point.
    - Coral no longer decays in random dimensions.
- Ported to 1.21.8. This port is in beta, please note the following:
    - This port is experimental and some features may not work as intended, notably custom skies and shaders, including the iridesecence shaders. Fixes for these are planned.
    - Item and entity rendering *should* be working as intended despite major rewrites, please report if you find this to not be the case.
    - Create integration has been modified to support [Create Fly](<https://modrinth.com/mod/create-fly>) and EMI integration has been changed to support [EIV](<https://modrinth.com/mod/eiv>).
    - This version will likely be dropped in the future in favour of 26.1.

### Changed:
- Major internal refactors to allow Infinite Dimensions to build for multiple versions of Minecraft at once.
- Required elements of Forgified Fabric API are now bundled on 1.20.1. All versions now only depend on Architectury API.

### Fixed:
- Infinite Dimensions now gracefully handles mods with invalid block entities instead of allowing Minecraft to crash with from invalid player data.
- Crash from Coral Blocks attempting to generate too early.

## [2.5.2 - 22/VIII/2025]
### Fixed:
- The dimension generator could not correctly randomly choose from preexisting biomes before (always defaulting to plains), this is now fixed.
- Fixed a very rare crash with out-of-bounds values chosen for dust particles.

### Technical:
- Provided support for using custom "easter egg" strings to lead to preexisting dimensions.

## [2.5.1 - 12/VIII/2025]
- Now includes Create 6.0 compat for trains going through infinity portals (on Neoforge).
- Every end gateway in random dimensions now teleports to a different point.
- Coral no longer decays in random dimensions.
- Fixed an "invalid player data" crash triggered by incomplete modded block entities.

## [2.5.0 - 10/VIII/2025]
### Features:
- A new amendments system is implemented, which allows for easy handling of modular config files - changing weights of any entries in any randomisation pools or eliminating them altogether, based on a robust system of selectors. default amendments bundled with the mod resolve some major earlier incompatibilities (aether, alex's caves, oritech) and it is recommended to have mod menu and cloth config installed if you want to add more of them
- There is a config screen for doing that easily, made by Cass :3
- Feature generation in random dimensions is improved a bit:
    - Random lakes are back.
    - Ocean vegetation is no longer hardcoded.
    - Fossils and bonus chests are introduced (rarity of both under review, i may make them more common).
- Cass added a `/warp random` command that does what you'd expect (good for datapacks that randomly warp you every n minutes)
- Cass added a button to delete all datapacks from a world, to salvage corrupted saves.
- A new "safe mode" setting is introduced that disables all block entities from being used in future dimensions for terrain generation - may be useful in heavily modded environments.
- There's a new setting to force all book text to be lowercased before parsing into dimension id; i'm thinking if it should be on by default (i've seen a fair few people miss easter dimensions via typing their names capitalized)

### Tweaks:
- Took another go at multi-biome dimensions, hopefully biome borders should be more common to encounter. iridescence journeys no longer make the player invulnerable by default, but there is now a config setting to turn it back on.
- Iridescence visuals are a bit less saturated.

### Bugfixes:
- Cass fixed crashes with ponder, crashes with dimlib, crashes from modded mobs pathfiding, and iridescence not rendering on neoforge.
- Fixed books with newlines in them corrupting translation tables.

### Technical:
- The transfinite altar block no longer spawns above your head for two ticks to do invocation :D

## [2.4.5 - 30/IV/2025]
### QoL:
- Upgraded the EMI compat plugin: it now shows additional information and better colors for many portal-based recipes.
- Added a recipe to craft chromatic carpets from wool.
- Iridescence can now convert a transfinite key to a randomized one (which before was creative-only) and can tint chromatic matter with its pure hue.
- Lilypads are now placeable on iridescence.

### Bugfixes:
- The Bishop of Lang wasn't equipping his weapon before at the start of the battle, now he will.
- When setting the dimension of F4 with an infinity portal, it used to delete its stored charge - this is fixed.
- Optimised gravity calculations.
- With FTB Essentials or Fabric Essentials present, our /warp command conflicted with theirs. Cass resolved this :D

## [2.4.4 - 6/III/2025]
### Bugfixes:
- There was a server crash with the iridescent effect, i fixed it!
- The mod's ComputerCraft compat crashed with outdated versions of CC, i fixed that too.

### Technical:
- The server is now saving what book text was used to generate every new dimension in a new "translation tables" json file.

## [2.4.3 - 5/III/2025]
### Changes:
- Greatly improved upon the iridescence shader: it is now tailored to the level and phase of the iridescent experience the player is having, and all transitions are smooth.
- Seeds for terrain generation in the mod's dimensions now vary (they used to all use the same seed as the overworld, which led to similar terrain shape patterns across dimensions).
- Forgified Fabric API is no longer required.

### Bugfixes:
- The mod now fully works on dedicated Neoforge servers!!
- The mod's dimensions no longer leave trailing data hooks in the level.dat file (which these hooks were bricking the world in certain circumstances after deleting dimensions);
- Accidental contacts with iridescence are now correctly giving the lowest level of the effect; i thought i did that in 2.4.0, but i forgot ><
- The reset charges work once again (they broke somewhere in 2.4's development);
- Cass made chromatic wool flammable and I made it (hopefully) more consistent in rendering its color in multiplayer;
- Some easter dimensions had lower parts of the sky blackened due to the game incorrectly invoking "void fog" in them - this is now fixed.

### Easter Egg Dimensions:
- Reworked and enabled `isolation` and `custom` :D

### Technical:
- The file format for Easter Egg dimensions is changed: additional data i.e. dimension options is no longer dangling in the vanilla-formatted dimension generation json, there's a layer between them. All existing easter dimensions are updated to that format, but if you have your own custom ones, you'll have to update them.
- Also introduced the "aliases" to the format, letting one easter dimension to be accessible from multiple book words.

## [2.4.2 - 4/II/2025]
- Cass fixed a crash on launch on multiplayer servers.
- "Sliderspam" fix (bugfix for certain modded entities i.e. aether bosses spawning with no regard to mobcap) now works on Neoforge.

## [2.4.1 - 24/I/2025]
- The mod now works on NeoForge again.
- There was a safety net system in place before that checked if particular config files correspond to a loaded mod, and if they don't, it ignored them. This system failed with some mods that added content under a mod ID different from their own; as a temporary fix, i added a toggle for globally disabling this system as a config option gamerule.

## [2.4.0 - 24/I/2025]
Lera presents: InfDim 2.4 the Chromatic Antics update :D

### New content:
- Chromatic Wool (and its respective Carpet): new blocks that can be gotten by throwing wool into a portal. Can be dyed any color you want! The color starts as the portal's color but can be adjusted with an array of different items (consult EMI).
  - Chromatic Matter: a paint tool for Chromatic Wool, can copy and paste colors between different blocks of it.
- The Langton's Ant block now fully works as expected and also has a use in progression: namely, if obtained and placed on a Transfinite Altar, and the altar is then rightclicked, it starts a miniboss battle.
  - The actual miniboss (Bishop of Lang) I still don't consider fully complete, but it has some basic attacks for now :D
  - There's also the Langton's Ant entity, which spawns in the same battle; i'll want to expand on those later a bit. They can walk on water and the big one that spawns in battle is actually rideable!
  - The miniboss drops a Star of Lang, which is a new item intended to serve as a non-consumable crafting catalyst for the mod's midgame recipes.
- Iridescent Star - another new item. It can cycle through colors of some blocks (transfinite altars, chromatic and iridescent wool) and is intended to become a crafting component for endgame tools. Two of those are currently implemented and craftable:
  - the Cosmic Altar, which for now does nothing and
  - the F4, which is fully functional!
- The F4 is a device that can move portals around in a single click. Click on a portal to absorb it, on ground blocks to place a portal, and in the air to open a config menu where the dimensions of placed portals can be adjusted. You can also press F4 while holding it to place portals around you in the air. The destination of resulting portals can be set by throwing F4 into an infinity portal.
  - When used in survival, F4 uses a charge system, keeping track of the amount of obsidian it has stored inside. To charge it, absorb some portals or combine it with obsidian in a crafting grid.
- Cass made a "Notes" Block for an upcoming Notes dimension - it plays random notes when receiving a random tick (potential randomizer for redstone?). Pretty much complete but may receive changes in the future (for better integration).
- Chaos creepers now drop a random music disc when killed by a skeleton. The selection of music, unlike that of regular creepers, encompasses every individual music track in Minecraft, including game and menu music, and automatically generates itself on invocation meaning any music from mods is also on the menu.
- A new effect is added, called "Afterglow", which is given to the player upon succsessful completion of an iridescent journey.
  - Slightly improves the player's movement, attack and mining speed, and slightly reduces gravity. This effect should stack with other bonuses to those stats, including Haste (+1 level to that).
  - The duration is configurable, with the default being 24 minutes. A journey without dimension shifting only gives half the duration.

### Changes:
- The transfinite altar now comes in more colors! 12 colors + gray instead of 6 + gray. For now it's just a cosmetic change, but i'm preparing it for a future integration with the Cosmic Altar (for which the Transfinite would be serving as item pedestals).
- In account with player feedback, Iridescence now only gives you level I of the effect on contact, which does not induce dimension shifting (only the shader effect). To get the full effect from before, you have to collect it in a bottle (right-click on a source block) and actually drink it.
  - These bottles are also obtainable from brewing, which is something I plan to expand upon in later updates.
  - Updated the related advancement accordingly.
- Chaos pawns now drop contents of a random loot table instead of random items; this prevents intentionally unobtainable items, i.e. bedrock and spawn eggs, from dropping from them. This behaviour can be reverted with a new config option.

### Tweaks and QOL:
- Iridescence-related items now have rainbowy names :D
- Removed the inherent position-based dependency of the color of iridescent wool in favor of setting it with an iridescent star.
- Durations of some side effects of iridescence are now configurable.
- Timeout period when deleting a dimension with a reset charge can now be changed or disabled in configs.
- Ender dragons in infinity dimensions now have their mob griefing disabled.

### Bugfixes:
- Transfinite keys obtained from multicolored easter egg portals used to not stack on occasions due to having hidden mismatching color components (not actually used in their rendering). This is now fixed.
- A bug in Mojang's own code is fixed that was sometimes making the game crash from "legacy random source" when bees spawn as part of biome spawning (which never happens in vanilla so no wonder mojang missed it)
- There's a new system in place that takes direct control over mob spawning in the mod's biomes, which should fix:
  - modded mobs refusing to spawn
  - modded mobs spawning in midair
  - modded mobs spawning nonstop and lagging the game like there's no tomorrow.

An Easter dimension with the key "end" is added for testing this, which is set to spawn every mob in the game. If you go there and bad things happen, it means i goofed :D

### Technical:
- The /warp command is reworked a bit, allowing to warp by text, numerical ID (/warp id <number>) and dimension identifier (i.e. minecraft:overworld; /warp existing <identifier>). The old /warp-id command is thus removed.
  - The position-searching logic for /warp is also improved a bit, now it should succeed in finding an open position more often.
- For use in those creeper discs, a new system is added that creates a sound event and a jukebox song for every individual music track in the game, allowing to separate them from other music (i.e. every individual track of C418's gets its own sound event, whereas playing music.game would select a random one of them). Due to how sound IDs work, it is partially done on the client, as such it generates its data on first player connection.

## [2.3.8 - 17/XII/2024]
### Create train integration (1.20):
- Now works on Forge.
- Is hooked up to the syncing of portals.
- When changing a dimension of a portal with rails through it, the game automatically tries to update the rails' destination (and breaks them if it can't).

### Tweaks:
- You can now use creative mode pickblock on infinity portal blocks, giving you the respective key.
- Config options now include ways to set a cap for dimension scale and tweak the length of/disable iridescence side effects (initial phase, cooldown).
- There are now chat error messages for when an infinity portal fails to teleport the player for any reason, intended or unintended.

### Bugfixes (1.20):
- Fixed a fish-related forge crash.
- Fixed incorrect teleportations with portals to deleted dimensions and all the calls to teleport the player to their respawn point.
- Fixed the "invalid player data" message when logging in to a deleted dimension.
- Fixed infinity portals not loading the other side with a chunk ticket when a non-player entity goes through (meaning they now can be used for chunkloaders).
- Iridescence-related shaders now should behave properly when the effect is applied, removed, or the player changes dimension.
- Shaders in general should be more stable (dimension-dependent ones failed to apply before until a resourcepack reload).
- There was an edge case before in which portals didn't resync correctly; this is fixed.

## [2.3.7 - 15/XII/2024]
### Content:
- Backported portal syncing logic (introduced in v2.3.2) to 1.20
- Fish now swim backwards when under the iridescence effect
- Kelp can be placed in iridescence

### Compatibility:
- Cass made infinity portals work with Create's interdimensional train tracks, though for now this only works on Fabric

### Bugfixes:
- 2.3.6 crashed on startup when on a dedicated server... this version shouldn't
- The frog food tag is no longer wrongly replaced

## [2.3.6 - 14/XII/2024]
### Technical:
- Trees are now a part of config generaton, meaning modded trees will now show up in dimensions.
- Infinity portals now have better default value assignment when placed with /setblock.
- Iridesence wool is now flammable.

### Bugfixes for:
- iridescence stopping mobs from pathfinding;
- iridescence not being transparent on neoforge;
- water mobs not being able to breathe in iridescence on neoforge;
- unremapped access widener causing a crash on startup on dedicated servers (cass, forge 1.20);
- random structure generation causing a crash (cass, forge 1.20);
- worldgen being able to choose fluids from some mods (_khm_ modern industrialization _khm_) which have improper in-world behaviour (due to not being supposed to be obtainable as blocks placed in the world at all)

## [2.3.5 - 5/XII/2024]
Added proper EMI integration for collision-based recipes, written by Cass :D

Additionally, there's a LOT of small bugfixes and i want to specifically credit JustImagineIT as he found and brought to my attention most of these bugs:
- Chaos slimes converted in iridescence now properly randomize their color
- Chaos pawns now only trigger advancement criteria if they actually convert
- Chaos pawns now use their underfoot block to determine their color, not the one below it
- Portals to dimensions targeted with a reset charge now can be reopened without needing to be broken and replaced
- CommonIO now correctly writes color data once again (before it made most biome tinting black due to how i made the terralith fix in 2.3.3... ><)
- Radial coordinate density functions no longer error at 0,0
- Nether portals can no longer bring you to infinity portals (when moving between the nether and the overworld)
- Dimensions with changed gravity now scale the fall damage and safe fall distance of entities accordingly

## [2.3.4 - 4/XII/2024]
even more bugfixes:
- fixed a crash on removal of the iridescent effect from entities which don't get it from the liquid naturally due to missing conversion data
- cass fixed a crash in fluid interactions between lava and iridescence
- cass also made iridescence buckets work in dispensers!

## [2.3.3 - 2/XII/2024]
A quick hotfix with some crucial bugfixes!
- Fixed a crash on invocation with Terralith (and other mods that store some files in subfolders) installed.
- Fixed a crash on startup when using a dedicated server.

## [2.3.2 - 1/XII/2024]
### Changes:
- Infinity Portals now have a system in place for automatically linking with each other. This way you won't exit out of a portal you haven't entered from unless you build a new portal or start breaking them. Testing and feedback is appreciated!
  - To allow for that, return portals to the Overworld are now infinity portals as well.

### Bugfixes:
- There was a nasty bug with Java occasionally refusing to halt upon closing the game. I fixed that :D

## [2.3.1 - 1/XII/2024]
### Content:
- New decorative block: iridescent wool
  - Craftable by throwing wool in iridescence when under the effect
  - Has an animated texture rotating through hues
  - Has a corresponding carpet

### Changes:
- Iridescence is now tinted with a noise map instead of periodic circles. (Iridescent wool follows the same pattern!)

### Technical:
- Portal crafting (and the new iridescence crafting) is now defined with json recipes instead of being hardcoded

### Bugfixes:
- When throwing a book into a portal which is already an infinity portal, it will now change color immediately
- Text structures used to occasionally create broken datapacks, now they won't
- The mod now registers spawn requirements in a safer way; before it was conflicting with other mods trying to do the same

## [2.3.0 - 29/XI/2024]
### Content:
- Added biome bottles!
  - Empty ones are obtainable by throwing bottles into an infinity portal
  - Use on chaos creepers to extract the biome out
  - Combine with other bottles of the same biome to increase stored charge
  - Release the biome by placing them on a transfinite altar, then clicking the altar with an empty hand
- Added more advancements relating to recent updates

### Changes:
- Area of biome change that chaos creepers create is now a cylinder instead of a cuboid and spans the full world height (same with bottles)
- Floating text in random dimensions now uses structures for generation instead of features; this makes it longer and never cut off

### Bugfixes:
- Transfinite altars now preserve coloration in item form (technically wasn't a bug but i fixed it anyway)

## [2.2.0 - 28/XI/2024]
From this point on 1.20 backports will be released at the end of middle-version development cycles; this is a start of a new one :D

### Content:
- Crafting recipes are added for Ultimate Content items!
  - Added new items to serve as crafting ingredients: white and black matter
- Reworked the structure generation in random dimensions. Structures are finally part of invocation!
- Added more options to InfinityOptions:
  - some dimensions can now give a player a constant potion effect
  - stars get more options related to their brightness and movement direction
  - there's now an option to not use sky shading on sunrise/sunset
- A Very Fine Item is now edible, and upon consumption teleports you home sweet home
- Updated the iridescent shader! It now has a slight color variation to it ^^

### Techninal:
- The format in which easter egg dimensions store their portal color has improved, allowing to set a checkerboard of colors instead of just one
- Removed unused options from InfinityOptions related to rendering a custom skybox

### Bugfixes:
- The old implementation of sky rendering had quite a few bugs (moons going off orbit, dawn shading incorrectly positioned etc), all that is now fixed as the relevant code is reworked :D

## [2.1.4 - 27/XI/2024]
### Bugfixes:
- mavity desync is fixed properly this time (previous implementation of the fix was fine except it crashed on dedicated servers... oopsie)
- the lunar client compiles java differently which caused a crash due to some of my old coding decisions, i reworked around that

## [2.1.3 - 26/XI/2024]
### Content:
- Some dimensions will subtly mess with the sound system, shifting the pitch of all the sounds played in them :D
- All items dropped by chaos pawns are now stackable

### Bugfixes:
- The iridescent effect didn't work properly when given at level I, this is now fixed
- Occasional mavity desync between the server and the client is fixed

This is also the first version since the 1.21 port (v1.12.0) to be backported to 1.20 (by Cass :D)

## [2.1.2 - 23/XI/2024]
### Content:
- Chaos mob precursors now convert to actual chaos mobs when exposed to iridescence
- Chaos pawns convert from the "chess-like" black/white variants to the random variant; "chess-like" pawns can no longer pathfind through iridescence as they avoid this conversion
- Chaos pawns' universal anger is now color-specific
- The old biome-changing mechanic for chaos creepers is removed in favor of them attuning to a biome they're in on conversion from regular creepers

### Bugfixes:
- Respawning the player alive (e.g. at the end of an iridescent journey) now works on Neoforge without crashing

## [2.1.1 - 21/XI/2024]
Added a status effect associated with iridescence that can send you on a journey through random dimensions :D

## [2.1.0 - 19/XI/2024]
### Content:
- Added the Iridescence fluid, which can be found in some dimensions and is rainbowy and stuff. Creates colored blocks when interacting with lava. Will get more functionality in the future.
- The Ant block can now recolor any colorable block, not just concrete.
- Cass added a toggle in configs to enable easter dimensions from the Ultimate Content mod (they still need work so by default are disabled until a later release).
- Fire tick is now disabled in all of the mod's dimensions.

### Technical:
- Substantial under-the-hood changes in how config files are structured. Status effects now have their own weighed list as well.
- Buffed the config autogenerator: the only thing that isn't included in it now is structures.

### Bugfixes:
- Invocation failed before if the player was far from 0,0 due to a typo; this is now fixed.
- Chaos mobs now once again have spawn eggs.
- The model for the swaggiest slab ever was fixed by Cass.

## [2.0.3 - 1/XI/2024]
### Content:
- Started integration with the Ultimate Content mod. Its easter dimensions are disabled for now, but the Cursor, Ant and Netherite Stairs blocks now all exist in the game and can be found in generated dimensions.
- Worlds with the rainbow sky type in infinity options now have rainbow fog as well

### Bugfixes:
- Modded particles now shouldn't crash the game on dimension generation, and neither should the Random Forest biome
- Transfinite altars can once again be recoloured

## [2.0.2 - 30/X/2024]
the only change from last version is that now it works on neoforge as well

## [2.0.1 - 29/X/2024]
- Portals to easter egg dimensions now have unique coloration
- Fixed the client rendering dimensions with wrong time scale (superfast sun in the overworld bug)
- Fixed hoglins and zoglins being able to spawn in midair and fall to their doom
- Cass fixed a crash when visiting some easter egg dimensions related to a broken access widener

for some reason the neoforge version didn't get bundled, i'll upload a patch later

## [2.0.0 - 27/X/2024]
A loooot of stuff which wouldn't have been possible without Cass, but also some content and quality tweaks!
- There is a new Transfinite Key item, which you can obtain by throwing Amethyst into an Infinity Portal. It is attuned to the portal which was used to create it and can be used to open another portal to the same dimension. More functionality for these is planned ^^
- The warp command now puts you in a safe location, making it viable for use in survival
- Dimension-specific shaders now shouldn't fail to load
- In 1.14 of the mod, the POI of infinity portals broke, making new portals generate every time you return from a dimension; this bug is now fixed
- The rest of format-change-related bugs from the 1.21 update should (hopefully) be fixed
- Forgix is now used, making a single jar usable for both Fabric and Neoforge
- Many optimisations by Cass related to the Fabric-Neoforge logical forks
- Fixes for "registration frozen" errors with other mods installed
- Mod documentation is now availible on the Modded Minecraft Wiki

## [1.14.0 - 13/X/2024]
cassian made a Neoforge port now!!! yay!!! go give them some love they deserve all of it

## [1.13.0 - 8/X/2024]
- ModMenu integration by cassiancc (for the root file)
- Item lore by cassiancc
- Bugfix: long numeric values were written in a way they weren't able to be read from, this is now fixed
- Chaos Slimes only drop their core once now, their children don't inherit it

## [1.12.6 - 4/X/2024]
...fixed yet another problem around changed particle format, hopefully the last

also changed mavity distribution a bit

## [1.12.5 - 3/X/2024]
Config files related to mods not loaded aren't now being read. This prevents crashes when previously loaded mods were removed from a game instance - a situation which before was guaranteed to break the mod.

## [1.12.4 - 3/X/2024]
Continuing bugfixes...
- Fixed a crash when throwing an _empty_ book into a portal
- Mavity works again
- Fixed elder guardians spawning nonstop in some worlds
- Added debug messages into JsonGrabber to hopefully help weed "This registry can't create new holders without value" crashes more effectively

## [1.12.3 - 2/X/2024]
- Continuing with fixing update-prompted bugs: tinting of chaos mobs now works properly once again.
- Chaos mobs have spawn eggs now!
- Reworked when the modular configs are generated (cosmic invocation), pruning unnecessary invocation calls - and removing associated lagspikes. Invocation now only happens once per _game instance_. For manual re-invocation (e.g. when adding new mods to the instance) you can either use the old trusty Transfinite Altar or delete the new `config/infinity/modular/invocation.lock`.

## [1.12.2 - 2/X/2024]
More bugfixes on the way!
- Fixed a crucial bug with some biomes crashing the game: apparently there are new particle types in 1.21 which require additional data
- The mod's storage system expected any IDs of anything not to have file separators in them. This isn't the case for some mods, so the system is now reworked to support them
- World-adding s2c packets split into several packets of smaller size
- ComputerCraft integration by Cassian
- Changes to the Classic dimension

## [1.12.1 - 26/IX/2024]
cass fixed a lot of things for this and thus here's an important update, thanks cass!!

advancements, loot tables, recipes etc. are now working correctly

## [1.12.0 - 26/IX/2024]
Updated the codebase to 1.21.1. Some bugs arisen in the process, so be sure to report it if you see anything strange!

## [1.11.1 - 8/VIII/2024]
- fixed a crucial bug in the config generator preventing creation of modded biome configs
- you can now wash chaos creepers with a water bucket! this resets their biome to a biome they are washed in.
- chaos skeletons are optimised a bit
- there's a new toggle in the config to force all random biomes to only allow full blocks as their surface - thus preventing biomes covered in doors, fences etc.
- there's also an experimental toggle to disable chaos mobs from spawning

## [1.11.0 - 6/VIII/2024]
- you don't need the altar anymore to include modded content in dimension generation! the game does it by itself! wheee!!
- that said, the transfinite altar is still kept as a decoration block
- biomes are now included in auto-detectable modded content, together with their surface rules! that was the most code-heavy part
- you can also configure the maximum number of biomes that can be observed in multinoise / checkerboard worlds. default is 6
- the biomes should hopefully mix a bit better as i restricted their multinoise generation parameters to between -1 and 1 (before it was -2 and 2 - on extreme ends, biomes think they are super rare e.g. mushroom islands and refuse generating :/)
- cassian fixed an annoying f5 bug with custom shaders - thanks to them :3

i believe that from this point on all roundabouts like altars and commands and needing to delete files to update the mod - are gone. now the mod is just playable without needing to do weird stuff even once :D

## [1.10.8 - 24/VI/2024]
- Amount and scale of stars can be now customized.
- Multiple moons can now be present in the sky.
- For each moon, texture, tint, axis and speed of rotation can be configured separately.
- These sky options are now actually used in dimension generation.

## [1.10.7 - 24/VI/2024]
- Fixed a startup crash related to the autoupdater
- Even more sky customization options

## [1.10.6 - 24/VI/2024]
- Added support for custom sky textures.
- Red, green and blue dimensions for shader testing.
- More fixes to spawn requirements: now passive mobs don't check for grass when in modded biomes.
- Naturally spawned sheep in modded biomes can be any color :D
- Added an auto-updater for config files, hopefully this puts a stop to manually purging configs.

## [1.10.5 - 23/VI/2024]
- Fixed chaos mobs lacking spawn restrictions and thus spawning in midair.

Merged pull requests by cassiancc and JPeroutek:
- EMI integration for mod's "throw a thing into a portal" recipes;
- fix for a crash with throwing empty books into portals;
- consistensy between unsigned and signed books;
- portals auto-opening when portal key is empty;
- F5 doesn't get rid of dimensional shaders;
- File.separator in use for config generations instead of hardcoded "/".

## [1.10.4 - 2/IV/2024]
- Added the Reset Charge: an item that disables dimensions on activation by deleting their datapack. The region files are not deleted, so a dimension can be regenerated later (after the server restarts) and all the blocks will still be there. This is meant for use on servers to reduce the amount of unneeded dimensions in a server's memory.
- To activate the charge, place it and shift-right click with an empty hand. A normal rightclick will pick an inactive charge up.
- The charge is completely safe outside of the mod's dimensions and can even be used as a light source.
- Added two new advancements related to the reset charge.

## [1.10.3 - 1/IV/2024]
### Bugfixes:
- Fixed a crash with entities forgetting their getWorld implementation
- Taught the Easterizer to respect the differences between operating systems

## [1.10.2 - 31/III/2024]
- Gravity can now vary between dimensions.
- Added the Classic density function type and the Pyramid structure.

### Bugfixes:
- Attempting to leave an "easter" dimension through a portal now works correctly.
- Removed debug messages that are no longer needed.

## [1.10.1 - 31/III/2024]
- Solar scale, lunar scale and temporal scale are now all customizable.
- Weather in the mod's dimensions now obeys the /weather command.
- Shaders and other custom options for all dimensions are now stored on the disk alongside the rest of the dimension's datapack.

### Bugfixes:
- Chaos slimes now have an appropriate hitbox.
- A weird crash with chaos mobs losing track of if they have a custom name or not is fixed.
- Loot tables for chaos pawns are fixed.
- Easter worlds now can properly be assigned shaders and other custom options (this process was broken before).

## [1.10.0 - 29/III/2024]
- Added the ability to force a shader upon a player depending on the dimension they are in. It is used by some random dimensions for simply changing the colors around (the chance for it is config-governed), but...
- ...there is a way to embed any vanilla-compatible post-processing shader into a custom "easter egg" dimension.
- Also fixed a broken file in the pack that was spoiling the #minecraft:mineable/pickaxe block tag.
- Also reworked the model of a book box so it's a bit less cursed to mine/outline/observe from small angles.
- Also made the chaos mob family respect the power of Jeb.

## [1.9.1 - 28/III/2024]
Minor update dedicated to tweaking surface rules for generated biomes in such a way that prevents generation of top layer blocks in unstable positions.

Also fixes being unable to dye the transfinite altar in purple.

## [1.9.0 - 28/III/2024]
- Added the Transfinite Altar, the block an interaction with which replaces the /generate_configs command.
- Made the generation of configs on the altar be deployed immediately without the need to restart.
- Fixed the book box not dropping its loot table.

## [1.8.4 - 27/III/2024]
- Reworked the naming convention for "easter egg" dimensions: their dimension IDs now use readable text instead of hashed numeric IDs.
- Added five new advancements related to the mod.
- Chaos pawns now spawn in infinity portals much like zombified piglins do in nether portals.
- Chaos pawns are now neutral mobs with group aggression.
- Added a way of obtaining the Box of Infinite Books by throwing a bookshelf into an infinity portal.

## [1.8.3 - 26/III/2024]
- Finally added the box of infinite books and the library dimension to go with it.
- Added two more mobs: the chaos creeper, that sets the surrounding area to a certain biome upon explosion, and the chaos pawn, a very fast mob that drops random items.
- Reworked the easter dimension format: they are now registered automatically without the need for a map.
- Added content created by cassiancc.

## [1.8.2 - 23/III/2024]
Finally I'm starting to add actual new features. Onwards to beta!

This update introduces two new mobs: the Chaotic Slime and Chaotic Skeleton. I have larger plans for them, but for now both of them are exclusive to a dimension of their own to find :D
- The Chaotic Slime has a random color tint to it and a random block in its core, which it drops upon being slain.
- The Chaotic Skeleton is soaked in a random harmful potion and shoots arrows tipped with its effect.

Oh, and I've also updated the translation file.

## [1.8.1 - 23/III/2024]
Merged pull requests from unilock that fix a bug impeding the mod's functionality on servers and make the mod more other-mod-friendly overall via the use of a dimension loading event some mods use that I didn't know existed.

Updated the credits accordingly.

## [1.8.0 - 3/III/2024]
- Added a new noise generator
- Added the Menger and Skygrid custom density function types
- Fixed vanilla trees being unable to generate in dimensions which don't have dirt
- Fixed Infinity portals consuming keys on use even when their dimension is already generated

## [1.8.0 - 3/III/2024]
- Added the /generate_configs command, which auto-generates compatibility packs for all loaded mods to the best of its ability, placing them in the /config/infinity/gen folder.
  - The command currently generates weighed lists for blocks, fluids, mobs, particles, items and sounds.
  - The command has two blockPos arguments. To properly use it, input the first block position to be surrounded by air and the second one one block above a stone block.
  - To use generated files as compatibility packs, for now you should manually drag the contents of /config/infinity/gen over to /config/infinity/modular, merging folders and replacing files. Make sure to reload the world after that for these changes to pass.
- Added the Chaos custom material rule.
- Prevented dimensions from generating structures too frequently (which can be very laggy due to mobs spawning in them).
- Made the `config/infinity/modular/.../extra/structures.json`'s format more mod-compatible.
- Prevented several frequent features from using laggy blocks for generation.

## [1.7.0 - 13/II/2024]
- Added custom material conditions for "easter egg" dimensions
- Added floating pieces of text
- Silenced excessive warning spam
- Fixed a bug with some dimensions trying to find minecraft:air in the fluid registry, failing, and not generating at all

## [1.6.0 - 13/II/2024]
A major update adding:
- the /warp and /warp-id commands
- interface to add "easter egg" dimensions via configs
- several new density function types for usage in these dimensions
- support for having dimension IDs go to the long limit instead of the integer limit
- ability to add salt to book text -> numeric dimension id encoding

and also:
- making structures a bit more common
- removing the seedDependentDimensions gamerule in favor of salt.

Make sure to wipe your config/infinity folder before updating to this one.

## [1.5.5 - 10/II/2024]
- fixed a bug with geodes causing "bound must be greater than origin" error
- fixed a crucial bug in string formatting with floating point numbers, which caused them to be written with a comma instead of a dot, thus entirely preventing the mod from working, on some systems with non-english default java locale

## [1.5.4 - 30/I/2024]
- fixed a crucial bug preventing runtime generation on windows systems
- refactored how heights and sea level are generated to fix the bug when the sea level can become greater than maximum height
- refactored generation of dimension scale

## [1.5.3 - 23/I/2024]
- Portal key can now be empty (set to air) meaning no item is required to open portals (you still need to right-click them with an empty hand though).

This is the first mod version to be released publicly.

## [1.5.2 - 23/I/2024]
- Flowers now generate in dimensions.
- Random infinity portals no longer spawn in the overworld.

## [1.5.1 - 20/I/2024]
- All block-related configs are now read from the same file.
- Compatibility added for fluids whose name and block name are different (i.e. milk from milklib).

## [1.5.0.1 - 18/X/2023]
- Added random infinity portals to dimension generation (though they are disabled in configs by default).
- Added a bunch of hardcoded block palettes to make portals from.

## [1.5.0 - 8/VIII/2023]
- Added random infinity portal structures, generating in the Overworld.
- Structures generated in random dimensions now are added to vanilla structure tags (this helps i.e. eyes of ender navigate to strongholds in random dimensions)

## [1.4.0 - 6/VIII/2023]
- Vanilla structures now spawn in random dimensions.

## [1.3.4 - 6/VIII/2023]
- Biome data is now loaded clientside in multiplayer. This concludes multiplayer compatibility.

## [1.3.3 - 5/VIII/2023]
- Dimension types are now loaded clientside in multiplayer.

## [1.3.2 - 4/VIII/2023]
- Added cherry-like options for random tree generation.

## [1.3.1 - 21/VII/2023]
- Book contents are now processed in a multiplayer-friendly way.

## [1.3.0 - 19/VII/2023]
- Surface rules are refactored again.
- Fixed a bug in multinoise preset selection.

## [1.2.2 - 19/VII/2023]
- Some file-reading code was erroring on windows, this was fixed.

## [1.2.1 - 19/VII/2023]
- Config packs' inner structure is reworked.

## [1.2.0 - 18/VII/2023]
- Crops can now spawn in infinite dimensions.

## [1.1.4 - 18/VII/2023]
- Surface rules are refactored.

## [1.1.3 - 18/VII/2023]
- Nether portals can now be lit in random dimensions.

## [1.1.2 - 18/VII/2023]
- Tree types (trees, huge mushrooms, and huge fungi) now have their distribution configurable.

## [1.1.1 - 18/VII/2023]
- Config filestructure rework.
- Color presets for shapes are now unified.

## [1.1.0 - 18/VII/2023]
- Updated the mod from 1.19.3 to 1.20.

## [1.0.1 - 17/VII/2023]
- Whack noise preset is changed to prevent holes in the bedrock.

## [a0.1 - 17/VII/2023]
### Features:
- Features generating in dimensions now include caves, ores, end spikes, end gateways, ceiling blobs, and shape features (cubes, spheres etc.).
- Added the "whack" noise preset.
- Random portals now play a sound on creation.
- A custom music track by ivorycello is added, to be found in dimensions.

### Tweaks & technical:
- Default config change: all random dimensions are now safe for piglins and support the use of a respawn anchor.
- World seed-dependent dimension generation is added as a config option.
- Config filestructure rework to support namespaced config packs.

### Fixes:
- When teleporting from a random dimension, return portals are now created as infinity portals instead of nether portals.
- Surface rules for nether biomes are fixed.

## [0.0.3 - 19/VI/2023]
### Features:
- Features generating in dimensions now include trees, surface and floating block patches, small end islands, monster rooms, and basalt delta-like column piles.

### Tweaks & technical:
- Block selection for world generation is split into several lists for different purposes.
- The root config file is introduced.

### Fixes:
- Random dimensions are loaded at runtime (with a config option to disable it).
- Noise provider generation is tweaked to prevent holes in bedrock.
- Random dimension depth is capped at y=-48 to prevent lava oceans (hardcoded to appear at y=-55).

## [0.0.2 - 02/II/2023]
- Implemented portal blocks, opening portals with books, and random tinting of portals.
- Portals can be used and teleport correctly, but only after reloading the world (dimensions do not load at runtime).
- Implemented random dimensions, dimension types, and noise routers.
- Implemented random biomes.
- Implemented random selection of vanilla features for random biomes.
- Implemented generation of random features: lakes, deltas, geodes, icebergs, forest rocks, and vegetation (consisting of huge mushrooms and huge fungi), all of them using random blocks.

## [0.0.1 - 26/I/2023]
- Initial commit.


