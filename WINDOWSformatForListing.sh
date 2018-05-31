cd /C/Users/David\ Rohrbaugh/FTC9899_app/TeamCode/src/main/java/org/firstinspires/ftc/teamcode
rm -rf TXT
mkdir TXT
mkdir TEMP_FOLDER
for file in `ls *.java`
do
grep -v "Util.log" $file > TEMP_FOLDER/$file.txt
grep -v "telemetry.addData" TEMP_FOLDER/$file.txt > TXT/$file.txt
done
cd RelicRecovery
for file2 in `ls *.java`
do
grep -v "Util.log" $file2 > ../TEMP_FOLDER/$file2.txt
grep -v "telemetry.addData" ../TEMP_FOLDER/$file2.txt > ../TXT/$file2.txt
done
cd ..
rm -rf TEMP_FOLDER
#rm -r /C/Users/David\ Rohrbaugh/FTC9899_app/TXT
#rm TXT/package-info.java.txt
