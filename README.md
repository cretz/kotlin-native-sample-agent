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

### What Did We Learn?

Notes (only valid as of the time this was written):

* Kotlin Native is a pain to use with IDEA and it's unfortunate that they are going w/ CLion
* There is no good way to pass C-instantiated objects into Kotlin
  * But you can pass `Long`s and work with them like pointers which is nice
* There is no good way to export top-level symbols from Kotlin Native
  * But a C/C++ shim is doable
* There is no easy way to add your own C/C++ files to the build
  * I had to add the C++ file and then export all symbols so it would not override exports from existing Kotlin files
* The concept of nullable native pointers really meshes well with Kotlin's null checks 
* All of the interop names and aliases can become confusing because of similar names
  * E.g. `CPointer` vs `CPointed` vs `CPointerVar` vs `CPointerVarOf`
  * Someone needs to write the `Kotlinomicon` like [they did for Rust](https://doc.rust-lang.org/nomicon/)
* In gradle, `linkerOpts` actually goes to `clang++`, so I had to add `-Xlinker`
* In gradle, spaces in Windows' paths are not handled properly in `linkerOpts` and you have to manually quote them
* Overall, Kotlin Native has a very bright future. Once you are out of the FFI world, things should be smooth.