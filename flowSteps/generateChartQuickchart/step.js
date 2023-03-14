/**
 * This flow step will send a generic request to generate a Chart.
 *
 * @param {object} inputs
 */
step.generateChartQuickchart = function (inputs) {

	sys.logs.warn(JSON.stringify(inputs));
	var inputsLogic = {
		devicePixelRatio: inputs.devicePixelRatio || 2.0,
		name: inputs.filename,
		backgroundColor: inputs.backgroundColor || "transparent",
		width: inputs.width || 500,
		height: inputs.height || 500,
		format: inputs.formatoutput || "png",
		chart: inputs.chart || {}
	}

	return endpoint.chart.post(inputsLogic);
}