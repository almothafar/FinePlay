for /F %%f in ('dir /AD /B /B target') do rd /S /Q target\%%f
for /F %%f in ('dir /AD /B /B logs') do rd /S /Q logs\%%f
for /F %%f in ('dir /AD /B /B bin') do rd /S /Q bin\%%f
for /F %%f in ('dir /AD /B /B generate') do rd /S /Q generate\%%f
copy /Y nul generate\.empty

for /F %%f in ('dir /AD /B /B document\tabledef_prod') do rd /S /Q document\tabledef_prod\%%f
for /F %%f in ('dir /AD /B /B document\doxygen') do rd /S /Q document\doxygen\%%f
rd /S /Q project\project
rd /S /Q project\target
del /AH /S Thumbs.db
rd /S /Q target\.history
