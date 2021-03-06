(*
 *  Test: Image convolution.
 *
 *  This program is a test for the ability
 *  to correctly apply a convolution matrix
 *  to an image (namely the product image
 *  obtained through the multiplication of
 *  left and right images in a stereo pair).
 *)
program testImageConvolution;

(*---------- Uses declarations ----------*)
uses bmp;

(*---------- Global constant declarations ----------*)
const
   kernelBounds	= 3;

(*---------- Global type declarations ----------*)
type
   {Array type to store premultiplication values}
   preMultiplicationArray(rows,cols:integer) = array[1..rows, 1..cols] of pimage;
   {Array type in which the element [i,j] is true if element [i,j] of the
   premultiplication array holds the first pointer to a premultiplied image}
   flagsArray(rows,cols:integer)   = array[1..rows, 1..cols] of boolean;

(*---------- Global variable declarations ----------*)
var
   {Input image and temporary storage for input image}
   imageIn, imageInTemp : pimage;
   {Kernel/Convolution matrix}
   kernel : ^matrix;
   {Parameters for iterating over the kernel}
   ki, kj : integer;
   {A pointer to the premultiplication array}
   preMultPointer	      : ^preMultiplicationArray;
   a, b, i, j : integer;
   {A pointer to the flags array}
   flagsArrayPointer      : ^flagsArray;

(*---------- Procedures ----------*)
(* Procedure to apply a convolution to an image.
 * The convolution matrix can be of any general form.
 *
 * Params: p -- image
 *         K -- convolution matrix/kernel
 *)
procedure applyConvolution(var p : image; var K : matrix);

   (* Function that checks for duplicate kernel elements *)
   function duplicateKernalElemCheck(i, j : integer):boolean;
   var
      c, d, l, m : integer;
      b		 : boolean;
   begin
      {Set c to the number of columns}
      c := K.cols;
      
      d := j + i * c;
      b := false;
      for l := 1 to c do
	 for m := 1 to K.rows do
	    b := b or (K[i, j] = K[m, l]) and (m + c * l < d);
      duplicateKernalElemCheck := b;
      {duplicateKernalElemCheck:=\or\or((K[i,j]=K)and(iota 1+c*iota 0<d));}
   end; { duplicateKernalElemCheck }

   (* Function to find a previous instance of a kernel element *)
   function prevKernelElemCheck(i, j : integer):pimage;
   var
      m, n : integer;
      s	   : real;
   begin
      s := K[i,j];
      for m := 1 to i - 1 do
	 for n := 1 to K.cols do
	    if K[m,n] = s then
	       prevKernelElemCheck := preMultPointer^[m,n];
      for n := 1 to j - 1 do
	 if K[i,n] = s then
	    prevKernelElemCheck := preMultPointer^[i,n];
   end; { prevKernelElemCheck }

   (* Function to handle premultiplication *)
   function preMultiplication(i, j : integer):pimage;
   var
      x	: pimage;
   begin
      {Create space for x on the heap, initialising
      its size with the number of planes, rows, and
      columns of the input image p}
      new(x, p.maxplane, p.maxrow, p.maxcol);
      
      {Adjust the contrast of image p by a factor given
      by the kernel K and store the result in x}
      adjustcontrast(K[i,j], p, x^);
      
      {Set the value of the flags array to true}
      flagsArrayPointer^[i,j] := true;
      
      {Return x}
      preMultiplication := x;
   end; { preMultiplication }

   (* Procedure to handle edge processing *)
   procedure handleEdges;
   var
      i, j, l, m, n, row, col : integer;
      r			      : pimage;
   begin
      {Set j to half the number of rows in the kernel}
      j := (K.rows) div 2;
      {Set i to half the number of columns in the kernel}
      i := (K.cols) div 2;
      
      {Initialise the edge rows and columns of p based
      on the size of the kernel, as these will be the
      focus of edge handling}
      p[][0..j-1] := 0;
      p[][][0..i-1] := 0;
      p[][1 + p.maxrow - j..p.maxrow] := 0;
      p[][][1 + p.maxcol - i..p.maxcol] := 0;
      for n := 1 to p.maxplane do
	 for l := 1 to k.rows do
	    for m := 1 to k.cols do
	    begin
	       r := preMultPointer^[l,m];
	       for row := 0 to j - 1 do
		  p[n,row] := p[n,row] + r^[n,(row + l - j - 1)];
	       for row := p.maxrow + j + 1 to p.maxrow do
		  p[n,row] := p[n,row] + r^[n,(row + l - j - 1)];
	       for col := 0 to i - 1 do
		  for row := 0 to p.maxrow do
		  begin
		     p[n,row,col] := r^[n,row,(col + 1 + m - i)] + p[n,row,col];
		  end;
	       for col := 1 + p.maxcol - i to p.maxcol do
		  for row := 0 to p.maxrow do
		  begin
		     p[n,row,col] := p[n,row,col] + r^[n];
		  end;
	       {$r+}
	    end;
   end; { handleEdges }

   (* Procedure to handle the release of temporary store *)
   procedure freeStorage;
   var
      i, j : integer;
   begin
      for i := 1 to K.rows do
	 for j := 1 to K.cols do
	    {if there exists a pointer to a premultiplied
	    image then dispose of that pointer}
	    if flagsArrayPointer^[i,j]
	       then dispose(preMultPointer^[i,j]);
   end; { freeStorage }

