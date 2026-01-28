### Features
- Haunted dimensions are significantly refactored; `notes` dimension is populated with features and opened to the public.
  - "Notes" block is removed as the new implementation does not use it.
- Custom music discs (dropped from chaos creepers when killed by skeletons):
  - are backported to 1.20;
  - empty ones from the creative inventory now have a tooltip and can be burned to a random song on rightclick.
- Footprints are given a use: throwing them in a portal creates a special exit-only portal that counts as a valid destination for returning from any dimension - helpful if the original portal you went through gets destroyed or overwritten by another player.

### Technical
- The mod now makes use of data generation, improving consistency between versions.
- Payload-related code is compartmentalised, which should not affect anything observable to the user but might spawn unknown bugs.

### Fixes
- Corals no longer crash world generation. (Cass)
- Custom packets are correctly registered on the client. (Cass, 1.21.1 Neoforge)