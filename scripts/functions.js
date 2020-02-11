endpoint.chart = function (chartOptions, callbackData, callbacks) {

    if (!chartOptions) {
        throw 'Invalid chart options';
    }

    if (!chartOptions.chart) {
        throw 'Chart or c can not be empty';
    }

    var URI = "/chart"
    if (endpoint._configuration && endpoint._configuration.key) {
        URI = concatUrl(URI, 'key=' + endpoint._configuration.key);
    }

    var options = checkHttpOptions(URI, chartOptions);
    return endpoint._chartByPost(options, callbackData, callbacks);

};

endpoint.qr = function (qrOptions, callbackData, callbacks) {

    var url = "/qr";
    if (endpoint._configuration && endpoint._configuration.key) {
        url = concatUrl(url, 'key=' + endpoint._configuration.key);
    }

    url = getUrl(url, qrOptions);

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

        url = concatUrl(url, tmp);

    }

    return url;
};

var concatUrl = function (url, str) {
    return url + ((!url || url.indexOf('?') < 0) ? '?' : '&') + str;
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