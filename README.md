ActionScript Compiler 2.0 Shell
===============================

`ascsh` and `ascshd`* are wrappers for the new ActionScript Compiler 2.0,
modelled to be roughly equivalent to the older Flex Compiler's `fcsh` and `fcshd`.
They improve AS3/AIR/SWF/SWC project compilation performance by maintaining a
persistent JVM compiler process.  See my [blog post](http://jcward.com/FCSH+for+ASC+2.0+Compiler)
for more info and a little backstory.

Setup
-----

FlashDevelop users don't need to do any setup, as `ascsh.jar` is included in
version 4.6.1 and later.  Command-line users should follow these instructions:

Clone (or download) the repo and run `ant install`.  This will install `ascsh` and
`acsshd` into the AIR SDK bin and lib directories.  You must have the `AIR_HOME`
environment variable configured to point to your AIR SDK.

```
>ant install
Buildfile: /home/admin/dev/ascsh/build.xml

compile:

install:
     [echo] ----------------------------------------------------------------
     [echo] Installing ascsh to AIR_HOME at /opt/air_sdk_3.9
     [echo] ----------------------------------------------------------------
     [copy] Copying 2 files to /opt/air_sdk_3.9/lib
     [copy] Copying 3 files to /opt/air_sdk_3.9/bin
     [echo] ----------------------------------------------------------------

BUILD SUCCESSFUL
Total time: 0 seconds
```

You may also run `ant uninstall` to remove files from your AIR SDK.

Usage
-----

You can use this project from FlashDevelop, or from the command-line like
fcsh or fcshd.

**Using ASCSH with FlashDevelop**

FlashDevelop 4.6.1 and newer include this ascsh jarfile, so building with the AIR
SDK should result in fast compile times.  In the output panel for the first build
you should see `INITIALIZING: ascsh v0.3 by Jeff Ward` and on subsequent builds
`Incremental compile of 1`.  If you have problems, you might check the [FlashDevelop
forums](http://www.flashdevelop.org/community/) for help with asc2 incremental compilation.

Developers may wish to modify `ascsh`, rebuild with `ant jar`, and re-install the
resultant `dist/ascsh.jar` file into your FlashDevelop installation.

**Using ASCSHD from the command-line**

`ascshd` is a convenience wrapper around `ascsh` and is the most common way of invoking
`ascsh` from the command-line.  It operates just like `fcshd` - it starts a background
server thread to maintain the `ascsh` process and sends commands to it for builds.
This way, your build can simply call `ascshd` by prefixing it to your `mxmlc`
command, and it just gets faster over time:

````
> $AIR_HOME/bin/ascshd mxmlc main.as -source-path+=. -optimize -o main.swf
  ... 10 second build (initial compile)
> $AIR_HOME/bin/ascshd mxmlc main.as -source-path+=. -optimize -o main.swf
  ... 2 second build (subsequent compiles)
````

Stop the server process by calling `$AIR_HOME/bin/ascshd kill`

**Multiple instances of ascshd**

To run multiple instances of `ascshd`, specify a port number before the
mxmlc or kill commands:

````
> $AIR_HOME/bin/ascshd -p 11123 mxmlc app1.as -source-path+=. -optimize -o app1.swf
> $AIR_HOME/bin/ascshd -p 11124 mxmlc app2.as -source-path+=. -optimize -o app2.swf
> $AIR_HOME/bin/ascshd -p 11123 kill
> $AIR_HOME/bin/ascshd -p 11124 kill
````

To kill all instances at once, use:
````
> $AIR_HOME/bin/ascshd killall
````

* Note that the `ascshd` server may not work under Windows.  It's currently a Ruby
script that uses processes, sockets, and process I/O to operate, and it seems to
hang on Windows.  I'd like to hear others' feedback - feel free to contribute a
Windows-compatible `ascshd` (in any language you like - perl, python, Java, etc.)
The ruby script should be fairly straight-forward to replicate.

**Using ASCSH from the command-line**

`ascsh` itself is a shell (like `fcsh`) and requires text input to kick off initial
and subsequent builds.  It is invoked under-the-hood when you use `ascshd` or
FlashDevelop.  But you can use it from the command-line, should you want to.
Simply enter your compile command at the prompt:

````
> $AIR_HOME/bin/ascsh
ascsh v0.2 by Jeff Ward, simbulus.com
(ascsh)
mxmlc main.as -o Main.as
  ... 10 second build
(ascsh)
mxmlc main.as -o Main.as
  ... 2 second build
(ascsh)
````

Version History
---------------

* version 0.5 - Updated README, build.xml uses Java target 1.6 compatibility, ascshd support for specifying port (multiple instances), ascsh_cmd.java created (see below)
* version 0.4 - Fixes for FlashDevelop (thanks to Philippe Elsass)
* version 0.3 - ascsh mimics fcsh prompts (for FlashDevelop), but only one target supported for now
* version 0.2 - working ascsh, ascshd in Ruby (Windows support iffy)
* version 0.1 - proof of concept

ascsh.java vs ascsh_cmd.java
----------------------------

Unfortunately, the FlashDevelop-compatible ascsh.java no longer works correctly from the command-line,
and I can't figure out why.  I spent a long time commenting/uncommenting code that just shouldn't
make a difference.  If ascsh.java is used in the command-line flow, it seems to ignore configuration
settings that should be setting the Flash target version to 11.9, and trying to load 11.1 instead:

````
> cd test; $AIR_HOME/bin/ascsh
ascsh v0.4 by Jeff Ward, simbulus.com
(fcsh) mxmlc simple.as
fcsh: Assigned 1 as the compile target id
Loading configuration: /opt/air_sdk_3.9/frameworks/flex-config.xml

/opt/air_sdk_3.9/frameworks/flex-config.xml:47: Error: unable to open '/opt/air_sdk_3.9/frameworks/libs/player/11.1/playerglobal.swc'.
/opt/air_sdk_3.9/frameworks/flex-config.xml (line: 47)
        </external-library-path>
Compile status: 4
(fcsh) ^C
````

The upshot is, I had to introduce `ascsh_cmd.java` that works for the command-line, and leave
`ascsh.java` that works with FlashDevelop.  I'm also not going to commit a `dist/ascsh.jar`
as I'll assume Philippe's last version is good.
