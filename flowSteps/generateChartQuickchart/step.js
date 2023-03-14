/**
 * This flow step will send generic request.
 *
 * @param {object} inputs
 * {text} method, This is used to config method.
 * {text} url, This is used to config external URL.
 * {Array[string]} pathVariables, This is used to config path variables.
 */
step.generateChartQuickchart = function (inputs) {

	sys.logs.warn(inputs);
	var inputsLogic = {
		backgroundColor: inputs.backgroundColor || "white",
		width: inputs.width || 500,
		height: inputs.height || 500,
		format: inputs.formatoutput || "png",
		chart: inputs.chart || {}
	}

	return endpoint.chart.post(inputsLogic);
}