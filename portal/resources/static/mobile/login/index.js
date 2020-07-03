var uname = document.getElementsByClassName('uname')[0].children[1]
var unameClear = document.getElementsByClassName('uname')[0].children[2]
uname.addEventListener('input', function () {
    if (uname.value !== '') {
        unameClear.style.display = "block"
    } else {
        unameClear.style.display = "none"
    }
})
unameClear.addEventListener('click', function () {
    uname.value = ''
    unameClear.style.display = "none"
})
var upwd = document.getElementsByClassName('upwd')[0].children[1]
var upwdClear = document.getElementsByClassName('upwd')[0].children[2]
console.log(upwd, upwdClear)
upwd.addEventListener('input', function () {
    if (upwd.value !== '') {
        upwdClear.style.display = "block"
    } else {
        upwdClear.style.display = "none"
    }
})
upwdClear.addEventListener('click', function () {
    upwd.value = ''
    upwdClear.style.display = "none"
})
var forgetPassword = document.getElementsByClassName('forgetPassword')[0]
forgetPassword.addEventListener('click', function () {

})
var button = document.getElementsByClassName('login')[0]
button.addEventListener('click', function () {
    var uname = document.getElementsByClassName('uname')[0].children[1].value
    var upwd = document.getElementsByClassName('upwd')[0].children[1].value
    if (uname === '' || upwd === '') {
        alert('用户名或密码不能为空~')
    } else {
        document.forms[0].submit();
    }
})
