ActionScript Compiler 2.0 Shell
===============================

`ascsh` and `ascshd`* are wrappers for the new ActionScript Compiler 2.0,
modelled to be roughly equivalent to the older Flex Compiler's `fcsh` and `fcshd`.
They improve AS3/AIR/SWF/SWC project compilation performance by maintaining a
persistent JVM compiler process.  See my [blog post](http://jcward.com/FCSH+for+ASC+2.0+Compiler)
for more info and a little backstory.

Setup
-----

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

`ascsh `is a shell (like `fcsh`) that it a shell and requires text input to kick off a new
build.  Simply enter your compile command at the prompt:

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

`ascshd` is a convenience wrapper around `ascsh` currently written in Ruby.  It
starts a background server thread to maintain the `ascsh` process, and sends
commands to it for builds.  This way, your build can simply call `ascshd` again
and again from your build system (no fussy shell), and it just gets faster over time:

````
> $AIR_HOME/bin/ascsdh mxmlc main.as -o Main.as
  ... 10 second build
> $AIR_HOME/bin/ascsdh mxmlc main.as -o Main.as
  ... 2 second build
````

Stop the server process by calling `ascshd kill`

* Note that **ascshd may not work under Windows**.  It's currently a Ruby script
that uses processes, sockets, and process I/O to operate, and it seems to
hang on Windows.  I'd like to hear others' feedback - feel free to contribute a
Windows-compatible `ascshd` (in any language you like - perl, python, Java, etc.)
The ruby script should be fairly straight-forward to replicate.

History
-------

* version 0.3 - ascsh mimics fcsh prompts (for FlashDevelop), but only one target supported for now
* version 0.2 - working ascsh, ascshd in Ruby (Windows support iffy)
* version 0.1 - proof of concept
