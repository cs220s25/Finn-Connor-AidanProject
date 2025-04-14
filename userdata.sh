#!/bin/bash
sudo yum install -y maven-amazon-corretto21
sudo yum install -y git
cd /home/ec2-user
git clone https://github.com/cs220s25/Finn-Connor-AidanProject.git
cd Finn-Connor-AidanProject/
mvn clean package  
sudo cp discord_bot.service /etc/systemd/system
sudo systemctl enable discord_bot.service
sudo systemctl start discord_bot.service
