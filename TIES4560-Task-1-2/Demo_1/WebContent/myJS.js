window.onload = customize;

function customize(){
	window.document.getElementById('div3').style.display = 'none';
}

function defineWord()
{
    window.document.getElementById('div3').style.display = 'none';
	var q_str = 'type=text&value='+document.getElementById('t1').value;
	doAjax('Serv1',q_str,'defineWord_back','post',0);
}
function defineWord_back(result)
{
	if (result.substring(0,5)=='error'){
	   window.document.getElementById('div3').style.display = 'block';
	   window.document.getElementById('div3').innerHTML="<p style='color:red;'><b>"+result.substring(6)+"</b></p>";
   }else{
	   var t = document.getElementById("t31").textContent = result;
   }
}

function findIt()
{
    window.document.getElementById('div3').style.display = 'none';
	var q_str = 'type=number&value='+document.getElementById('t12').value;
	doAjax('Serv1',q_str,'findIt_back','post',0);
}
function findIt_back(result)
{
	if (result.substring(0,5)=='error'){
	   window.document.getElementById('div3').style.display = 'block';
	   window.document.getElementById('div3').innerHTML="<p style='color:red;'><b>"+result.substring(6)+"</b></p>";
   }else{
	   var t = document.getElementById("t32").textContent = result;
   }
}
