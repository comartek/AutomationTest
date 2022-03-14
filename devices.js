const yargs = require('yargs');

// Sample arg string: --devices=iPhone/00008030-000A6DA421C1802E/13.6.1/deviceName1|iPad/00008020-001D24D91E79002E/14.2/iPadMini5|Tablet/R9WN3028Q7J/10/androidName|AndroidPhone/RF8N304KXZH/10/G_Note10|Web
// Format: <type:iPhone|iPad|AndroidPhone|Tablet>/<device-id>/<version-of-OS><device-name>

const { devices: devicesStr, suite: suiteStr } = yargs.argv;

const suitesMapping = {
    iPad_Web_Mobile: '**/iPadWeb/*.story',
    iPhone_Web_Mobile: '**/iPhoneWeb/*.story',
    iPad_App_Mobile: '**/iPadApp/*.story',
    iPad_UAT_Mobile: '**/iPadUAT/*.story',
    Android_Web_Mobile: '**/AndroidWeb/*.story',
    Android_SBH_Mobile: '**/SoBanHang/*.story',
    iPhone_SBH_Mobile: '**/SoBanHang/iPhone/*.story',



};

const devices = devicesStr.split('|').map((deviceStr, index) => {
    const chunks = deviceStr.split('/');
    const type = chunks[0];
    if (type === 'Web') {
        return {
            platform: 'Web',
            name: 'Chrome',
        };
    }
    const uuid = chunks[1];
    const version = chunks[2];
    const port = chunks[3];
    const name = chunks[4];
    const bundleId = chunks[5];
    const platform = type.startsWith('i') ? 'iOS' : 'Android';

    let suite = suitesMapping[type];
    if (suiteStr && !`${suiteStr}`.toLowerCase().includes('all')) {
        suite = `${suiteStr}`.split(',').map((s) => {
            const testCaseWildcard = suite.replace('*.story', `${s.trim()}_*.story`);
            return `"${testCaseWildcard}"`;
        }).join(',');
    }

    return {
        platform,
        version,
        name,
        uuid,
        bundleId,
        suite,
        wdalocalport: 8130 + index,
        port,
        // port: 4770 + index,
    };

});
module.exports = devices;