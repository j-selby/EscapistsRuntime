# Unofficial Escapists Runtime (UER)
[Downloads/CI Builds](https://ci.jselby.net/job/Escapists%20Runtime/)


UER is a WIP project designed to implement modding and Android support for The Escapists.

Why?
The Escapists is a fully fledged prison escape game, and that allows for many cool ideas within the universe. On the games [Steam Workshop](http://steamcommunity.com/app/298630/workshop/) page, there are thousands of maps that people are designing that show creative ways of navigating the games mechanics. However, there is no way to add custom textures for maps, custom scripts, or anything that doesn't exist within the base game. This project was created in order to solve this problem.

Cross-platform compatability has always been a critical element of the project, using [Mini2DX](http://mini2dx.org/) to ensure that platform-specific differences are abstracted away. As a result, Android (and in theory, iOS and HTML5 as well) can now run the runtime.

## TODO
There are still many elements of the project that aren't ready for primetime yet. These include:

- Full rendering capabilities
- Finishing the base scripting engine
- Non-standard functionality

See the [TODO](https://github.com/j-selby/EscapistsRuntime/blob/master/TODO.md) file for a more up-to-date TODO list.

## Building
Gradle is used within the project to help ease building.

You will need ANDROID_HOME (the Android SDK) in your environment for this to work.

The .bat extension is not required when using OS X/Linux.

To build for desktop:
> gradlew.bat launchDesktop

To build for Android:

> gradlew.bat android:installDebug android:run

## License
UER is licensed under the MIT license, located within [LICENSE](https://github.com/j-selby/EscapistsRuntime/blob/master/LICENSE). 
Portions of the application are also based on [Anaconda/Chowdren](https://github.com/matpow2/anaconda), which is under the GPL.
