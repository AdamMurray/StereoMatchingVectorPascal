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
{type
   ;}


(*---------- Variable declarations ----------*)
var
   leftimagein, rightimagein	       : string;
   leftimage, rightimage, productimage : pimage;


(*---------- Functions ----------*)

(*
 *  Function to load in both images.
 *  Returns boolean -- true if images read in properly, false otherwise
 *)
function loadimages(limagein, rimagein : string):boolean;
begin
   {take a string as source and a pimage (from bmp) to load image}
   if loadbmpfile(leftimagein, leftimage)
      and loadbmpfile(rightimagein, rightimage) then
   begin
      loadimages := true;
   end
   else
   begin
      loadimages := false;
   end;
end; { loadimages }


(*---------- Procedures ----------*)

(*
 *  Procedure to generate a product image
 *  from the left and right images
 *)
procedure generateproductimage;
begin
   new(leftimage, rightimage ^.maxplane, rightimage ^.maxrow, rightimage ^.maxcol);
   new(productimage, rightimage ^.maxplane, rightimage ^.maxrow, rightimage ^.maxcol);
   
   {productimage^ := rightimage^;}
   productimage^ := leftimage^ * rightimage^;
end; { generateproductimage }


(*---------- Main body of program ----------*)
begin
   writeln('// First step for masters algorithm');
   writeln('// Reads in two images and applies a correlation');
   writeln;
   write('Enter the file name (with path) for the left image: ');
   readln(leftimagein);
   write('Enter the file name (with path) for the right image: ');
   readln(rightimagein);
   
   if loadimages(leftimagein, rightimagein) then
   begin
      generateproductimage;
      storebmpfile('productimage.bmp', productimage^);
      
      writeln;
      writeln('Success!');
   end
   else
   begin
      writeln;
      writeln('Failure!');
   end;
end.
(*---------- End of program ----------*)
