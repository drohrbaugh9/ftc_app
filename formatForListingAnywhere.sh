if [ "$1" = "" ]
then
  echo "No path specified, exiting..."
  exit
#elif [ "rev<<<$1 | cut -c-8 | rev" != teamcode ]
#then
#  echo "Path must be to your teamcode (LOWERCASE \"T\") directory"
#  exit
fi

cd $1
rm -rf TXT
mkdir TXT
mkdir TEMP_FOLDER
for file in `ls *.java`
do
grep -v "Util.log" $file > TEMP_FOLDER/$file.txt
grep -v "telemetry.addData" TEMP_FOLDER/$file.txt > TXT/$file.txt
done
rm -rf TEMP_FOLDER
#rm TXT/package-info.java.txt
