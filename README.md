# Finn-Connor-Aidan DevOps Project

## Overview

A Discord Bot that utilizes Docker and AWS to run independently from your 
machine.

## Setup

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
   - In the "Advanced details" dropdown menu, set "IAM Instance Profile" to "LabInstanceProfile" and paste teh contents of userdata.sh from the repository into the section labelled "User data".
   - Click "Launch Instance"
4. Allow the new instance to initialize. This may take a few minutes.
5. To chek if the setup worked, go to Discord and interact with your bot 
using the !help command.

## Tools

- AWS: https://us-east-1.console.aws.amazon.com/console/home
- Redis: https://redis.io/
- JaCoCo Code Coverage: https://github.com/cozyloon/JaCoCo_Code-Coverage
- Docker: https://www.docker.com/get-started/

## Background




### CI Status

![Testing](https://github.com/cs220s25/Finn-Connor-AidanProject/actions/workflows/run_tests.yml/badge.svg)
