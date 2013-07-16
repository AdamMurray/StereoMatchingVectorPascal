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

* Devise a way to apply a very simple convolution matrix to a reasonably small array.

* Extend this application of a simple convultion matrix to work on images, specifically the product image generated in *testImageMultiplication.pas*.

* Test the that the convolution works properly by using some test images. (Details to be worked out later).

**Problems**:

* (13/07/13 13:22) None so far.

* (16/07/13 23:26) Seg faults everywhere. Currently need to handle problems with assigning values to the kernel and trying to stop a seg fault at the point at which the kernel assignment is made. There are also some small problems in the code such as the **p[n,row,col] := p[n,row,col] +  r^[n];** noted in the log. According to the compiler output, r^[n] should have rank 0 in the context but has rank 2, although it seems to me that to add those two arrays together the rank of r^[n] should be 2 or 3.

* (16/07/13 00:35) Seg fault problems solved so far. Now have some more issues as mentioned in the log.

**Log**:

* (13/07/13 13:22) *testImageConvolution.pas* initially created.

* (16/07/13 17:02) Using the *genconv* procedure from *SIMD Programming Manual* as a basis for the convolution. So far there are a few bugs, partly due to what seem to be typos in the book, and partly due to things I haven't figured out yet. One line in particular (from the doedges procedure) is a head-scratcher: **p[n,row,col] := p[n,row,col] +  r^[n];**. This line throws up a compiler error to do with incompatible array rank. Still to figure out why this doesn't work. For the meantime, the **+ r^[n]** part has been commented out.

* (16/07/13 23:19) Another problem which is not currently understood, and could be an error on my part, is that the code seemed to produce a Seg fault at two different places in the code without the code having been changed in between running it. There was a segmentation fault in assigning the temporary copy of the input image with data from the input image itself, the code was then re-compiler (without any changes as far as I recall) and the seg fault arose in a different place, just before assignment should be made to the kernel. Currently, after multiple re-compilation of the same code, the seg fault continually arises at the point of kernel assignment.

* (16/07/13 00:22) I'm a complete idiot. The seg fault at kernel initialisation is because I didn't create space for the kernel on the heap. Now working out how to set the bounds of the kernel within the **new(...)** statement.

* (16/07/13 00:35) Sorted out how to apply bounds to the kernel while creating space for it on the heap. Not altogether sure if the bounds are correct, but need to experiment a bit. Currently getting a runtime error while the convolution is being applied: **Line    129 error    201 array or subrange bounds error**










