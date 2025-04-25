# Finn-Connor-Aidan DevOps Project

## Overview

A Discord Bot that utilizes Docker and AWS to run independently from your 
machine.

## Setup

 1. Launch AWS. It may take some time for AWS to finish launching.
 2. In AWS, create a new secret in the AWS Secrets Manager:
	2a. Select "Store a new secret" in the Secrets tab
	2b. Set the Secret Type to "Other type of secret"
	2c. Under Key/Value Pairs, in the left box (key), put "DISCORD_TOKEN", 
and in the right box (value), paste the Discord token of your Discord bot
	2d. Click "Next"
	2e. In the next menu, under "Secret Name", enter "220_Discord_Token"
	2f. Click "Next" through the remaining menus, then click "Store"
	2g. Reload the page to see the new secret
 3. In AWS, create an EC2 instance:
	3a. Select "Launch Instance" from the Instances tab
	3b. Select your key pair
	3c. Check the box labelled "Allow HTTP traffic from the internet"
	3d. In the "Advanced details" dropdown menu, set "IAM Instance 
Profile" to "LabInstanceProfile" and paste teh contents of userdata.sh 
from the repository into the section labelled "User data".
	3e. Click "Launch Instance"
 4. Allow the new instance to initialize. This may take a few minutes.
 5. To chek if the setup worked, go to Discord and interact with your bot 
using the !help command.

## Tools



## Background




### CI Status

![Testing](https://github.com/cs220s25/Finn-Connor-AidanProject/actions/workflows/run_tests.yml/badge.svg)
