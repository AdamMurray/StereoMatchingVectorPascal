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
   premult(rows,cols:integer) = array[1..rows, 1..cols] of pimage;
   tflag(rows,cols:integer)    =  array[1..rows, 1..cols] of boolean;

(*---------- Global variable declarations ----------*)
var
   f	      : ^premult;
   a, b, i, j : integer;
   flags      : ^tflag;

(*---------- Procedures ----------*)
procedure genconv(var p	: images; var K : matrix);

   (* Function that checks for duplicate kernel elements *)
   function dup(i, j : integer):boolean;
   var
      c, d, l, m : integer;
      b		 : boolean;
   begin
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
   function prev(i, j : integer)pimage;
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
      new(x, p.maxplane, p.maxrow, p.maxcol);
      adjustcontrast(K[i,j], p, x^);
      flags^[i,j] := true;
      pm := x;
   end; { pm }

   (* Procedure to handle edge processing *)
   procedure doedges;
   var
      i,j,l,m,n,row,col	: integer;
      r			: pimage;
   begin
      j := K.rows/2;
      i := K.cols/2;
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
		     p[n,row,col] := p[n,row,col] + r^[n];
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
   a := (K.rows)/2;
   b := (K.cols)/2;
   {Initialise the rows and columns of the image p
   which do not require edge handling to zero}
   p[][a..p.maxrow - a, b..p.maxcol - b] := 0;

   {Iterate over the rows and columns of the kernel}
   for i := 1 to K.rows do
      for j := 1 to K.cols do
	 p[][a..p.maxrow - a, b..p.maxcol - b] :=
	 p[][a..p.maxrow - a, b..p.maxcol - b] + f^[i,j] ^[iota 0, i + iota 1 - a, j + iota 2 - b];

   doedges;

   freestore;
end; { genconv }
 