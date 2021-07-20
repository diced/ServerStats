# ServerStats
Visualize your Minecraft server statistics in realtime for Minecraft 1.8+ (*Only 1.17+ for Fabric*)

Want to learn more? Visit [serverstats.diced.me](https://serverstats.diced.me) for the latest downloads and documentation.

![Grafana Dashboard Example](https://raw.githubusercontent.com/diced/serverstats/trunk/example-grafana-dashboard.png)

## Downloading
There's multiple places where you can download ServerStats

### Stable Builds
* **`builds`** branch contains builds from every stable tag release
* **[Website](https://serverstats.diced.me/download)** contains the builds from stable releases
* **[GitHub Releases](https://github.com/diced/ServerStats/releases)** contains the builds from stable releases

(*[dl.diced.me/serverstats/latest](https://dl.diced.me/serverstats/latest)*)

### Development Builds (Bleeding Edge)
* **`dev-builds`** branch contains builds from every commit
* **GitHub Actions Artifacts** zip file containing jars from commit actions

## Features
* Easy: setup ServerStats in minutes
* Prometheus: export stats to prometheus
* Grafana: visualize your stats in Grafana using our provided dashboard
* Cross server compatible: works with proxy servers, Fabric and Bukkit compatible servers (Spigot, Paper, etc)

## Metrics
This is a list of all the metrics provides. The code block has the config property.
[Docs Link to Configuration](https://serverstats.diced.me/docs/config#pushable)
### Player Count `player-count`
The online player count
### Free Memory `free-memory`
Free memory in JVM
### Max Memory  `max-memory`
Max memory in JVM
### Total Memory `total-memory`
Total memory allocated to JVM
### TPS `tps`
Ticks per-second (usually 20)
### MSPT `mspt`
Milliseconds per-tick (usually below 50ms for good perf)
### CPU % `cpu`
CPU Percentage
### Loaded Chunks `loaded-chunks`
Loaded chunks in each world
### Entity Count `entity-count`
Entity count in each world
### Disk Space used `disk-space`
Disk space that each world is using
### Packets Sent/Read `packets`
Packets that are being sent to clients and being read from clients
### Garbage Collection Time `gc`
The time it took to do the last GC
### Thread Count `threads`
The amount of JVM threads
### Uptime `uptime`
The uptime of the server

## Compiling
You must be using Java 16+ as Fabric/Minecraft 1.17 requires it, or go into the directory you want and build using a different JDK.
```shell
git clone https://github.com/diced/ServerStats
cd ServerStats
./build.sh # custom build shell script that builds all modules and gives all jars in builds/
```

## Project Layout
ServerStats utilizes mono-repos/modules to have different server types in one repo.

* **Common** - Common modules with interfaces and abstract classes that bukkit/bungee/fabric/velocity use
* **Bukkit/Bungee/Fabric/Velocity** - Uses the common module's interfaces to implement a platform specific plugin
