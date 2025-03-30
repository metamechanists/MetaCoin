# MetaCoin™️ Plugin

## Leaderboard
TODO: FILL THIS OUT (include that multiple people can be ranked at the same position if they have equal coins)

## Placeholders (PAPI)
If you chose to use the MetaCoin™️ leaderboard system, there are some placeholders you can use.
- `%MetaCoin™_position%` - Displays the position of the player in the leaderboard
- `%MetaCoin™_value%` - Displays the amount of coins the player has submitted to the leaderboard
- `%MetaCoin™_#%` - Displays the names of the players at position #
- `%MetaCoin™_#_value%` - Displays the amount of coins the players at position # have submitted to the leaderboard

## Permissions & Commands
- `metacoin.command.deposit` - Gives access to the `/metacoin™ deposit` command, used to deposit any coins in your inventory to the leaderboard
- `metacoin.command.resetleaderboard` - Gives access to the `/metacoin™ resetleaderboard` command, used to reset the leaderboard COMPLETELY
- `metacoin.command.compress` - Gives access to the `/metacoin™ compress` command, used to compress all metacoins in your inventory down
- `metacoin.command.invest` - Gives access to the `/metacoin™ invest` command, which gives the player their ONLY MetaMiner™ (however the reset command below will allow you to use this command again)
- `metacoin.command.reset` - Gives access to the `/metacoin™ reset <player>` command, which resets a players MetaMiner™ data (allowing the acquiring of a new miner)
- `metacoin.command.becomeslag` - Gives access to the `/metacoin™ becomeslag` command, which gives a MachineSlag™ of the targeted placed MetaMiner™
- `metacoin.command.voidwarranty` - Gives access to the `/metacoin™ voidwarranty` command, which voids the warranty of the targeted placed MetaMiner™

- Npcs setup at spawn
    - Name: Indra
      - Idra but in a trench coat
      - has his own full setup
      - gives a player a limited machine: MetaCoin:tm: Miner
      - only gives one per player
    - Name: Tom
      - Is a Genius 
      - Villager NPC
      - Has a setup with tons of placed metacoin
      - Can be conversed with to *eventually* compress metacoin
        - Job for Eden 
        - Cannot skip the dialogue
        - is at minimum 8 sections long
        - talks all about how metacoin is the future of cc

- MetaCoin™️ Miner
  - has owner, upgrade levels, and total coins mined in the lore 
  - Slowly mines for meta coin (like void harvester)
  - requires no energy
  - use noteblock & block state + resourcepack for custom texture OR display model texture
  - explodes after [REDACTED], turns into "machine slag" a magma block **with the final upgrade numbers & total coins mined in the lore** OR if using display model, fun messed up display model
    - "The meta coin miner seems to have overheated slightly"
    - Randomly malfunctions, turning off breakers and must be manually fixed in the control panel (mentions this is *possible* in the lore)
      - makes noise & particles
  - Can be upgraded with coins
      - Speed: how long it takes
          - Upgrading decreases timer per craft & decreases reliability
      - Production: how many it creates
          - Upgrading increases # of coin per craft (by 1) & decreases reliability
      - Reliability: the speed at which breakers turn off in the control panel
          - Upgrading increases reliability

- Aurelium Skills MetaCoin something Artifact
  - meta coins can be applied to apply random souls
  - the more coins applied the better the random soul

- Meta Coin
  - New currency
  - Can be compressed by a *very smart* NPC at spawn (permanent)
    - Amount in a single stack is shown in the lore
  - has custom texture with rp
  - Can be thrown as a projectile, more compressed > more damage
  - Can be placed as a display model
    - you can place multiple in a block and it will stack together
    - send stuff to #alerts in case anything goes wrong

- 2025 TODO
  - auto updater
  - check with sefi this is ok lmao (done)
  - /invest command (done)
  - make sure softdepends are actually soft depends (done)
  - test on local server with only slimefun (done)
  - improve/add messages (they're fine actually lol)
  - check perms are correct (done)
  - check this still works on metamechanists lmao (done)