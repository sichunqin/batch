var userAgent = navigator.userAgent,
    rMsie = /(msie\s|trident.*rv:)([\w.]+)/,
    rFirefox = /(firefox)\/([\w.]+)/,
    rOpera = /(opera).+version\/([\w.]+)/,
    rChrome = /(chrome)\/([\w.]+)/,
    rSafari = /version\/([\w.]+).*(safari)/;
var browser;
var version;
var ua = userAgent.toLowerCase();

function uaMatch(ua) {
    var match = rMsie.exec(ua);
    if (match != null) {
        return {browser: "IE", version: match[2] || "0"};
    }
    var match = rFirefox.exec(ua);
    if (match != null) {
        return {browser: match[1] || "", version: match[2] || "0"};
    }
    var match = rOpera.exec(ua);
    if (match != null) {
        return {browser: match[1] || "", version: match[2] || "0"};
    }
    var match = rChrome.exec(ua);
    if (match != null) {
        return {browser: match[1] || "", version: match[2] || "0"};
    }
    var match = rSafari.exec(ua);
    if (match != null) {
        return {browser: match[2] || "", version: match[1] || "0"};
    }
    if (match != null) {
        return {browser: "", version: "0"};
    }
}

var browserMatch = uaMatch(userAgent.toLowerCase());
if (browserMatch.browser) {
    browser = browserMatch.browser;
    version = browserMatch.version;
}

var str = '';
var copyright = "金格科技iWebOffice2015智能文档中间件[演示版];V5.0S0xGAAEAAAAAAAAAEAAAAJEBAACgAQAALAAAAEnETMLfjzlFKkLbsBP8QVd2P5OtkkDyMgxgC1pFIRacyXbx8diNwmCITUtMwEDfDyAdCEVVr6yXCHD97ZiaT8YIZCNeE4A7gs3HY2tM8sspVWCd30/CiZaDUx0AsqcXEEXoB/vsqnZaET0u1wGsphKe8ZKcvJJJdQ3Ciy2cTiQObN//pSPY/3LgdxdJeEyC5ibUvVW9ngjJG0brXt4N17r1TDygcaC+SZ/pBTLg3Hac6bHoHHks5P9R5HvAjICsShbtkO2snAIvvslWn0BSDoL4XRSKyUErCdOwgUs+QjQ7HkoziQI+YpszJD05dP2AxH39S8feWlTg0D9ouIEHTCQH0KNvzaqKc2c2dYqrHqY77SJ9JRozb9SsqruQvUBo/CG91rVivzUiZrybJ14KjepAVO1mAmbCzbSYMFtGFWlBIwN6NbNYBtD+jZ4EB2dLs+V7dQpbR7J/EZVI2Sg8npwls3b9DTxkaNOubL7CkvTNfpETB8Irmq8kkWYzGRrv0iOKgRvkwm8AnQqliHCzRdvytzmoaUVhjEiTMjbEMYkovu1U1W2cVLD4jxA1xcDiXw==";


str += '<object id="WebOffice2015" ';

str += ' width="100%"';
str += ' height="100%"';

if ((window.ActiveXObject != undefined) || (window.ActiveXObject != null) || "ActiveXObject" in window) {
    if (window.navigator.platform == "Win32")
        str += ' CLASSID="CLSID:D89F482C-5045-4DB5-8C53-D2C9EE71D025"  codebase="iWebOffice2015.cab#version=0,0,0,0"';
    if (window.navigator.platform == "Win64")
        str += ' CLASSID="CLSID:D89F482C-5045-4DB5-8C53-D2C9EE71D024"  codebase="iWebOffice2015.cab#version=0,0,0,0"';
    str += '>';
    str += '<param name="Copyright" value="' + copyright + '">';
} else if (browser == "chrome") {
    str += ' clsid="CLSID:D89F482C-5045-4DB5-8C53-D2C9EE71D025"';
    str += ' type="application/kg-plugin"';
    str += ' OnCommand="OnCommand"';
    str += ' OnRightClickedWhenAnnotate="OnRightClickedWhenAnnotate"';
    str += ' OnSending="OnSending"';
    str += ' OnSendEnd="OnSendEnd"';
    str += ' OnRecvStart="OnRecvStart"';
    str += ' OnRecving="OnRecving"';
    str += ' OnRecvEnd="OnRecvEnd"';
    str += ' OnFullSizeBefore="OnFullSizeBefore"';
    str += ' OnFullSizeAfter="OnFullSizeAfter"';
    str += ' Copyright="' + copyright + '"';
} else if (browser == "firefox") {
    str += ' clsid="CLSID:D89F482C-5045-4DB5-8C53-D2C9EE71D025"';
    str += ' type="application/kg-activex"';
    str += ' OnCommand="OnCommand"';
    str += ' OnReady="OnReady"';
    str += ' OnOLECommand="OnOLECommand"';
    str += ' OnExecuteScripted="OnExecuteScripted"';
    str += ' OnQuit="OnQuit"';
    str += ' OnSendStart="OnSendStart"';
    str += ' OnSending="OnSending"';
    str += ' OnSendEnd="OnSendEnd"';
    str += ' OnRecvStart="OnRecvStart"';
    str += ' OnRecving="OnRecving"';
    str += ' OnRecvEnd="OnRecvEnd"';
    str += ' OnRightClickedWhenAnnotate="OnRightClickedWhenAnnotate"';
    str += ' OnFullSizeBefore="OnFullSizeBefore"';
    str += ' OnFullSizeAfter="OnFullSizeAfter"';
    str += ' Copyright="' + copyright + '"';
    str += '>';
}
str += '</object>';
document.write(str);
//alert(str);
//谷歌中加载插件
function OnControlCreated() {
    if (browser == "chrome") {
        KGChromePlugin = document.getElementById('WebOffice2015');
        iWebOffice = KGChromePlugin.obj;
        WebOffice.setObj(iWebOffice);
        Load();
    }
}