const express = require('express');
const compression = require('compression');
const { createProxyMiddleware } = require('http-proxy-middleware');
const path = require('path');
const fs = require('fs');
const https = require('https');

const key = fs.readFileSync('.cert/key.pem');
const cert = fs.readFileSync('.cert/cert.pem');

const port = process.env.PORT || 3000;

const app = express();

app.use(compression());

app.use('/', express.static(path.join(__dirname, '../build')));

const target = 'https://command-wrt-dev-core.herokuapp.com';

// proxy
app.use(
    '/command',
    createProxyMiddleware({
        target,
        changeOrigin: true,
    }),
);

app.use(
    '/pusher',
    createProxyMiddleware({
        target,
        changeOrigin: true,
    }),
);

app.get('*', (_, res) => {
    const file = fs.readFileSync(path.join(__dirname, '../build/index.html'), 'utf8');
    res.send(file);
});

const server = https.createServer({
    key,
    cert,
}, app);

server.listen(port, () => console.log(`Proxy server listening on port ${port}!`));
