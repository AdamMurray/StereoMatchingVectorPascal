Installation README for the Vector Pascal Compiler
========================================================

This file is improvement over the README contained in
the Vector Pascal install.jar.

Installation Steps (for Linux)
-------------------------------

* First, download the necessary dependencies: Java VM, Nasm, GCC, libc6-dev-i386 (if there are problems during first test of the compiler).

* Download the *install.jar* file from the [Vector Pascal sourceforge site](http://sourceforge.net/projects/vectorpascalcom/).

* Unpack the *install.jar* file by using the command: **jar -xf install.jar**.

* There will now be two new directories in the directory of the jar file.

* Enter into the directory named *mmpc* and run the commands: **make rtl**, **make rtl.o**.

* If necessary, change the permissions of the *vpc* file contained within the *mmpc* directory to give executable privileges. This can be done using the command: **chmod u+rwx vpc** (for user's privileges).

* Finally, create a file named *.bashrc* in the home directory (or if one already exists then update this file), and add the following lines:

**PATH=/path/to/mmpc:${PATH}:.**
**export PATH**
