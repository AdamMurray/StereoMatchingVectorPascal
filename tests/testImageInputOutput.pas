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

(*---------- Global variable declarations ----------*)
var
   imageInLocation, imageOutLocation : string;
   imageInTemp			     : pimage;
   imageIn			     : pimage; {pimage is a pointer to an image type, defined in bmp} 
   
(*---------- Procedures ----------*)
(*
 *  Procedure to encapsulate user input
 *)
procedure userInput;
begin
   writeln('/==============================================/');
   writeln('/ Test: Input and Output of Full Colour Images /');
   writeln('/==============================================/');
   writeln;
   write('Enter the name (with full path) of the image to be loaded: ');
   readln(imageInLocation);
   write('Enter the name of the destination of output image: ');
   readln(imageOutLocation);
   writeln;
   writeln('<Press enter to initiate test>');
   readln;
end; { userInput }

(*---------- Main body of the program ----------*)
begin
   {User input}
   userInput;
   
   {Load one of the images using the full path}
   {and if successful then proceed}
   if loadbmpfile(imageInLocation, imageIn) then
   begin
      new(imageInTemp, imageIn ^.maxplane, imageIn ^.maxrow, imageIn ^.maxcol);

      if imageInTemp <> imageIn then
      begin
	 writeln('Images do not match');
      end
      else
      begin
	 writeln('Images match');
	 storebmpfile(imageOutLocation, imageInTemp^);
      end;      
   end
   else writeln('Failed to load image file.');

   {Dispose of image buffers}
   dispose(imageIn);
   dispose(imageInTemp);
end.
(*---------- End of the program ----------*)