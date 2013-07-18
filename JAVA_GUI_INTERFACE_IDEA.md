Idea for a GUI Written in Java to Interface with the Algorithm Source Code
===========================================================================

**Date created: 17/07/13**.

I had an idea for a nice way to run my algorithm once it is finished. Basically, I want to try to
write a GUI in Java which can open the Vector Pascal program and pass it information, such as path
names for images to be loaded and then matched.

The GUI would be reasonably simple, as far as my plans go now. It would have one window, a menu bar at
the top with options such as to exit, run, etc. There would be some information in the GUI about what
the program does, a way to select the two images to be compared, a way to run the program, and a bunch
of other things that will come together once I start developing the GUI.

I'll start by writing a version just to run on Linux (specifically my distro), and I'll need to consider
how I'll work selecting which terminal to open (xterm, gnome-terminal, konsole, etc), either using grep
to find the path, or getting the user to select which terminal they use on their system (which seems
reasonable on Linux since users would usually know a bit about what's going). I'll then go onto write
the program so that the user chooses which OS they use (Linux or Windows, and maybe even Mac OS!), which
could be done through some nifty JOptionPanes, and then the code will select how to proceed as appropriate.

It would be nice to hide everything from the user; however, so far I know that a terminal can be opened and
commands can be sent to it, so if the terminal needs to be visible then I can deal with that. If I can get it
all running in the background then that would be marvelous.

I also will need to find a way to get information from the GUI, such as path to images to be matched, and then
pass those paths to the Vector Pascal program so that it can use those images in matching. I'm sure there will
be a way to construct a string that runs the VP code with parameters, just like can be done with most programming
languages, but anyway I'll find that out in time.

The important thing about this idea is that it's far from the focus of my project, so I shouldn't spend a huge
amount of time on it. I need to get the main stuff done, and then write this in the spare time. It will, however,
help my Java skills which will help get me a job probably, so it's a good thing to be working on.

So, what should the GUI have:

* A menu bar at the top, with the following items; exit, ...

* 
