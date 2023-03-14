/**
 * This flow step will send a generic request to generate QR.
 *
 * @param {object} inputs
 */
step.generateQRQuickchart = function (inputs) {

	sys.logs.warn(JSON.stringify(inputs));
	var inputsLogic = {
		devicePixelRatio: inputs.devicePixelRatio || 2.0,
		name: inputs.filename,
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