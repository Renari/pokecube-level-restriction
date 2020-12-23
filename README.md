# SampleAddon

## General Notes:

The instructions here which reference a `runSetup.bat` are intended for use with Eclipse, if you use something else, the Readme.txt might help with setup instructions.

## Setting up.

See the Readme.txt for the official Forge instructions on setting up a development environment. The build.gradle included here is configured to also include adding pokecube-aio to the dependencies for the setup.

## Updating the pokecube dependancy version.

Note, these steps are only needed if the default settings fail to work. By default, it should install the latest release version of pokecube into the development environment when the `runSetup.bat` is executed.

### Step 1: Obtaining the updated deobf jar
First you must obtain the updated deobf jar, there are 2 ways to do this, listed below. Once the jar is obtained, place it in the libs directory in your repository.

#### Method 1: Build your own version of Pokecube AIO - deobf

To do this, first you need to download the repository for Pokeube-AIO, which is located here: https://github.com/Pokecube-Development/Pokecube-Issues-and-Wiki
Next, run the build.bat (may require running runSetup.bat first).
Once this finishes running, there will be a usable pokecube-aio-<version>-deobf.jar located on /build/libs/
  
#### Method 2: Find a copy of it elsewhere.

Sometimes the deobf jar will be uploaded as an additional file to the main jar, if this is done, you can download the deobf jar from there and use it.

### Step 2: Updating the build.gradle to use this jar

In the build.gradle, there is a section `dependencies`, which should have a line similar to `implementation "libs:pokecube-aio-1.16.4-3.1.2-deobf"`, replace the part after `libs:`, with whatever the name of the deobf jar you located was.

### Step 3: Updating the environment

Re-running the runSetup.bat should update things at this step. If you are using eclipse, you may need to remove and then re-add the project for it to properly reflect the changes to the dependencies.
