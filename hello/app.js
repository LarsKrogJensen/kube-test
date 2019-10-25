const express = require('express');
const fetch = require('node-fetch');

const app = express();
const router = express.Router();

const port = 8080;

router.use((req, res, next) => {
  console.log('/' + req.method);
  next();
});

router.get('/hello', async (req, res) => {
  let response = await fetch("https://httpbin.org/get" )
  let data = await response.json()
  res.send(data);
});

app.use('/', router);

app.listen(port, () => console.log('Example app listening on port 8080!'))
