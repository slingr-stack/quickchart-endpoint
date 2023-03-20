---
title: QuickChart
keywords: 
last_updated: March 14, 2023
tags: []
summary: "Detailed description of QuickChart endpoint."
---

## Overview

[QuickChart](https://quickchart.io/) creates embeddable chart and qr images with a single URL. Chart images are defined by [Chart.js](https://www.chartjs.org/) objects. 

The QuickChart endpoint allows to create Chart and QR codes. Some of the features supported by this endpoint are:

- Generate an image (PNG or PDF) with the Chart
- Generate an image (PNG or SVG) with the QR codes

## Configuration

### Key
It is necessary in case to use QuickChart Pro. It is not required.

## Javascript API

### Generate chart image

```js
app.endpoints.quickchart.chart(chartOptions, callbackData, callbacks);
```

Where:

- `chartOptions`: a map with the following options:
  - `name`: this is the image/pdf file name. If it is null the default is `chart-{uuid}`.
  - `chart`: use a [Chart.js config](https://www.chartjs.org/docs/latest/charts/) to define your chart (required).
  - `backgroundColor`: background of the chart canvas (optional). By default `transparent`.
  - `width`: Width of the image (optional). By default `500`.
  - `height`: height of the image (optional). By default `500`.
  - `format`: format of your output. Currently the two supported output formats are PNG and PDF (optional). By default `png`.
  - `devicePixelRatio`: device pixel ratio of the output (defaults to retina=2.0). Note that width and height are multiplied by this value. (optional)
- `callbackData`: information that will be passed to the callbacks in the second parameter.
- `callbacks`: a map with the callback `chartResponse` to receive the file with the Chart.

Sample:

You can generate a chart images like this:

```js
var record = sys.data.findById('company', '5ab0041fea30451201647e2b');
app.endpoints.quickchart.chart({
        "backgroundColor": "white",
        "width": "500",
        "height": "300",
        "format": "png",
        "chart": {
          "type": "bar",
          "data": {
            "labels": [2012, 2013, 2014, 2015, 2016],
            "datasets": [{
              "label": record.field('labelA').val(),
              "data": [12, 6, 5, 18, 12]
            }, {
              "label": record.field('labelB').val(),
              "data": [4, 8, 16, 5, 5]
            }]
          }
      }
  },
  { record: record }, 
  {
    'chartResponse': function(res, callbackData) {
      var data = res.data;
      if(data && data.status == "ok") {
        var document = callbackData.record;
        document.field('myFileType').val({
          id: data.file.fileId,
          name: data.file.fileName,
          contentType: data.file.contentType
        });
        sys.data.save(document);
      }
    }
  });
```

> Note that if you want to include Javascript code in chart (e.g. to format labels), you'll have to send the entire 
> chart parameter as a string rather than a JSON object. 

Following example generate a line chart with formatted label in axis y 

```js
    var MONTHS = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    
    var rand = function(min, max) {
        var seed = Date.now();
        min = min === undefined ? 0 : min;
        max = max === undefined ? 1 : max;
        seed = (seed * 9301 + 49297) % 233280;
        var result = min + (seed / 233280) * (max - min);
        
        sys.logs.info("--> Inside rand" + result);
        return Math.round(result);
    };
            
    var chartColors = {
        red: 'rgb(255, 99, 132)',
        orange: 'rgb(255, 159, 64)',
        yellow: 'rgb(255, 205, 86)',
        green: 'rgb(75, 192, 192)',
        blue: 'rgb(54, 162, 235)',
        purple: 'rgb(153, 102, 255)',
        grey: 'rgb(201, 203, 207)'
    };
    
    var arr1 = [ rand(-100, 100), rand(-100, 100), rand(-100, 100), rand(-100, 100), rand(-100, 100), rand(-100, 100),  rand(-100, 100) ];
    
    var arr2 = [ rand(-100, 100), rand(-100, 100), rand(-100, 100), rand(-100, 100), rand(-100, 100), rand(-100, 100),  rand(-100, 100) ];
    
    var config3 =  "{";
    config3 += "  \"type\": \"line\",";
    config3 += "  \"data\": {";
    config3 += "    \"labels\": [";
    config3 += "      \"January\",";
    config3 += "      \"February\",";
    config3 += "      \"March\",";
    config3 += "      \"April\",";
    config3 += "      \"May\",";
    config3 += "      \"June\",";
    config3 += "      \"July\"";
    config3 += "    ],";
    config3 += "    \"datasets\": [";
    config3 += "      {";
    config3 += "        \"label\": \"My First dataset\",";
    config3 += "        \"backgroundColor\": \"rgb(255, 99, 132)\",";
    config3 += "        \"borderColor\": \"rgb(255, 99, 132)\",";
    config3 += "        \"data\": "+ JSON.stringify(arr1) +",";
    config3 += "        \"fill\": false";
    config3 += "      },";
    config3 += "      {";
    config3 += "        \"label\": \"My Second dataset\",";
    config3 += "        \"fill\": false,";
    config3 += "        \"backgroundColor\": \"rgb(54, 162, 235)\",";
    config3 += "        \"borderColor\": \"rgb(54, 162, 235)\",";
    config3 += "        \"data\": "+ JSON.stringify(arr2);
    config3 += "      }";
    config3 += "    ]";
    config3 += "  },";
    config3 += "  \"options\": {";
    config3 += "    \"responsive\": true,";
    config3 += "    \"title\": {";
    config3 += "      \"display\": true,";
    config3 += "      \"text\": \"Chart.js X Line Chart\"";
    config3 += "    },";
    config3 += "    \"tooltips\": {";
    config3 += "      \"mode\": \"index\",";
    config3 += "      \"intersect\": false";
    config3 += "    },";
    config3 += "    \"hover\": {";
    config3 += "      \"mode\": \"nearest\",";
    config3 += "      \"intersect\": true";
    config3 += "    },";
    config3 += "    \"scales\": {";
    config3 += "      \"xAxes\": [";
    config3 += "        {";
    config3 += "          \"display\": true,";
    config3 += "          \"scaleLabel\": {";
    config3 += "            \"display\": true,";
    config3 += "            \"labelString\": \"Month\"";
    config3 += "          }";
    config3 += "        }";
    config3 += "      ],";
    config3 += "      \"yAxes\": [";
    config3 += "        {";
    config3 += "          \"ticks\": {";
    config3 += "                    callback: function(value, index, values) {";
    config3 += "                        return \"$\" + value;";
    config3 += "                    }";
    config3 += "                }";
    config3 += "        }";
    config3 += "      ]";
    config3 += "    }";
    config3 += "  }";
    config3 += "}";


    var record = sys.data.findById('company', '5e415a74c2c7e73982e328ba');
    app.endpoints.quickchart.chart({
            "name": "my-chart",
            "backgroundColor": "white",
            "width": "500",
            "height": "300",
            "format": "png",
            "chart": config3
      },
      { record: record }, 
      {
        'chartResponse': function(res, callbackData) {
          var data = res.data;
          if(data && data.status == "ok") {
            sys.logs.info("Chart was generated");
          } else {
            sys.logs.error("+++ There are errors to generate file");
          }
          
          if(data && data.file) {
            var document = callbackData.record;
            document.field('chartFile').val({
              id: data.file.fileId,
              name: data.file.fileName,
              contentType: data.file.contentType
            });
            sys.data.save(document);
          }
    
        }
      });
```

### Generate QR codes

```js
app.endpoints.quickchart.qr(qrOptions, callbackData, callbacks);
```

Where:

- `qrOptions`: a map with the following options:
  - `name`: this is the image file name. If it is null the default is `qr-{uuid}`.
  - `text`: text to convert to QR code (required).
  - `margin`: specify the whitespace around QR image (optional). By default `4`.
  - `size`: in pixels of the square QR image (optional). By default `150`.
  - `ecLevel`: error correction level (valid values: L, M, Q, H) (optional). By default `M`.
  - `dark`: QR color (optional). By default `000`.
  - `light`: background color (optional). By default `fff`.
  - `format`: format of your output. Currently the two supported output formats are PNG and SVG (optional). By default `png`.
- `callbackData`: information that will be passed to the callbacks in the second parameter.
- `callbacks`: a map with the callback `qrResponse` to receive the file with the QR.

Sample:

You can generate a QR code like this:

```js
var record = sys.data.findById('company', '5ab0041fea30451201647e2b');
app.endpoints.quickchart.qr({
    "text": record.field('description').val(),
    "dark": "f23",
    "light": "55f",
    "ecLevel": "Q",
    "size": "300"
  },
  { record: record }, 
  {
    'qrResponse': function(res, callbackData) {
      var data = res.data;
      if(data && data.status == "ok") {
        var document = callbackData.record;
        document.field('myFileType').val({
          id: data.file.fileId,
          name: data.file.fileName,
          contentType: data.file.contentType
        });
        sys.data.save(document);
      }
    }
  });
```

# Javascript API

The Javascript API of the quickchart endpoint has three pieces:

- **HTTP requests**: These allow to make regular HTTP requests.
- **Shortcuts**: These are helpers to make HTTP request to the API in a more convenient way.
- **Additional Helpers**: These helpers provide additional features that facilitate or improves the endpoint usage in SLINGR.

## HTTP requests
You can make `POST`,`GET` requests to the [quickchart API](API_URL_HERE) like this:
```javascript
var response = app.endpoints.quickchart.post('/chart')
var response = app.endpoints.quickchart.get('/qr')
```

Please take a look at the documentation of the [HTTP endpoint](https://github.com/slingr-stack/http-endpoint#javascript-api)
for more information about generic requests.

## Shortcuts

Instead of having to use the generic HTTP methods, you can (and should) make use of the helpers provided in the endpoint:
<details>
    <summary>Click here to see all the helpers</summary>

<br>

* API URL: '/chart'
* HTTP Method: 'POST'
```javascript
app.endpoints.quickchart.chart.post(chartOptions, body)
```
---
* API URL: '/qr'
* HTTP Method: 'GET'
```javascript
app.endpoints.quickchart.qr.get(qrOptions)
```
---

</details>

## Flow Step

As an alternative option to using scripts, you can make use of Flows and Flow Steps specifically created for the endpoint:
<details>
    <summary>Click here to see the Flow Steps</summary>

<br>



### Generic Flow Step

Generic flow step for full use of the entire endpoint and its services.

<h3>Inputs</h3>

<table>
    <thead>
    <tr>
        <th>Label</th>
        <th>Type</th>
        <th>Required</th>
        <th>Default</th>
        <th>Visibility</th>
        <th>Description</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>URL (Method)</td>
        <td>choice</td>
        <td>yes</td>
        <td> - </td>
        <td>Always</td>
        <td>
            This is the http method to be used against the endpoint. <br>
            Possible values are: <br>
            <i><strong>POST,GET</strong></i>
        </td>
    </tr>
    <tr>
        <td>URL (Path)</td>
        <td>choice</td>
        <td>yes</td>
        <td> - </td>
        <td>Always</td>
        <td>
            The url to which this endpoint will send the request. This is the exact service to which the http request will be made. <br>
            Possible values are: <br>
            <i><strong>/chart<br>/qr<br></strong></i>
        </td>
    </tr>
    <tr>
        <td>Query Params</td>
        <td>keyValue</td>
        <td>no</td>
        <td> - </td>
        <td>Always</td>
        <td>
            Used when you want to have a custom query params for the http call.
        </td>
    </tr>
    </tbody>
</table>

<h3>Outputs</h3>

<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Type</th>
        <th>Description</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>response</td>
        <td>object</td>
        <td>
            Object resulting from the response to the endpoint call.
        </td>
    </tr>
    </tbody>
</table>


</details>

For more information about how shortcuts or flow steps works, and how they are generated, take a look at the [slingr-helpgen tool](https://github.com/slingr-stack/slingr-helpgen).

## Additional Flow Step


<details>
    <summary>Click here to see the Customs Flow Steps</summary>

<br>



### Generate Chart

Flow Step that provides us with the fields to create a Chart.

<h3>Inputs</h3>

<table>
    <thead>
    <tr>
        <th>Label</th>
        <th>Type</th>
        <th>Required</th>
        <th>Default</th>
        <th>Visibility</th>
        <th>Description</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>Background Color</td>
        <td>text</td>
        <td>no</td>
        <td> "transparent" </td>
        <td>Always</td>
        <td>
            Background of the chart canvas. Accept colors by name (red, blue, Default: transparent), and hex values (#ff00ff) without the "#".
        </td>
    </tr>
    <tr>
        <td>Width</td>
        <td>number</td>
        <td>no</td>
        <td> 500 </td>
        <td>Always</td>
        <td>
            Width of the image of chart in pixels.
        </td>
    </tr>
    <tr>
        <td>Heigth</td>
        <td>number</td>
        <td>no</td>
        <td> 500 </td>
        <td>Always</td>
        <td>
            Height of the image of chart in pixels.
        </td>
    </tr>
    <tr>
        <td>Device Pixel Ratio</td>
        <td>number</td>
        <td>no</td>
        <td> 2.0 </td>
        <td>Always</td>
        <td>
            Width and height are multiplied by this value.
        </td>
    </tr>
    <tr>
        <td>Output Format</td>
        <td>choice</td>
        <td>yes</td>
        <td> PNG </td>
        <td>Always</td>
        <td>
            File format of the image. Possible values are: <br> PNG <br> PDF
        </td>
    </tr>
    <tr>
        <td>Chart Data</td>
        <td>json</td>
        <td>yes</td>
        <td> - </td>
        <td>Always</td>
        <td>
            Json with the data to generate the chart.
        </td>
    </tr>
    </tbody>
</table>

<h3>Outputs</h3>

<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Type</th>
        <th>Description</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>response</td>
        <td>object</td>
        <td>
            Object resulting from the response to the endpoint call.
        </td>
    </tr>
    </tbody>
</table>


### Generate QR

Flow Step that provides us with the fields to create a QR code.

<h3>Inputs</h3>

<table>
    <thead>
    <tr>
        <th>Label</th>
        <th>Type</th>
        <th>Required</th>
        <th>Default</th>
        <th>Visibility</th>
        <th>Description</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>Text</td>
        <td>text</td>
        <td>no</td>
        <td> - </td>
        <td>Always</td>
        <td>
            Text to be encoded in the QR code.    
        </td>
    </tr>
    <tr>
        <td>Margin</td>
        <td>number</td>
        <td>no</td>
        <td> 4 </td>
        <td>Always</td>
        <td>
            Is the whitespace around QR image.
        </td>
    </tr>
    <tr>
        <td>Error Correction Level</td>
        <td>choice</td>
        <td>no</td>
        <td> M </td>
        <td>Always</td>
        <td>
            Error correction level Possible values are: <br> L <br> M <br> Q
        </td>
    </tr>
    <tr>
        <td>Output Format</td>
        <td>choice</td>
        <td>yes</td>
        <td> PNG </td>
        <td>Always</td>
        <td>
            File format of the image. Possible values are: <br> PNG <br> SVG
        </td>
    </tr>
    <tr>
        <td>Dark Color</td>
        <td>text</td>
        <td>no</td>
        <td> 000000 </td>
        <td>Always</td>
        <td>
            Color of the dark squares of QR code. Accept colors by hex values (#ff00ff) without the "#".
        </td>
    </tr>
    <tr>
        <td>Light Color (Background)</td>
        <td>text</td>
        <td>no</td>
        <td> ffffff </td>
        <td>Always</td>
        <td>
            Color of the whites spaces of QR code. Accept colors by hex values (#ff00ff) without the "#".
        </td>
    </tr>
    </tbody>
</table>

<h3>Outputs</h3>

<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Type</th>
        <th>Description</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>response</td>
        <td>object</td>
        <td>
            Object resulting from the response to the endpoint call.
        </td>
    </tr>
    </tbody>
</table>



</details>

## Additional Helpers
*MANUALLY ADD THE DOCUMENTATION OF THESE HELPERS HERE...*

## About SLINGR

SLINGR is a low-code rapid application development platform that accelerates development, with robust architecture for integrations and executing custom workflows and automation.

[More info about SLINGR](https://slingr.io)

## License

This endpoint is licensed under the Apache License 2.0. See the `LICENSE` file for more details.