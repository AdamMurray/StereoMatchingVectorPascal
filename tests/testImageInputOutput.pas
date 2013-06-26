(*
 *  Test: Image input and output.
 *
 *  This program is a test for the correctness of
 *  image input and output. An image (full colour)
 *  will be read in and then stored again. If the
 *  images are exactly equal then the test is a
 *  success, otherwise the test fails.
 *)
program testImageInputOutput;

(*---------- Uses declarations ----------*)
uses bmp;

(*---------- Variable declarations ----------*)
var
   imageIn, imageOut : pimage; {pimage is a pointer to an image type, defined in bmp}

(*---------- Main body of the program ----------*)
begin
   {load one of the images using the full path}
   {and if successful then proceed}
   if loadbmpfile('../images/Left1.bmp', imageIn) then
   begin
      new(imageOut, imageIn ^.maxplane, imageIn ^.maxrow, imageIn ^.maxcol);

      imageOut^ := imageIn^;
      storebmpfile('testImageInputOutput.bmp', imageOut^);

      writeln('If the input and output images match then you''re good!');
   end
   else writeln('Failed to load image file.');
end.