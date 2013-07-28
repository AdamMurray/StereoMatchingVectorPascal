program masters_project_main;

uses bmp;

var
   leftImageFilePath, rightImageFilePath, diffImageFilePath : string;
   leftImage, rightImage, diffImage			    : pimage;

function loadImages:boolean;
begin
   if loadbmpfile(leftImageFilePath, leftImage)
      and loadbmpfile(rightImageFilePath, rightImage) then
   begin
      loadImages := true;
      writeln('Both images successfully read in.');
   end
   else
   begin
      loadImages := false;
   end;
end; { loadImages }

procedure generateDiffImage;
var
   leftImageTemp, rightImageTemp : pimage;
begin
   new(leftImageTemp, leftImage ^.maxplane, leftImage ^.maxrow, leftImage ^.maxcol);
   new(rightImageTemp, rightImage ^.maxplane, rightImage ^.maxrow, rightImage ^.maxcol);
   new(diffImage, rightImage ^.maxplane, rightImage ^.maxrow, rightImage ^.maxcol);

   leftImageTemp^ := leftImage^;
   rightImageTemp^ := rightImage^;

   diffImage^ := leftImageTemp^ - rightImageTemp^;
   diffImageFilePath := ParamStr(3);
   storebmpfile(diffImageFilePath, diffImage^);

   dispose(leftImageTemp);
   dispose(rightImageTemp);
end;

begin
   leftImageFilePath := ParamStr(1);
   rightImageFilePath := ParamStr(2);

   if loadImages then
   begin
      generateDiffImage;
   end
   else
   begin
      writeln('ERROR: one or both images could not be read in');
   end;

   dispose(leftImage);
   dispose(rightImage);
   dispose(productImage);
end.