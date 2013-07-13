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

**Problems**:

* (26/06/13) The colour of the input image is different to that of the input image. In particular, the blue background of the input image becomes a shade of magenta in the output image.

**Log**:

* (09/07/13 14:43) Test produces a grey or blank image on output. There is clearly a problem with the way in which I am using pointers to the various image components.
* (09/07/13 15:01) Clearly making a pointer to a pimage does not compile, as I have just seen.
* (09/07/13 15:15) Referencing the image components in this fashion *imageIn.maxplane*, as opposed to *imageIn ^.maxplane* (as seen in the textbook) compiles; however, the output image is again grey/blank.
* (09/07/13 15:26) Simply loading in a bmp file (in this case *Right1.bmp*) and storing it without any intermediate storage or modificaton outputs the image with modified colour. The background of *Right1.bmp* is blue but is output as magenta.
* (09/07/13 15:45) Going to try to further develop the other components of the first step algorithm, leave this specific part to ask Cockshott. It seems that I can read in the files fine but there is that colour problem. Sure I can do most of the other work (if possible) and sort the colour later.
* (12/07/13 00:09) I can now solve the reading in issue from what I learned in *testImageMultiplication.pas*. The next step is working out how to preserve the colour of the original image when it is read in.


testImageMultiplication.pas
-----------------------------

**Date created: 11/07/13**.

*testImageMultiplication.pas* tests that a correct product image is produced when the images *Left1.bmp* and *Right1.bmp* are multiplied together.

A number of small tests on multiplying together arrays of various dimensions are also contained in this test; however, they are just a way of testing my own Vector Pascal knowledge before taking on the main point of the test.

**Problems**:

* (11/07/13) The same colour problem found in the *testImageInputOutput.pas* test is found in this test, as can be seen in the product image produced from muliplying together *Left1.bmp* and *Right1.bmp*.

**Log**:

* (12/07/13 00:05) Test finally works after many various modifications. The output product image is indeed the product of *Left1.bmp* and *Right1.bmp*; however, the colour problem highlighted above is evident.


testImageConvolution.pas
--------------------------

**Date created: 13/07/13 (13:20)**.

*testImageConvolution.pas* tests that a specific convolution matrix is applied correctly to an image.

This test will proceed in the following steps:

* Devise a way to apply a very simple convolution matrix to a simple integer array of, say, 5x5.

* Extend this application of a simple convultion matrix to work on images, specifically the product image generated in *testImageMultiplication.pas*.

* Test the that the convolution works properly by using some test images. (Details to be worked out later).

**Problems**:

* None so far.

**Log**:

* (13/07/13 13:22) *testImageConvolution.pas* initially created.











