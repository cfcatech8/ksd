
var CryptoAgent = "";
// Create ActiveX object according to the platform
function OnLoad() {
            try {
                var eDiv = document.createElement("div");
                if (navigator.appName.indexOf("Internet") >= 0 || navigator.appVersion.indexOf("Trident") >= 0) {
                    if (window.navigator.cpuClass == "x86") {
                        eDiv.innerHTML = "<object id=\"CryptoAgent\" codebase=\"CryptoKit.Paperless.x86.cab\" classid=\"clsid:B64B695B-348D-400D-8D58-9AAB1DA5851A\" ></object>";
                    }
                    else {
                        eDiv.innerHTML = "<object id=\"CryptoAgent\" codebase=\"CryptoKit.Paperless.x64.cab\" classid=\"clsid:8BF7E683-630E-4B59-9E61-C996B671EBDF\" ></object>";
                    }
                }
                else {
                    eDiv.innerHTML = "<embed id=\"CryptoAgent\" type=\"application/npCryptoKit.Paperless.x86\" style=\"height: 0px; width: 0px\">";
                }
                document.body.appendChild(eDiv);
            }
            catch (e) {
                alert(e);
                return;
            }
            CryptoAgent = document.getElementById("CryptoAgent");
        }
        
//Select certificate
function SelectCertificateOnClick(sourceHash,ID) {
    try {
        var subjectDNFilter = "";
        var issuerDNFilter = "";
        var serialNumFilter = "";
		var cspName = "";
        var bSelectCertResult = "";
        subjectDNFilter = "";//document.getElementById("SubjectDNFilter").value;
        issuerDNFilter = "";// document.getElementById("IssuerDNFilter").value;
        serialNumFilter =  "";//document.getElementById("SerialNumFilter").value;
		cspName =  "";//document.getElementById("CSPNameFilter").value;

        bSelectCertResult = CryptoAgent.SelectCertificate(subjectDNFilter, issuerDNFilter, serialNumFilter, cspName);
        // Opera浏览器，NPAPI函数执行结果为false时，不能触发异常，需要自己判断返回值。
        if (!bSelectCertResult) {
            var errorDesc = CryptoAgent.GetLastErrorDesc();
            alert(errorDesc);
            return;
        }
        SignOnClick(sourceHash,ID);
        //document.getElementById("SelectCertResult").value = bSelectCertResult;
    }
    catch (e) {
        var errorDesc = CryptoAgent.GetLastErrorDesc();
        alert(errorDesc);
    }
}
// Sign hash message
function SignOnClick(sourceHash,ID) {
    try {
        var sourceHashData =sourceHash;// "";
        var signature = "";

        //document.getElementById("Signature").value = "";
        signature = CryptoAgent.SignHashMsgPKCS7Detached(sourceHashData, "SHA-1");
        if (!signature) {
            var errorDesc = CryptoAgent.GetLastErrorDesc();
            alert(errorDesc);
            return;
        }
        CompoundPdf(signature,ID);
        //document.getElementById("Signature").value = signature;
    } catch (e) {
        var errorDesc = CryptoAgent.GetLastErrorDesc();
        alert(errorDesc);
    }
}
function CompoundPdf(signature,ID) {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "CompoundPdf", true);
xhr.overrideMimeType('application/json; charset = utf-8')
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                //SelectCertificateOnClick(JSON.parse(this.responseText).pdfSealHash,JSON.parse(this.responseText).id);
                document.getElementById("sealedPDF").removeAttribute("hidden");
            }
        }
    };
    xhr.send(JSON.stringify({
    	"signature":signature,
    	"ID":ID
    })); 
}

