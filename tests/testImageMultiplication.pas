(*
 *  Test: Image multiplication.
 *
 *  This program is a test for the ability
 *  to multiply two images together to produce
 *  a product image. Multiplication should be
 *  on corresponding pixels, i.e. distinct from
 *  array or matrix multiplication.
 *)
program testImageMultiplication;

(*---------- Uses declarations ----------*)
uses bmp;

(*---------- Gloval variable declarations ----------*)
var
   {Images}
   ImageA, ImageB, ProductImage : pimage;

(*---------- Procedures ----------*)
(*
 * Procedure to multiply together images
 * (3D arrays) to test that the result is
 * what is expected.
 *)
procedure multImages;
var
   {Temporary images for ImageA and ImageB}
   ImageATemp, ImageBTemp : pimage;
begin
   if loadbmpfile('../images/Left1.bmp', ImageA)
      and loadbmpfile('../images/Right1.bmp', ImageB) then
   begin
      {Initialise temporary image arrays}
      writeln('Initialising arrays...');      
      new(ImageATemp, ImageA ^.maxplane, ImageA ^.maxrow, ImageA ^.maxcol);
      new(ImageBTemp, ImageB ^.maxplane, ImageB ^.maxrow, ImageB ^.maxcol);
      new(ProductImage, ImageA ^.maxplane, ImageA ^.maxrow, ImageA ^.maxcol);

      {Assign temp images to the corresponding image}
      ImageATemp^ := ImageA^;
      ImageBTemp^ := ImageB^;

      {Produce product image}
      writeln('Multiplying together images...');      
      ProductImage^ := ImageATemp^ * ImageBTemp^;

      {Store the product image}
      writeln('Storing product image...');
      storebmpfile('multImagesProductImages.bmp', ProductImage^);
      writeln('Product image stored.');

      {Dispose of the temporary image buffers}      
      dispose(ImageATemp);
      dispose(ImageBTemp);
      dispose(ProductImage);
   end
   else
      writeln('Error --> image files could not be loaded');
end;

(*---------- Main body of the program ----------*)
begin
   writeln('/============================/');
   writeln('/ Test: Image Multiplication /');
   writeln('/============================/');
   writeln;
   
   {Multiply images, store image}
   writeln('<Press ENTER to start image multiplication test>');
   readln;
   multImages;

   {Dispose of image buffers}
   dispose(ImageA);
   dispose(ImageB);
end.
(*---------- End of the program ----------*)