### Added
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

### Changed
- Major internal refactors to allow Infinite Dimensions to build for multiple versions of Minecraft at once.
- Required elements of Forgified Fabric API are now bundled on 1.20.1. All versions now only depend on Architectury API.

### Fixed
- Infinite Dimensions now gracefully handles mods with invalid block entities instead of allowing Minecraft to crash with from invalid player data.
- Crash from Coral Blocks attempting to generate too early.