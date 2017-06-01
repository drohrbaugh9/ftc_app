#sudo apt-get install android-tools-adb
#echo -e "\n----adb installation complete----\n"
if [ "$1" = "" ]
then
  IP="192.168.49.1"
  echo "No IP specified, Using default IP: $IP"
else
  echo "Using specified IP: $1"
  IP=$1
fi
#echo "Make sure you have the right IP"
adb kill-server
echo -n -e "Press [Enter] to run \x1b[0;34madb tcpip 5555\x1b[0m"
read -p ""
adb tcpip 5555
echo -n -e "Press [Enter] to run \x1b[0;34madb connect $IP\x1b[0m"
read -p ""
adb connect $IP
echo -e "If you are disconnected for a short time, run \x1b[0;34madb connect $IP\x1b[0m.\nOtherwise, run this script again."
