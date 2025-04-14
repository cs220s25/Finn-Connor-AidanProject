cd /home/ec2-user/Finn-Connor-AidanProject
sudo systemctl stop discord_bot
sudo git pull origin main
sudo mvn clean package -DskipTests
sudo systemctl start discord_bot
