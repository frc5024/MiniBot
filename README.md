# MiniBot [![Build Status](https://travis-ci.org/frc5024/MiniBot.svg?branch=master)](https://travis-ci.org/frc5024/MiniBot)
This is the code behind MiniBot for the 2019 offseason.

## Installation
This code can be "installed" in one of two ways. First, is via an official release:

These are the steps:
 - Get the latest release from github
 - Unpack the compressed archive
 - Open it as a folder in vscode
 - Set your team number in wpilib
 - Deploy using wpilib

The second way is to use rolling release / waterfall. Just do the steps above, but use git to clone the master branch instead of grabbing the latest release.

NOTE: 5024 members should always use the second method.

## Generating documentation
### Writing comments
To add comments that are pushed to the website, follow the [javadoc](https://en.wikipedia.org/wiki/Javadoc) guidelines.

### Deploying to the website
To build the documentation, make sure you have doxygen installed, then run:

```
doxygen ./doxygen.config 
```

The documentation will the be automattically pushed to the website for this repo.

5024 members should not worry about this because the documentation is automatically deployed on every push, and once per day by travis-ci.

## Pull requests
Pull requests is our prefered method of merging code. For a PR to be merged, it must:
 - Pass style checking
 - Be approved by a mentor or team lead
 - Compile without any errors or warnings

## Deployment
When deploying code to our robots we have a problem where, on the first few tries, the deployment fails due to not being able to find the RIO. Just keep trying, and it will eventually work.

## The team
This codebase is built and tested by the [5024 programming team](https://github.com/frc5024) with help from our mentors:
 - [@johnlownie](https://github.com/johnlownie)
 - [@LordMichaelmort](https://github.com/LordMichaelmort)
 - [@awpratten](https://github.com/awpratten)