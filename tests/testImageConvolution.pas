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

(*---------- Global type declarations ----------*)
type
   {Array type to store premultiplication values}
   premult(rows,cols:integer) = array[1..rows, 1..cols] of pimage;
   {Array type to... }
   tflag(rows,cols:integer)   = array[1..rows, 1..cols] of boolean;

(*---------- Global variable declarations ----------*)
var
   {Input image}
   imageIn, imageInTemp : pimage;
   {Kernel}
   Kernel : ^matrix;
   {For kernel iteration}
   ki, kj : integer;
   {A pointer to the premultiplication array}
   f	      : ^premult;
   a, b, i, j : integer;
   {A pointer to the flags array}
   flags      : ^tflag;

(*---------- Procedures ----------*)
(* Procedure to apply a convolution to an image.
 * The convolution matrix can be of any general form.
 *
 * Params: p -- image
 *         K -- convolution matrix/kernel
 *)
procedure genconv(var p	: image; var K : matrix);

   (* Function that checks for duplicate kernel elements *)
   function dup(i, j : integer):boolean;
   var
      c, d, l, m : integer;
      b		 : boolean;
   begin
      {Set c to the number of columns}
      c := K.cols;
      
      d := j + i * c;
      b := false;
      for l := 1 to c do
	 for m := 1 to K.rows do {uncertain if upper or lower case K/k here}
	    b := b or (K[i, j] = K[m, l]) and (m + c * l < d);
      dup := b;
      {dup:=\or\or((K[i,j]=K)and(iota 1+c*iota 0<d));}
   end; { dup }

   (* Function to find a previous instance of a kernel element *)
   function prev(i, j : integer):pimage;
   var
      m, n : integer;
      s	   : real;
   begin
      s := K[i,j];
      for m := 1 to i - 1 do
	 for n := 1 to K.cols do
	    if K[m,n] = s then
	       prev := f^[m,n];
      for n := 1 to j - 1 do
	 if K[i,n] = s then
	    prev := f^[i,n];
   end; { prev }

   (* Function to handle premultiplication *)
   function pm(i, j : integer):pimage;
   var
      x	: pimage;
   begin
      {Create space for x on the heap, initialising
      its size with the number of planes, rows, and
      columns of the input image p}
      new(x, p.maxplane, p.maxrow, p.maxcol);
      {Adjusts the contrast of image p by a factor given
      by the kernel K and stores the result in x}
      adjustcontrast(K[i,j], p, x^);
      {Sets the value of the flags array to true}
      flags^[i,j] := true;
      {Returns x}
      pm := x;
   end; { pm }

   (* Procedure to handle edge processing *)
   procedure doedges;
   var
      i,j,l,m,n,row,col	: integer;
      r			: pimage;
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
      for n := 0 to p.maxplane do
	 for l := 1 to K.rows do
	    for m := 1 to K.cols do
	    begin
	       r := f^[l,m];
	       for row := 0 to j - 1 do
		  p[n,row] := p[n,row] + r^[n,(row + l - j - 1)];
	       for row := j + 1 to p.maxrow do
		  p[n,row] := p[n,row] + r^[n,(row + l - j - 1)];
	       for col := 0 to i - 1 do
		  for row := 0 to p.maxrow do
		  begin
		     p[n,row,col] := r^[n,row,(col + 1 + m - i)] + p[n,row,col];
		  end;
	       for col := 1 + p.maxcol - i to p.maxcol do
		  for row := 0 to p.maxrow do
		  begin
		     p[n,row,col] := p[n,row,col]{ +  r^[n]};
		  end;
	       {$r+}
	    end;
   end; { doedges }

   (* Function to handle the release of temporary store *)
   procedure freestore;
   var
      i,j : integer;
   begin
      for i := 1 to K.rows do
	 for j := 1 to K.cols do
	    if flags^[i,j]
	       then dispose(f^[i,j]);
   end; { freestore }

begin
   {Create space for f on the heap, initialising
   its size with the rows and columns of the
   convolution matrix}
   new(f, K.rows, K.cols);
   {Initialise the elements of the array
   pointed at by f to be initially nil}
   f^ := nil;

   {Create space for flags on the heap, initialising
   its size with the rows and columns of the
   convolution matrix}
   new(flags, K.rows, K.cols);
   {Initialise the elements of the array
   pointed at by flags to be initially false}
   flags^ := false;

   {Iterate over the rows and columns of f, and set
   each element to the value found by calling the
   premultiplication function (pm)}
   for i := 1 to K.rows do
      for j := 1 to K.cols do
	 f^[i, j] := pm(i, j);

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
	 p[][a..p.maxrow - a, b..p.maxcol - b] + f^[i,j] ^[iota 0, i + iota 1 - a, j + iota 2 - b];

   {Handles edge processing}
   doedges;

   {Handles the release of temporary storage}
   freestore;
end; { genconv }

(*---------- Main body of the program ---------- *)
begin
   writeln('/=========================/');
   writeln('/ Test: Image Convolution /');
   writeln('/=========================/');
   writeln;

   {Read in image}
   if loadbmpfile('../images/Left1.bmp', imageIn) then
   begin
      writeln('Creating space for temporary image...');
      new(imageInTemp, imageIn ^.maxplane, imageIn ^.maxrow, imageIn ^.maxcol);

      {Assign temp image to the corresponding input image}
      writeln('Initialising temp image...');
      imageInTemp^ := imageIn^;
      writeln('Temp image initialised.');

      {Initialise the kernel}
      writeln('Initilising the kernel...');
      for ki := 0 to 2 do
	 for kj := 0 to 2 do
	 begin
	    write('Input the value of the kernel at position [' + ki + ',' + kj + ']: ');
	    readln(Kernel^[ki,kj]);
	 end;
      writeln('Kernel initialised.');
      
      {Apply general convolution}
      writeln('Applying convolution...');
      genconv(imageInTemp^,  Kernel^);
      writeln('Convolution applied.');

      {Store the convolved image}
      writeln('Storing image...');
      storebmpfile('convolvedImage.bmp', imageInTemp^);
      writeln('Image stored.');

      {Dispose of temporary storage}
      writeln('Disposing of temporary storage...');
      dispose(imageIn);
      dispose(imageInTemp);
      writeln('Temporary storage disposed.');
   end;
end.
(*---------- End of program ---------*)