begin
   {Create space for premultiplicationarray on the heap,
   initialising its size with the rows and columns of the
   convolution matrix}
   new(preMultPointer, K.rows, K.cols);
   {Initialise the elements of the premultiplication
   array to be initially nil}
   preMultPointer^ := nil;

   {Create space for flags array on the heap, initialising
   its size with the rows and columns of the
   convolution matrix}
   new(flagsArrayPointer, K.rows, K.cols);
   {Initialise the elements of the
   flags array to be initially false}
   flagsArrayPointer^ := false;

   {Iterate over the rows and columns of the premultiplication array,
   and set each element to the value found by calling the
   premultiplication function (preMultiplication)}
   for i := 1 to K.rows do
      for j := 1 to K.cols do
	 preMultPointer^[i, j] := preMultiplication(i, j);
   
   {Initialise a and b which store the steps
   away from the centre of the kernel}
   a := (K.rows) div 2;
   b := (K.cols) div 2;
   {Initialise the rows and columns of the image p
   which do not require edge handling to zero}
   p[][a..p.maxrow - a, b..p.maxcol - b] := 0;

   {Iterate over the rows and columns of the kernel}
   for i := 1 to K.rows do
      for j := 1 to K.cols do
	 p[][a..p.maxrow - a, b..p.maxcol - b] :=
	 p[][a..p.maxrow - a, b..p.maxcol - b] + preMultPointer^[i,j] ^[iota 0, i + iota 1 - a, j + iota 2 - b];

   {Handles edge processing}
   handleEdges;

   {Handles the release of temporary storage}
   freeStorage;
end; { applyConvolution }


(*---------- Main body of the program ---------- *)
begin
   writeln('/=========================/');
   writeln('/ Test: Image Convolution /');
   writeln('/=========================/');
   writeln;

   {Read in image}
   writeln('Attempting to read in image to be convolved...');
   if loadbmpfile('../images/Left1.bmp', imageIn) then
   begin
      writeln('Image read in successfully.');
      writeln('Attempting to create space for image...');
      new(imageInTemp, imageIn ^.maxplane, imageIn ^.maxrow, imageIn ^.maxcol);
      writeln('Space for image created. Seg fault avoidance achieved.');

      {Assign temp image to the corresponding input image}
      writeln('Attempting to initialise temporary image with input image data...');
      imageInTemp^ := imageIn^;
      writeln('Temporary image initialised.');

      {Create space for the kernel on the heap}
      new(kernel, kernelBounds, kernelBounds);

      {Initialise the kernel}
      writeln('Attempting to initialise the kernel...');
      {for ki := 0 to 2 do
	 for kj := 0 to 2 do
	 begin
	    write('Input the value of the kernel at position [' + ki + ',' + kj + ']: ');
	    readln(kernel^[ki,kj]);
	 end;}
      kernel^ := -1;
      writeln('Kernel initialised.');
      writeln(kernel^);
      
      {Apply general convolution}
      writeln('Attempting to apply convolution...');
      applyConvolution(imageInTemp^,  kernel^);
      writeln('Convolution applied.');

      {Store the convolved image}
      writeln('Attempting to store image...');
      storebmpfile('convolvedImage.bmp', imageInTemp^);
      writeln('Image stored.');

      {Dispose of temporary storage}
      writeln('Disposing of temporary storage...');
      dispose(imageIn);
      dispose(imageInTemp);
      writeln('Temporary storage disposed.');
   end
   else
      writeln('ERROR: Image could not be read in or does not exist.');
end.
(*---------- End of program ---------*)