/**
 * This flow step will send a generic request to generate a Chart.
 *
 * @param {object} inputs
 */
step.generateChartQuickchart = function (inputs) {

	var inputsLogic = {
		devicePixelRatio: inputs.devicePixelRatio || 2.0,
		backgroundColor: inputs.backgroundColor || "transparent",
		width: inputs.width || 500,
		height: inputs.height || 500,
		format: inputs.formatoutput || "png",
		chart: inputs.chart || {}
	}

	return endpoint.chart.post(inputsLogic);
}