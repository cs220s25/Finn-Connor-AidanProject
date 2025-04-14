sudo git pull origin main
sudo mvn clean package -DskipTests
sudo systemctl restart discord_bot
