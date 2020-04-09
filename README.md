# WoWSetta
Program to decode every WoW race's language using Pcap as opposite faction.

Perusing through packets aftering having captured them with Wireshark, while playing World of Warcraft, I noticed that the opposite faction's in-game messages are not scrambled until they reach the application layer, making the enemy's thoughts *easy pickings*.  Especially useful on PvP servers.

Application reads through each packet and extracts text, displaying it to the console, before it reaches the WoW client and is scrambled for the game's user.

Has not been used since the "Burning Crusade" expansion pack, but as long as the text is not scrambled before it hits the client, the principle will remain exactly the same.
