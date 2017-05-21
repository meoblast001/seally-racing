### Seally Racing ###

Seally Racing is a racing game in a 3D space with seals! This project targets
jMonkeyEngine 3 and is written in Java.

#### Gameplay

Fly around in a 3D space (under the sea, in the air, etc) and follow a course
marked by rings. Race your opponents and be the first to finish. Currently, the
plan is to make a single network multiplayer mode, but this plan is not
finalised.

#### Building and Launching

First you must check out the assets submodule if you want the game to run. From
the project directory, run:

```shell
git submodule init
git submodule update
```

Gradle is used to build this codebase.
A wrapper is provided that can download Gradle.
You just need Java first, then you can run via Gradle:

```shell
./gradlew run
```

Optionally, build a runnable distribution:

```shell
./gradlew installDist
cd build/install/seally-racing
bin/seally-racing
```

#### Getting Involved

While this project is in early stages of development, there are several ways you
can start getting involved:

* Look at issues and milestones in the
  [issue tracker on GitHub](https://github.com/meoblast001/seally-racing/issues).
* Read the [wiki on GitHub](https://github.com/meoblast001/seally-racing/wiki).
* Join us in IRC on freenode (irc.freenode.net) in the channel #seally-racing.
  If you don't have or want your own IRC client, you can use the
  [freenode web chat](https://webchat.freenode.net/) or services like
  [IRCCloud](https://www.irccloud.com/).
* Fork the project, change the code or art, and submit a pull request.
