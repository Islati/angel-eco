
About
---
Angel is a simple digital economy plugin for the SpigotMC Platform.


_Plugin Features_
* Simple commands for managing your economy.
* Lightweight & efficient (_Minimal Lag_)

_Codebase Features_
* Annotation based command processor.
* PostgreSQL Database. (No planned support for any other JDBC driver.)
* Simple YML Configuration.
  

Setup
---

_(If you're not using PostgreSQL the included SQL scripts will not execute)_

0. Stop your server if it's running.
1. Move ```Angel.jar``` to your ```plugins``` folder.
2. Start your server & Wait for configuration to generate _(Plugin will disable itself)_
3. Stop server.
4. Navigate to ```plugins``` folder and find ```angel/config.yml```
5. Open ```angel/config.yml``` in your text editor and configure the appropriate values.
6. Start your server & enjoy using Angel!


Notes
---
* Inside the `io.gric.mc.api.commands` package is a collection of classes used to create the intuitive command interface. This component was salvaged from _Commons_ (https://github.com/TechnicalBro/Commons)
* _There is no support for Vault in the initial release (as it's a proof of concept.)_