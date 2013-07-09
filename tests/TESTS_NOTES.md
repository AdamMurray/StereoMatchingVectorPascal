Notes on the Tests
====================

This file contains notes on the tests and problems to be solved with them.

The date of creation of a test should be given with that particular test (in **bold**).

Problems should be given with the date (an approx. time) on which the problem was encountered.

New code should be committed before or after conducting a new version of a test.

testImageInputOutput.pas
--------------------------

**Date created: 26/06/13**.

*testImageInputOutput.pas* tests for correct input and output of the image files to be used in the program. If the test works correctly, the image input to the test and the image output from the test will be exactly equal (including colour).

This test will only use the *Left1.bmp* and *Right1.bmp* image files contained in the *images* folder of the project. Output images from this test will have file named following the convention: *date(DD-MM-YY)_(time(HH-MM))_inputImage*.

Problems:

* (26/06/13) The colour of the input image is different to that of the input image. In particular, the blue background of the input image becomes a shade of magenta in the output image.

Log:

* (09/07/13 14:43) Test produces 
