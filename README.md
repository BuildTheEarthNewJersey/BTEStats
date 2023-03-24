# BTEStats

BTEStats is a Minecraft 1.12.2 plugin for Build the Earth New Jersey that collects building statistics and stores it in MongoDB. This data can also be accessed in game. 

Users can see data such as:
- Login Streak
- Percentage of NJ completed
- Blocks placed leaderboards

## Building with IntelliJ

1. Open IntelliJ and run the maven configuration `[clean, package]`. The jar file will be stored in `./TestServer/plugins`
    - Note: If you don't see the build configuration, restart IntelliJ
    - Note: Any changes in the code require the jar file to be rebuilt
## Debugging with IntelliJ

1. Follow the steps above to build with IntelliJ
2. Download a minecraft 1.12.2 server jar, rename it to `server.jar`  and add it into `./TestServer`
3. Create an IntelliJ JAR Application run configuration. In the run configuration:
   1. Set `Path to JAR` to the absolute path of `server.jar`
   2. Set `Working directory` to the absolute path of `./TestServer`
4. Run the new JAR application configuration