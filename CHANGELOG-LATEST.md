### Features
- Haunted dimensions are significantly refactored; `notes` dimension is populated with features and opened to the public.
  - "Notes" block is removed as the new implementation does not use it.
- Custom music discs (dropped from chaos creepers when killed by skeletons):
  - are backported to 1.20;
  - empty ones from the creative inventory now have a tooltip and can be burned to a random song by right-clicking them;
  - are integrated with 1.21.8's music toasts.
- Footprints are given a use: throwing them in a portal creates a special exit-only portal that counts as a valid destination for returning from any dimension - helpful if the original portal you went through gets destroyed or overwritten by another player.
- The /warp command now supports a target argument, allowing to warp another player or several at once. (CasS)

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