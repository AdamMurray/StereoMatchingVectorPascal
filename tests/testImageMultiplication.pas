(*
 *  Test: Image multiplication.
 *
 *  This program is a test for the ability
 *  to multiply two images together to produce
 *  a product image. Multiplication should be
 *  on corresponding pixels, i.e. distinct from
 *  array or matrix multiplication.
 *
 *  Since images are 3 dimensional arrays, a number
 *  of specific things will be tested in this
 *  program, as follows:
 *     > multiplication of simple 1D arrays;
 *     > multiplication of simple 2D arrays;
 *     > multiplication of simple 3D arrays;
 *     > multiplication of images.
 *)
program testImageMultiplication;

(*---------- Uses declarations ----------*)
uses bmp;

(*---------- Type declarations ----------*)
type
   {Array range}
   arrayRange = 1..5;
   {Array types}
   OneDIntArray = array[arrayRange] of integer;
   TwoDIntArray = array[arrayRange, arrayRange] of integer;
   ThreeDIntArray = array[arrayRange, arrayRange, arrayRange] of integer;

(*---------- Constant declarations ----------*)
const
   {Array Size (used to iterate over arrays)}
   arraySize = 5;

(*---------- Variable declarations ----------*)
var
   {1D arrays}
   OneDArrayA, OneDArrayB, OneDProductArray : OneDIntArray;
   {2D arrays}
   TwoDArrayA, TwoDArrayB, TwoDProductArray : TwoDIntArray;
   {3D arrays}
   ThreeDArrayA, ThreeDArrayB, ThreeDProductArray : ThreeDIntArray;
   {Images}
   ImageA, ImageB, ProductImage : pimage;

(*---------- Procedures ----------*)
(*
 * Procedure to multiply together 1D
 * arrays to test that the result is
 * what is expected.
 *)
procedure mult1DArrays;
var
   i : arrayRange;
begin
   {initialise arrays}
   for i := 1 to arraySize do
   begin
      OneDArrayA[i] := i;
      writeln('Number added to 1DArrayA: ', i:2);
      OneDArrayB[i] := i;
      writeln('Number added to 1DArrayB: ', i:2);
   end;

   {multiply arrays}
   OneDProductArray := OneDArrayA * OneDArrayB;
end; { mult1DArrays }

(*
 * Procedure to multiply together 2D
 * arrays to test that the result is
 * what is expected.
 *)
procedure mult2DArrays;
var
   i, j	: arrayRange;
begin
   for i := 1 to arraySize do
      for j := 1 to arraySize do
      begin
	 TwoDArrayA[i][j] := 2;
	 TwoDArrayB[i][j] := 2;
      end;

   {multiply array}
   TwoDProductArray := TwoDArrayA * TwoDArrayB;
end; { mult2DArrays }

(*
 * Procedure to multiply together 3D
 * arrays to test that the result is
 * what is expected.
 *)
procedure mult3DArrays;
var
   i, j, k : arrayRange;
begin
   for i := 1 to arraySize do
      for j := 1 to arraySize do
	 for k := 1 to arraySize do
	 begin
	    ThreeDArrayA[i][j][k] := 2;
	    ThreeDArrayB[i][j][k] := 2;
	 end;

   ThreeDProductArray := ThreeDArrayA * ThreeDArrayB;
end; { mult3DArrays }

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
   writeln('/****************************/');
   writeln('/ Test: Image Multiplication /');
   writeln('/****************************/');
   writeln;
   
   {Multiply 1D arrays, print all arrays}
   writeln('/* 1D Array Multiplication */');
   writeln;
   
   mult1DArrays;
   writeln;
   write('The contents of 1D array A: ');
   write(OneDArrayB:2);
   write('The contents of 1D array B: ');
   write(OneDArrayA:2);
   write('The contents of the product array are: ');
   write(OneDProductArray:3);

   {Multiply 2D arrays, print all arrays}
   writeln;
   writeln('/* 2D Array Multiplication */');

   mult2DArrays;
   writeln;
   writeln('The contents of 2D array A: ');
   write(TwoDArrayA:2);
   writeln('The contents of 2D array B: ');
   write(TwoDArrayB:2);
   writeln('The contents of the product array are: ');
   write(TwoDProductArray:2);

   {Multiply 3D arrays, print all arrays}
   writeln;
   writeln('/* 3D Array Multiplication */');

   mult3DArrays;
   writeln;
   writeln('The contents of 2D array A: ');
   write(ThreeDArrayA:2);
   writeln('The contents of 2D array B: ');
   write(ThreeDArrayB:2);
   writeln('The contents of the product array are: ');
   write(ThreeDProductArray:2);

   {Multiply images, store image}
   writeln;
   writeln('/* Image Multiplication */');

   writeln;
   writeln('<Press ENTER to start image multiplication test>');
   readln;
   multImages;

   {Dispose of image buffers}
   dispose(ImageA);
   dispose(ImageB);
end.
(*---------- End of the program ----------*)