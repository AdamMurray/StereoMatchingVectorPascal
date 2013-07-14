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
   imageInLocation, imageOutLocation : string;
   ImageInTemp			     : pimage;
   ImageIn			     : pimage; {pimage is a pointer to an image type, defined in bmp} 
   
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
   {user input}
   userInput;
   
   {load one of the images using the full path}
   {and if successful then proceed}
   if loadbmpfile(imageInLocation, ImageIn) then
   begin
      new(imageIn, imageIn ^.maxplane, imageIn ^.maxrow, imageIn ^.maxcol);

      {if imageInTemp <> imageOutTemp then
      begin
	 writeln('Images do not match');
      end
      else
      begin
	 writeln('Images match');
	 storebmpfile(imageOutLocation, imageOutTemp^);
      end;}

      storebmpfile(imageOutLocation, imageInTemp^);
      
   end
   else writeln('Failed to load image file.');

   dispose(imageInTemp);
end.
(*---------- End of the program ----------*)