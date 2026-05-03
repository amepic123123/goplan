const http = require('http');

const data = JSON.stringify({
  projectName: 'test',
  basePackage: 'com.example',
  framework: 'SPRING_BOOT',
  features: ['Models']
});

const req = http.request('http://localhost:8080/api/v1/projects/generate', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': data.length
  }
}, (res) => {
  console.log(`STATUS: ${res.statusCode}`);
  console.log(`HEADERS: ${JSON.stringify(res.headers, null, 2)}`);
  
  const chunks = [];
  res.on('data', (chunk) => chunks.push(chunk));
  res.on('end', () => {
    const body = Buffer.concat(chunks);
    console.log(`BODY LENGTH: ${body.length}`);
    if (res.headers['content-type'] && res.headers['content-type'].includes('application/json')) {
      console.log(`BODY: ${body.toString()}`);
    } else {
      console.log(`BODY: (binary data) first 20 bytes: ${body.slice(0, 20).toString('hex')}`);
      console.log(`BODY: (as string) first 50 chars: ${body.slice(0, 50).toString()}`);
    }
  });
});

req.on('error', (e) => {
  console.error(`problem with request: ${e.message}`);
});

req.write(data);
req.end();
