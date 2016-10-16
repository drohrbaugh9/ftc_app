cd /home/owner/Android/Qualcom-test/ftc_app/TeamCode/src/main/java/org/firstinspires/ftc/teamcode
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
