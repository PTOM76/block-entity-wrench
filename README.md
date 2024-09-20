# Block Entity Wrench
Add wrench to save block entity data, etc.

## Items
### Wrench
A wrench that allows you to block that has block entity data and rotate blocks.

## Commands
- `/bew76 [config | reload]` : Use this command to manage or reload configuration settings.

## Config
The config file is stored at `config/bew76.yml`, and you can either edit it directly or modify settings via commands.

### Option
- rotate_feature: Enables/disables the block rotation feature. (Default: enabled)
- break_feature: Enables/disables the block breaking feature. (Default: enabled)
- save_block_entity: Enables/disables the feature that saves block entity data. (Default: enabled)
- rotate_entity_feature: Enables the feature to rotate entities. (Experimental feature. Default: enabled)
- blacklist_blocks: Specifies blocks that cannot be broken by the wrench. By default, all types of beds are included.

- You can either modify the settings via commands or directly edit the config/bew76.yml file.

## Dependencies
- [MCPitanLib](https://curseforge.com/minecraft/mc-mods/mcpitanlibarch)
  - [Architectury API](https://curseforge.com/minecraft/mc-mods/architectury-api)


- [Fabric API](https://curseforge.com/minecraft/mc-mods/fabric-api) (Fabric only)

## License
- CC0-1.0