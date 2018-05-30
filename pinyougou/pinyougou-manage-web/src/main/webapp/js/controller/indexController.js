app.controller("indexController",function ($scope,loginService) {//注入内置对象

    //获取用户名
    $scope.getUsername = function () {
        loginService.getUsername().success(function (response) {
            $scope.username = response.username;
        })   
   }

})