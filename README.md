# DropsToInventory

A plugin for Spigot (a Minecraft Server implementation) targeting 1.18 that makes
blocks drops and dead mob drops insert straight into the player's inventory. 

This is a very simplistic implementation that tries to be simple and fast and without any configuration,
commands or permissions (yet). I'd like to fix this at least for some major things but I give no guarantees
that I'll ever get to it.

If you want anything more complex look somewhere else - I stumbled across
"[Drops2Inventory Plus](https://www.spigotmc.org/resources/%E2%AD%90-drop2inventory-plus-%E2%AD%90.87784/)"
on Spigot and it looks decent (though it's paid).

## Usage

Drop the plugin in your `plugins` folder and reload. All drops should go to the player who destroyed the block.
For entity kills we remember the last player who dealt damage and if the entity dies within a few seconds
after that they get the drop. This is to allow for things like fire or mobs finishing off the entity.

We try to put all items in the player's inventory (stacking with existing ones) and anything that doesn't
fit is dropped on the ground like normal.

## Limitations

Due to the way drops work in Spigot it's not trivial to handle "related" drops - like when you break the bottom
block of a sugar cane or cactus, or when you break an item frame or a chest. In cases like these items will drop
on the ground like normal. Some of those would be fairly easy to fix, others harder, but they are all ultimately
edge cases that can't be fixed universally and easily without some major changes in Spigot itself.

(Due to this it's possible some of those will start working in the future on their own.)

Additionally, the "pickups" don't generate pickup events. This means that if there is something that should prevent
a player from picking up items from the ground (but not preventing them from killing stuff or breaking blocks) - 
like a permission plugin or a WorldGuard zone - it will be ignored and the items inserted into their inventory
regardless.

This also means that if you have a logging plugin that logs block pickups they will not be logged.

## To do / wishlist

I'd really like to fix at least the pickup events (perhaps make it configurable) because it's potentially a huge
issue, especially for the logging.

Similarly, I'd like to at least handle a few of the easier and more common edge cases
(like breaking a chest, though it's questionable whether that's actually desirable).

Which means at least basic configuration is a must.

And I'd also like to throw in a command that allows to toggle DTI behavior
to keep parity with the original version of my plugin.

## License

Note that while the code is released under MIT license I'd greatly appreciate it if you let me know
about issues, suggestions or patches you might have before redistributing the plugin yourself.
