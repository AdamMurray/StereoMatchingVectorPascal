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
	       for row := 0 to j-1 do
		  p[n,row] := p[n,row] + r^[n,(row + l - j - 1)];
	       for row := p.maxrow
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
   new(f, K.rows, K.cols);
   f^ := nil;
   new(flags, K.rows, K.cols);
   flags^ := false;

   for i := 1 to K.rows do
      for j := 1 to K.cols do
      else f^[i, j] := pm(i, j);
   
end; { genconv }
 