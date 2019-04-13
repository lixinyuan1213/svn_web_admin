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
//退出
function log_out(){
	location.href = "/logOut";
}
//退出
function logOut(){
	location.href = "/logOut";
}