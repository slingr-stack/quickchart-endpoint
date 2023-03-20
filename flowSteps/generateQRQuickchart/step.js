/**
 * This flow step will send a generic request to generate QR.
 *
 * @param {object} inputs
 */
step.generateQRQuickchart = function (inputs) {

	var inputsLogic = {
		devicePixelRatio: inputs.devicePixelRatio || 2.0,
		text: inputs.text || "QR Code",
		margin: inputs.margin || 4,
		size: inputs.size || 150,
		format: inputs.formatoutput || "png",
		ecLevel: inputs.ecLevel || "M",
		dark: inputs.dark || "000",
		light: inputs.light || "fff",
	}

	return endpoint.qr.get(inputsLogic);
}