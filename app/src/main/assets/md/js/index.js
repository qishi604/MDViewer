/**
 * parse md string to html
 * @param string
 */
function parseString(string) {
	if (string || !string.startsWith('<')) {
		document.body.innerHTML = marked(string);
	}
}
window.onload = function () {
	parseString(document.body.innerHTML);
}