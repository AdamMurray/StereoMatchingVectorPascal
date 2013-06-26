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
   imageInLocation, imageOutName : string;
   imageIn, imageOut		 : pimage; {pimage is a pointer to an image type, defined in bmp}

(*---------- Main body of the program ----------*)
begin
   writeln('// Test: Input and Output of Full Colour Images');
   writeln;
   write('Enter the name (with full path) of the image to be loaded: ');
   readln(imageInLocation);
   write('Enter the name of the destination of output image: ');
   readln(imageOutName);
   writeln;
   writeln('<Press enter to initiate test>');
   readln;
   
   {load one of the images using the full path}
   {and if successful then proceed}
   if loadbmpfile(imageInLocation, imageIn) then
   begin
      new(imageOut, imageIn ^.maxplane, imageIn ^.maxrow, imageIn ^.maxcol);

      imageOut^ := imageIn^;

      if imageIn <> imageOut then
      begin
	 writeln('Images do not match');
      end
      else
      begin
	 writeln('Images match');
	 storebmpfile(imageOutName, imageOut^);
      end;
   end
   else writeln('Failed to load image file.');
end.
(*---------- End of program ----------*)