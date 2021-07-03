# ServerStats
Visualize your Minecraft server statistics in realtime

Want to learn more? Visit [serverstats.diced.me](https://serverstats.diced.me) for the latest downloads and documentation.

## Downloading
There's 4 places you can download ServerStats
* **Bleeding Edge Builds** - GitHub Actions build artifacts
* **Bleeding Edge Builds 2** - `builds` branch
* **Stable Build** - GitHub releases
* **Official Website** - Offers stable & bleeding edge.

## Features
* Easy: setup ServerStats in minutes
* Prometheus: export stats to prometheus
* Grafana: visualize your stats in Grafana using our provided dashboard
* Cross server compatible: works with proxy servers, Fabric and Bukkit compatible servers (Spigot, Paper, etc)

## Compiling
You must be using Java 16+ as Fabric/Minecraft 1.17 requires it, or go into the directory you want and build using a different JDK.
```shell
git clone https://github.com/diced/ServerStats
cd ServerStats
./build.sh # custom build shell script that builds all modules and gives all jars in builds/
```

## Project Layout
ServerStats utilizes mono-repos/modules to have different server types in one repo.

* **Common** - Common modules with interfaces and abstract classes that bukkit/bungee/fabric use
* **Bukkit/Bungee/Fabric** - Uses the common module's interfaces to implement a platform specific plugin
