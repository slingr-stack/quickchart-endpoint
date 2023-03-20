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