# Kotlin Native Sample JVMTI Agent

This is a sample of creating a [JVMTI agent](https://docs.oracle.com/javase/9/docs/specs/jvmti.html) with
[Kotlin Native](https://github.com/JetBrains/kotlin-native/). As an example, it displays all the known system property
names on agent load. At this time I have only built and tested it on Windows.

In order to build, the latest `gradle` and JDK >= 8 has to be installed. Then from the cloned directory, run:

    path/to/gradle --no-daemon build

This will build a DLL at `build/konan/bin/{target}/sampleagent.{ext}` where `{target}` is the platform (e.g. `mingw`)
and `{ext}` is the shared lib extension (e.g. `.dll`).

Now this can be passed as the agent on and `java` command. For example, in a directory with `HelloWorld.class` that
simply prints out `Hello, World!`, the agent can be used like so:

    java -agentpath:path/to/sampleagent.dll HelloWorld

When run on Windows for me, this is the output::

    Displaying 16 property names:
    java.vm.specification.name
    java.vm.version
    java.vm.name
    java.vm.info
    java.ext.dirs
    java.endorsed.dirs
    sun.boot.library.path
    java.library.path
    java.home
    java.class.path
    sun.boot.class.path
    java.vm.specification.vendor
    java.vm.specification.version
    java.vm.vendor
    sun.java.command
    sun.java.launcher
    Hello, World!

That's basically it for this PoC. I might add a linux version at some point.