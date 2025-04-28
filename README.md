# Finn-Connor-Aidan DevOps Project

## Overview

A Discord Bot that utilizes Docker and AWS to run independently from your
machine. The bot is a text-based adventure game. The player is trapped in a mysterious mansion and must escape. The game starts with the player receiving a description of the starting location. The player can then use various commands to interact with the game, such as moving to different locations, picking up items, and using items to solve the puzzle. The goal is to find the needed puzzle pieces to unlock the exit door and escape the mansion.

## System Diagram

![UML Diagram](images/UML%20diagram.png)

## Setup

**Local Setup without AWS (requires Discord bot token):**
- Clone the repo.
- Make sure you have java 21 and maven installed.
- In the root directory of the repo, add a file named '.env' with the following content:
   ```
   DISCORD_TOKEN=your_discord_token_here
   ```
   Replace `your_discord_token_here` with your actual Discord bot token.
- In the root directory of the repo, run `mvn clean package` to build the project.
- Run the command `java -jar target/dbot-1.0-SNAPSHOT-jar-with-dependencies.jar` to start the bot.

**To Manually run with Docker:**
- Make sure you have Docker installed.
- The `aws.env` file should have the following content:
   ```
   AWS_ACCESS_KEY_ID=YOUR_ACCESS_KEY_HERE
   AWS_SECRET_ACCESS_KEY=YOUR_SECRET_KEY_HERE
   AWS_SESSION_TOKEN=YOUR_SESSION_TOKEN_HERE
   ```
   You can get these from AWS Details in Learner Lab.
- In the root directory of the repo, run the following commands:
   ```
   docker build -t discord-bot .
   ```
   ```
   docker run -d --name redis -p 6379:6379 redis:alpine
   ```
   ```
   docker run -d --name discord-bot --env-file aws.env -e REDIS_HOST=host.docker.internal -e REDIS_PORT=6379 discord-bot
   ```
- To check if the containers are running, run the command:
   ```
   docker ps
   ```
  You should see two containers: one for the bot and one for Redis.
- To stop the containers, run the commands:
   ```
   docker stop discord-bot
   ```
   ```
   docker stop redis
   ```

**AWS Setup (requires Discord bot token):**
1. Launch AWS. It may take some time for AWS to finish launching.
2. In AWS, create a new secret in the AWS Secrets Manager:
  - Select "Store a new secret" in the Secrets tab
  - Set the Secret Type to "Other type of secret"
  - Under Key/Value Pairs, in the left box (key), put "DISCORD_TOKEN", and in the right box (value), paste the Discord token of your Discord bot
  - Click "Next"
  - In the next menu, under "Secret Name", enter "220_Discord_Token"
  - Click "Next" through the remaining menus, then click "Store"
  - Reload the page to see the new secret
3. In AWS, create an EC2 instance:
  - Select "Launch Instance" from the Instances tab
  - Select your key pair
  - Check the box labelled "Allow HTTP traffic from the internet"
  - In the "Advanced details" dropdown menu, set "IAM Instance Profile" to "LabInstanceProfile" and paste the contents of userdata.sh from the repository into the section labelled "User data".
  - Click "Launch Instance"
4. Allow the new instance to initialize. This may take a few minutes.
5. To check if the setup worked, go to Discord and interact with your bot
   using the !help command.

## Tools

- AWS: https://us-east-1.console.aws.amazon.com/console/home
- Redis: https://redis.io/
- JaCoCo Code Coverage: https://github.com/cozyloon/JaCoCo_Code-Coverage
- Docker: https://www.docker.com/get-started/
- Docker Compose: https://docs.docker.com/compose/

## Background

- The Following links were used to help setup the Jacoco coverage report:

https://github.com/cozyloon/JaCoCo_Code-Coverage

https://github.com/marketplace/actions/jacoco-report


### CI Status

![Testing](https://github.com/cs220s25/Finn-Connor-AidanProject/actions/workflows/run_tests.yml/badge.svg)
