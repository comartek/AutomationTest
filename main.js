// Sample arg string: --devices=iPhone/00008030-000A6DA421C1802E/13.6.1/deviceName1|iPad/00008020-001D24D91E79002E/14.2/iPadMini5|Tablet/R9WN3028Q7J/10/androidName|AndroidPhone/RF8N304KXZH/10/G_Note10|Web
// --install to install from output

/* eslint-disable no-unreachable */
/* eslint-disable no-console */
/* eslint-disable no-unused-expressions */
const { exec, execSync } = require('child_process');
const path = require('path');
const yargs = require('yargs');
const devices = require('./devices');

const { install: hasInstall } = yargs.argv;

let willRunOnDevices = [];

willRunOnDevices = devices.map((d) => {
    if (d.platform === 'iOS') {
        return {
            ...d,
            app: path.join(__dirname, '../builder/Outputs/iOS/Rosy-Dev.ipa'),
        };
    }

    if (d.platform === 'Android') {
        return {
            ...d,
            app: path.join(__dirname, '../builder/Outputs/Android/rosy.apk'),
        };
    }

    return d;
});

function serveAppiumPortForDeviceAndroid(port, uuid) {
    return new Promise((res) => {
        exec(`appium -p ${port} -U "${uuid}" --full-reset --session-override`, {
            maxBuffer: 1024 * 1024 * 100,
        }, res);
    });
}

function serveAppiumPortForDeviceIOS(port, wdalocalport) {
    return new Promise((res) => {
        exec(`appium -p ${port} --webdriveragent-port ${wdalocalport}`, {
            maxBuffer: 1024 * 1024 * 100,
        }, res);
    });
}

function installAppIntoDevice(app, uuid) {
    const isIOS = app.includes('.ipa');
    if (isIOS) {
        return execSync(`ideviceinstaller -u ${uuid} -i ${app}`);
    }
    return execSync(`adb -s ${uuid} install ${app};`);
}

/**
 *
 * @param {string} platform : Android | iOS
 * @param {*} uuid
 * @param {string} suite
 * @param {number} port
 * @param {number} version
 */
function runTest(root, { platform, uuid, suite, port, version, name, app, wdalocalport, bundleId }) {
    const running = new Promise(async (res, rej) => {
        hasInstall && installAppIntoDevice(app, uuid);

        const completionHandle = (err, stdOut, stdErr) => {
            if (err || stdErr) {
                rej(err || stdErr);
            }
            res(stdOut);
        };
        const script = `cd ${root} && mvn verify allure:report -P fastCompilation -Dbrowser=${platform} -DdeviceName=${name} -Dsuite="${suite}" -DappiumHub=http:"//127.0.0.1:${port}/wd/hub" -DplatformVersion=${version} -Dudid=${uuid} -DbundleId=${bundleId}`;
        const execution = exec(script, {
            encoding: 'utf8',
            maxBuffer: 1024 * 1024 * 100,
        }, completionHandle);

        console.log(`Run test on ${name} - ${uuid}: ${script}`);
        console.log(`${name} - ${uuid}: `);

        let stdOut = ''
        execution.stdout.on('data', function(d) {
            stdOut += d;
            if (d.endsWith("\n") || d.length > 2000) {
                console.log(stdOut.trim());
                stdOut = '';
            }
        });
    });

    const serveAppiumPort = platform === 'iOS' ? serveAppiumPortForDeviceIOS(port, wdalocalport) : serveAppiumPortForDeviceAndroid(port, uuid);

    return Promise.all([serveAppiumPort, running]);
}

function runTestWeb(root) {
    return new Promise((res) => {
        const process = exec(
            `cd ${root} && mvn verify allure:report -P fastCompilation -Dbrowser=Chrome -Dsuite="**/NhaHang/*.story"`,
            {
                maxBuffer: 1024 * 1024 * 100,
            },
            res,
        );
        process.stdout.on('data', console.log);
    });
}

async function main() {
    try {
        const root = __dirname;
        const executions = willRunOnDevices.map((des) => {
            if (des.platform === 'Web') {
                return runTestWeb(root);
            }
            return runTest(root, des);
        });
        const result = await Promise.all(executions);
        console.log(result);
    } catch (error) {
        console.log(error);
    }
}

main();
