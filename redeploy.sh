cd /home/ec2-user/Finn-Connor-AidanProject
sudo git pull origin main
mvn clean package -DskipTests
sudo systemctl restart discord_bot
