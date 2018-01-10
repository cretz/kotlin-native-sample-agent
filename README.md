# Kotlin Native Sample JVMTI Agent

This is a sample of creating a [JVMTI agent](https://docs.oracle.com/javase/9/docs/specs/jvmti.html) with
[Kotlin Native](https://github.com/JetBrains/kotlin-native/). At this time I have only tested it on Windows.

In order to build, the latest `gradle` and JDK >= 8 has to be installed. Then from the cloned directory, run:

    path/to/gradle --no-daemon build

This will build a DLL at `build/konan/bin/{target}/sampleagent.{ext}` where `{target}` is the platform (e.g. `mingw`)
and `{ext}` is the shared lib extension (e.g. `.dll`).

Now this can be passed as the agent on and `java` command. For example, in a directory with `HelloWorld.class` that
simply prints out `Hello, World!`, the agent can be used like so:

    java -agentpath:path/to/sampleagent.dll HelloWorld

At this early stage, it would dump:

    Begin onload
    Creating symbols
    Created symbols
    Inside of Kotlin test function
    Ran function
    Hello, World!

Lots more work to do like crossing platforms, properly passing the pointers to the funcs, etc.