#!/bin/bash
# Install Docker
sudo yum install -y docker
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -a -G docker ec2-user

# Install docker compose plugin
sudo mkdir -p /usr/local/lib/docker/cli-plugins
sudo curl -SL https://github.com/docker/compose/releases/download/v2.35.1/docker-compose-linux-x86_64 -o /usr/local/lib/docker/cli-plugins/docker-compose
sudo chmod +x /usr/local/lib/docker/cli-plugins/docker-compose

# Install git
sudo yum install -y git

# Clone the repo
cd /home/ec2-user
git clone https://github.com/cs220s25/Finn-Connor-AidanProject.git
cd Finn-Connor-AidanProject/

# Build and run the docker containers
sudo docker compose up -d
