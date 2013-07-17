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

(*---------- Global constant declarations ----------*)
{const
   ;}


(*---------- Global type declarations ----------*)
{type
   ;}


(*---------- Global variable declarations ----------*)
var
   {Information for the output product image}
   currentDate, currentTime, productImageLocation : string;
   {Used to get the images}
   leftImageLocation, rightImageLocation	  : string;
   {Pointers to the left, right, and product images}
   leftImage, rightImage, productImage		  : pimage;


(*---------- Functions ----------*)

(*
 *  Function to load in both images.
 *  Returns boolean -- true if images read in properly, false otherwise
 *)
function loadImages:boolean;
begin
   {take a string as source and a pimage to load image}
   if loadbmpfile(leftImageLocation, leftImage)
      and loadbmpfile(rightImageLocation, rightImage) then
   begin
      loadImages := true;
      
      {Indicate that the files were read in}
      writeln('Both image files were successfully read in.');
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
   writeln('/==================================/');
   writeln('/ First step for masters algorithm /');
   writeln('/==================================/');
   writeln;
   writeln('   > Reads in two image files');
   writeln('   > Multiplies the images together to form a product image');
   writeln('   > Applies a correlation function to the product image');
   writeln('   > Applies a convolution to the resulting image');
   writeln('   > Outputs the final image');
   writeln;
   writeln('<Press ENTER to begin the program>');
   readln;
   write('Enter the current date (DD-MM-YY): ');
   readln(currentDate);
   write('Enter the current time (HH-MM): ');
   readln(currentTime);
   write('Enter the file name (with path) for the left image: ');
   readln(leftImageLocation);
   write('Enter the file name (with path) for the right image: ');
   readln(rightImageLocation);
   writeln;
end; { userInput }

(*
 *  Procedure to generate a product image
 *  from the left and right images
 *)
procedure generateProductImage;
var
   {Temporary variables for left and right images}
   leftImageTemp, rightImageTemp : pimage;
begin
   {Initialise temporary image arrays}
   writeln('Initialising temporary arrays...');
   new(leftImageTemp, leftImage ^.maxplane, leftImage ^.maxrow, leftImage ^.maxcol);
   new(rightImageTemp, rightImage ^.maxplane, rightImage ^.maxrow, rightImage ^.maxcol);
   new(productImage, rightImage ^.maxplane, rightImage ^.maxrow, rightImage ^.maxcol);

   {Assign temp images to their corresponding image}
   leftImageTemp^ := leftImage^;
   rightImageTemp^ := rightImage^;

   {Produce product image}
   writeln('Multiplying left and right images together...');
   productImage^ := leftImageTemp^ * rightImageTemp^;

   {Store the product image}
   writeln('Storing product image...');
   productImageLocation := currentDate + '_(' + currentTime + ')_ProductImage.bmp';
   storebmpfile(productImageLocation, productImage^);
   writeln('Product Image stored.');

   {Dispose of the temporary image buffers}
   dispose(leftImageTemp);
   dispose(rightImageTemp);
end; { generateproductimage }


(*---------- Main body of program ----------*)
begin
   {Generate initial user input}
   userInput;

   {Prompt to begin generating product image}
   writeln('<Press ENTER to generate product image>');
   readln;
   
   {If both images are read in succesfully then...}
   if loadImages then
   begin      
      {...generate a product image from the two input images...}
      generateProductImage;
      
      {...store the product image as a bmp file}
      productImageLocation := currentDate + '_(' + currentTime + ')_ProductImage.bmp';
      storebmpfile(productImageLocation, productImage^);
   end
   {If either/both of the images fail to be read in...}
   else
   begin
      {...then indicate failure}
      writeln('**Error: one or both of the images could not be read in or does not exist.**');
   end;

   {Indicate end of the program}
   writeln;
   writeln('**End of program**');
   
   {Dispose of the remaining image buffers after use}
   dispose(leftImage);
   dispose(rightImage);
   dispose(productImage);
end.
(*---------- End of program ----------*)
