### Added
- Backported all content from Infinite Dimensions v2.5 to 1.20.1. Primarily, this includes
  - a new amendments system is implemented, which allows for easy handling of modular config files - changing weights of any entries in any randomisation pools or eliminating them altogether, based on a robust system of selectors. default amendments bundled with the mod resolve some major earlier incompatibilities (aether, alex's caves, oritech) and it is recommended to have mod menu and cloth config installed if you want to add more of them
  - random lakes are back, ocean vegetation is no longer hardcoded, fossils and bonus chests are introduced (rarity of both under review, i may make them more common). cass added a /warp random command that does what you'd expect (good for datapacks that randomly warp you every n minutes)
  - a new "safe mode" setting is introduced that disables all block entities from being used in future dimensions for terrain generation - may be useful in heavily modded environments.
  - Every end gateway in random dimensions now teleports to a different point.
  - Coral no longer decays in random dimensions.

### Changed
- Major internal refactors to allow Infinite Dimensions to build for multiple versions of Minecraft at once.

### Fixed
- Infinite Dimensions now gracefully handles mods with invalid block entities instead of allowing Minecraft to crash with from invalid player data.
- Crash from Coral Blocks attempting to generate too early.