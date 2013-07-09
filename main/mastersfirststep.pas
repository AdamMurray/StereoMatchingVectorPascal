(*
 *  First step in for the masters project algorithm
 *
 *  Steps:
 *     > read in two images (using bmp.pas loadbmpfile);
 *     > multiply the images to get a product image;
 *     > apply a correlation function (SSD) to the product image;
 *     > apply a convolution;
 *     > output the image.
 *)
program mastersfirststep;

(*---------- Uses declarations ----------*)
uses bmp;

(*---------- Constant declarations ----------*)
{const
   ;}


(*---------- Type declarations ----------*)
type
   pint = ^integer;


(*---------- Variable declarations ----------*)
var
   currentDate, currentTime, productImageLocation : string;
   leftImageLocation, rightImageLocation	  : string;
   leftImage, rightImage, productImage		  : pimage;
   leftImageTemp, rightImageTemp		  : pimage;


(*---------- Functions ----------*)

(*
 *  Function to load in both images.
 *  Returns boolean -- true if images read in properly, false otherwise
 *)
function loadImages(limagein, rimagein : string):boolean;
begin
   {take a string as source and a pimage to load image}
   if loadbmpfile(leftImageLocation, leftImage)
      and loadbmpfile(rightImageLocation, rightImage) then
   begin
      loadImages := true;
   end
   else
   begin
      loadImages := false;
   end;
end; { loadimages }


(*---------- Procedures ----------*)

(*
 *  Procedure to get user input at the
 *  beginning of the program
 *)
procedure userInput;
begin
   writeln('// First step for masters algorithm');
   writeln('// Reads in two images and applies a correlation');
   writeln;
   write('Enter the current date (DD-MM-YY): ');
   readln(currentDate);
   write('Enter the current time (HH-MM): ');
   readln(currentTime);
   write('Enter the file name (with path) for the left image: ');
   readln(leftImageLocation);
   write('Enter the file name (with path) for the right image: ');
   readln(rightImageLocation);
   writeln;
   writeln('<Press ENTER to initiate program>');
   readln;
end; { userInput }

(*
 *  Procedure to generate a product image
 *  from the left and right images
 *)
procedure generateProductImage;
begin
   new(newTestInteger);
   new(leftImageTemp, leftImage ^.maxplane, leftImage ^.maxrow, leftImage ^.maxcol);
   new(rightImageTemp, rightImage ^.maxplane, rightImage ^.maxrow, rightImage ^.maxcol);

   writeln('In ''generate product image''');
   {productImage^ := leftImage^ * rightImage^;}
end; { generateproductimage }


(*---------- Main body of program ----------*)
begin
   {initial user input}
   userInput;

   {if both images are read in succesfully then...}
   if loadImages(leftImageLocation, rightImageLocation) then
   begin
      {generate a product image from the two input images}
      generateProductImage;
      
      {store the product image as a bmp file}
      productImageLocation := currentDate + '_(' + currentTime + ')_ProductImage.bmp';
      storebmpfile(productImageLocation, productImage^);

      {indicate success}
      writeln;
      writeln('Success!');
   end
   {if either/both of the images fail to be read in...}
   else
   begin
      {indicate failure}
      writeln;
      writeln('Failure!');
   end;

   dispose(leftImage);
   dispose(rightImage);
   dispose(productImage);
   dispose(leftImageTemp);
   dispose(rightImageTemp);
end.
(*---------- End of program ----------*)
