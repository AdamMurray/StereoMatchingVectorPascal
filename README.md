Parallelised Exhaustive Search Stereo Matching Algorithm - Vector Pascal
=========================================================================

The code contained in this repository is for the development of a parallelised exhaustive
search stereo matching algorithm writtin in Vector Pascal *([the compiler is described here]
(http://www.dcs.gla.ac.uk/~wpc/reports/compilers/compilerindex/x25.html)).*

Algorithm Development
------------------------

The development of the algorithm (and other research which will be conducted) will proceed as a number of steps.

The first step (begun in the file *mastersfirststep.pas* and uploaded on 26/06/13) is as follows:

* Read in two image files (in .bmp format) - the left and right images of the stereo pair;
* Multiply the images together to produce a product image;
* Apply a correlation function (SSD) to the product image;
* Convolve the image after correlation;
* Output the result.

Tests
---------

A number of tests will be written as the code is developed to test key features. The format of the files containing tests will be: *test<ThingBeingTested>.pas*.


