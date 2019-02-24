var ApiHost = "http://127.0.0.1:8080";
var Host = "http://192.168.1.230:90";
var LocalSvnUrl = "svn://192.168.1.222:3666/";
var ServerSvnUrl = "svn://118.190.156.91:20008/";
function toLoginPage(){
    url = Host+"/login.html";
	location.href=url;
}
//退出登录
function logOut(){
    sessionStorage.removeItem("userToken");
    toLoginPage();
}
//复制功能
function copyFun(val)
{
    var oInput = document.createElement('input');
    oInput.value = val;
    document.body.appendChild(oInput);
    oInput.select(); // 选择对象
    document.execCommand("Copy"); // 执行浏览器复制命令
    oInput.className = 'oInput';
    oInput.style.display='none';
    layer.msg('复制成功');
}