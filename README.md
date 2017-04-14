### Seally Racing ###

Seally Racing is a racing game in a 3D space with seals! This project targets
jMonkeyEngine 3 and is written in Java.

#### Gameplay

Fly around in a 3D space (under the sea, in the air, etc) and follow a course
marked by rings. Race your opponents and be the first to finish. Currently, the
plan is to make a single network multiplayer mode, but this plan is not
finalised.

#### Building and Launching

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

* Look at our issue tracker and wiki at the
  [Seally Racing project in Redmine](https://development.meoblast001.info/projects/seally-racing).
* Join irc.freenode.net on the channel #seally-racing to get up to date with the
  project status.
* Fork the project, change the code or art, and submit a pull request.
