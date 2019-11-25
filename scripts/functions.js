endpoint.chart = function(chartOptions, callbackData, callbacks) {

    var url = "/chart";
    chartOptions = chartOptions || {};

    var options = checkHttpOptions(url, chartOptions);
    return endpoint._chartByPost(options, callbackData, callbacks);

};

endpoint.qr = function(qrOptions, callbackData, callbacks) {

    var url = getUrl("/qr", qrOptions);
    var options = checkHttpOptions(url, null);

    return endpoint._qrByGet(options, callbackData, callbacks);
};

/////////////////////////////
//  Private helpers
/////////////////////////////

var getUrl = function (url, args) {

    if (!url) {
        return null;
    }

    if (args) {
        var tmp = Object.keys(args).map(function (k) {
            return encodeURIComponent(k) + '=' + encodeURIComponent(args[k]);
        }).join('&');

        url += '?' + tmp;
    }

    return url;
};

var checkHttpOptions = function (url, options) {
    options = options || {};
    if (!!url) {
        if (isObject(url)) {
            // take the 'url' parameter as the options
            options = url || {};
        } else {
            if (!!options.path || !!options.params || !!options.body) {
                // options contains the http package format
                options.path = url;
            } else {
                // create html package
                options = {
                    path: url,
                    body: options
                }
            }
        }
    }
    return options;
};

var isObject = function (obj) {
    return !!obj && stringType(obj) === '[object Object]'
};

var stringType = Function.prototype.call.bind(Object.prototype.toString);