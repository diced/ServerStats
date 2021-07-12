# ServerStats
Visualize your Minecraft server statistics in realtime for Minecraft 1.8+ (*Only 1.17+ for Fabric*)

Want to learn more? Visit [serverstats.diced.me](https://serverstats.diced.me) for the latest downloads and documentation.

## Downloading
There's multiple places where you can download ServerStats

### Stable Builds
* **`builds`** branch contains builds from every stable tag release
* **[Website](https://serverstats.diced.me/download)** contains the builds from stable releases
* **[GitHub Releases](https://github.com/diced/ServerStats/releases)** contains the builds from stable releases
<br>
(*[https://dl.diced.me/serverstats/latest](https://dl.diced.me/serverstats/latest)*)

### Development Builds (Bleeding Edge)
* **`dev-builds`** branch contains builds from every commit
* **GitHub Actions Artifacts** zip file containing jars from commit actions

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
