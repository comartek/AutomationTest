
1. Download jdk1.8 and config JAVA_HOME:
  Install jdk 1.8:
  https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
  Configure JAVA_HOME
      open ~/.zshrc
      export $JAVA_HOME =[FOLDERNAME]/Contents/Home
      export PATH=$JAVA_HOME/bin:$PATH
  example:
      export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_261.jdk/Contents/Home
      export PATH=$JAVA_HOME/bin:$PATH
      source ~/.zshrc
  NOTE: check folder contains setup java: cd  /Library/Java/JavaVirtualMachines
  Now open a second terminal and run
      java -version
      echo $JAVA_HOME

2. Configure maven
  Download the latest version of Maven
  https://maven.apache.org/download.cgi
  apache-maven-3.6.3-bin.zip as of this writing
  Unzip the archive and place the Maven folder somewhere on your computer
      ex: /Users/user01/Downloads/apache-maven-3.6.3
      Add the following environment variables:
      export  M2_HOME=/Users/user01/Downloads/apache-maven-3.6.3
      export M2=${M2_HOME}/bin
      export PATH=${PATH}:${M2_HOME}/bin
      export PATH
  Add the M2 environment variables to your Path
  Ensure that the JAVA_HOME environment variable exists and is set to the location of your JDK
  Ensure that $JAVA_HOME/bin has been added to your Path
  Verify Maven is installed by typing mvn --version in the command prompt.
  Link video configure: https://www.youtube.com/watch?v=EoXImdzlAls
  NOTE: when running: mvn --version fails. We will change the location of the maven directory, and try the setup again, then run the mvn --version command again.

3. Installing Appium
  3.1. Appium Desktop: We download appium desktop from the address:
    https://github.com/appium/appium-desktop/releases
  3.2.Appium server:
    sudo npm install -g appium@1.21.0 --unsafe-perm=true --allow-root
  3.3.Install WebDriverAgent to devices
    Follow this instruction to set up an Apple developer account and install WebDriverAgent to the iOS device:
    http://appium.io/docs/en/drivers/ios-xcuitest-real-devices/#basic-manual-configuration
    Find out where your Appium installation is:
      - Appium server
          $ which appium
    Given this installation location /usr/local/bin/appium , WebDriverAgent project will be found in /usr/local/lib/node_modules/appium/node_modules/appium-webdriveragent.
      - Appium Desktop
          open  folder appium-webdriveragent :
              /Applications/Appium.app/Contents/Resources/app/node_modules/appium/node_modules/appium-webdriveragent
    Open a terminal and go to that location, then run the following to set the project up:
      mkdir -p Resources/WebDriverAgent.bundle
      ./Scripts/bootstrap.sh -d
    NOTE: With version 1.21.0 or later of appium, there is no need to run the command: ./Scripts/bootstrap.sh -d
    Open WebDriverAgent.xcodeproj in Xcode. For both the WebDriverAgentLib and WebDriverAgentRunner targets, select "Automatically manage signing" in the "General" tab, and then select your Development Team. This should also auto-select Signing Certificate. The outcome should look as shown below.
    Xcode may fail to create a provisioning profile for the WebDriverAgentRunner target:
    This necessitates manually changing the bundle id for the target by going into the "Build Settings" tab and changing the "Product Bundle Identifier" from com.facebook.WebDriverAgentRunner to something that Xcode will accept:
    Going back to the "Signing & Capabilities" tab for the WebDriverAgentRunner target, you should now see that it has created a provisioning profile and all is well:
    Finally, you can verify that everything works. Build the project:
    After build success, the WDA will be displayed on the device:
  3.4.Connect Appium
    Start Appium Desktop with the default configuration
    Click on the button “Start Inspector Session”
    Setup new device information corresponding with a mobile device
      ex :
      {
        "platformName": "Android",
        "platformVersion": "9",
        "deviceName": "2732a41ddd217ece",
        "automationName": "UiAutomator2",
        "appPackage": "com.demorosyv2.dev",
        "appActivity": "com.demorosyv2.MainActivity"
      }
  Start session to make sure that the device information is right. If connected successfully, Appium Desktop will open a new window:

4. Connect devices
  4.1.Connect iOS devices
    How to get the device information?
    For iOS, we need to know the deviceName, udid, and PlatformVersion of devices.
    The platform version is the ios version of the device. We can get it from the Setting of the device.
    To get the deviceName and udid, we will use “Xcode”:
    open Xcode and go to Window => Devices and Simulators
    In the Devices and Simulators section, we will see device name, version, udid
  4.2.Connect Android device
    For Android, we need to know the deviceName, udid, and PlatformVersion of devices.
    The platform version is the android version of the device. We can get it from the Setting of the device.
    Open your phone's Settings app.
    Near the bottom, tap System -> About phone > Software information
    See your "Android version" , “Service provider SW ver”

5.  Project Setup
  Download IntelliJ version 2018.3 :
    https://www.jetbrains.com/idea/download/other.html
  Open IntelliJ IDEA, import Framework AT as Maven project. After opening the terminal from the project’s folder, input the command line “mvn clean install” or “mvn clean verify”  to download Framework’s libraries.
  When you run the command line “mvn clean install” it fails
  Copy file reficio to folder ‘ .m2/repository ’. If you search directly, it won't appear so, and please follow as below steps to see .M2 repository path.
    Go-> Find folder -> type this "~/.m2" and click go
  Only for mac mini:
    ex: ​​
    Override parameters – With the Rosy project, it’s important to create a bunch of maven command lines to execute each platform with the desired test suite and test device, we can modify the environment with each execution, or we can declare parameters in a command line:
    node main.js --devices="Web|iPhone|iPad|AndroidPhone|Tablet>/<device-id>/<version-of-OS>/<port-appium-server>/<device-name><ENV>"
  Example:
    For iOS devices
      node main.js --devices="iPhoneSmokeTest/094cce3e15e6f0f60b1d5edcfaabb9dd285e2092/14.3/4723/iphone/DEV"

6. Config ANDROID_HOME
  Download and install Android studio here
  config ANDROID_HOME with Terminal window:
    open ~/.zshrc
    export ANDROID_HOME=/Users/$USER/Library/Android/sdk
    export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
    source ~/.zshrc
  Now open a second terminal and run. Which should give output like...
  From now on, whenever you open a terminal window, you can run the command line: adb devices to find the name of the connected device. (Android OS only)
  Link support configuration: https://www.youtube.com/watch?v=KfrTv_IP8Fs
  
7. Install auto app
  To install the application automatically, we must first install Ideviceinstaller
  brew install ideviceinstaller
  Then, run the command line to start install and run auto test
  node main.js --devices="Web|iPhone|iPad|AndroidPhone|Tablet>/<device-id>/<version-of-OS>/<port-appium-server>/<device-name>/<env>" --install
  Example:
    For iOS devices
      node main.js --devices="iPhoneSmokeTest/094cce3e15e6f0f60b1d5edcfaabb9dd285e2092/14.3/4723/iphone/DEV" --install