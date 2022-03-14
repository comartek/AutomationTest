/* eslint-disable no-console */
const nodemailer = require('nodemailer');
const path = require('path');
const yargs = require('yargs');
const appInfo = require('./../app.json');

const { email, pass, to, report, platform } = yargs.argv;

if (!email || !pass || !to || !report || !platform) {
    throw new Error('Invalid params');
}

// async..await is not allowed in global scope, must use a wrapper
async function main() {
    const transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
            user: email,
            pass,
        },
    });

    const changeLogPath = path.join(__dirname, '../changelog.txt');

    // send mail with defined transport object
    const info = await transporter.sendMail({
        from: `"Rosy Builder" <${email}>`, // sender address
        to, // list of receivers
        subject: `[${platform}: ${appInfo.version}-${appInfo.arkusVersion}] Automation test report`, // Subject line
        html: `<div><a href="${report}">Link to report</a></div>`, // html body,
    });

    console.log('Message sent: %s', info.messageId);
}

main().catch(console.error);
