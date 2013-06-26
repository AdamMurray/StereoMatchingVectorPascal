Parallelised Exhaustive Search Stereo Matching Algorithm - Vector Pascal
=========================================================================

The code contained in this repository is for the development of a parallelised exhaustive
search stereo matching algorithm writtin in Vector Pascal ([the compiler is described here]
(http://www.dcs.gla.ac.uk/~wpc/reports/compilers/compilerindex/x25.html)).

Algorithm Development
------------------------

The development of the algorithm, and other research which will be conducted, will proceed in a number of steps.

The first step (begun in the file *mastersfirststep.pas*, uploaded on 26/06/13) is as follows:

* Read in two image files (in .bmp format) - the left and right images of the stereo pair;
* Multiply the images together to produce a product image;
* Apply a correlation function (Sum of Squared Differences (SSD)) to the product image;
* Convolve the image after correlation;
* Output the result.

Testing
---------

Testing of the key features of the algorithm will consist of a number of explicitly programmed standalone tests.

All tests are contained within the *tests* folder.

The format of all test files is: *testThingBeingTested.pas*.


