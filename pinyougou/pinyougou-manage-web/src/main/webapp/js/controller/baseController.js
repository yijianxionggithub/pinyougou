app.controller("baseController",function ($scope) {
    //初始化分页参数
    $scope.paginationConf = {
        currentPage:1,//当前页号
        totalItems:10,//总记录数
        itemsPerPage:10,//页大小
        perPageOptions:[10, 20, 30, 40, 50],//可选择的每页大小
        onChange: function () {//当上述的参数发生变化了后触发
            $scope.reloadList();
        }
    };

    //加载数据列表
    $scope.reloadList = function () {
        //$scope.findPage( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);

        $scope.search($scope.paginationConf.currentPage,
            $scope.paginationConf.itemsPerPage);
    };

    //判断选中的checkedbox
    $scope.selectedIds = [];
    $scope.updateSelection = function ($event,id) {
        var flag = $event.target.checked;
        if(flag) {
            $scope.selectedIds.push(id);
        } else {
            var index = $scope.selectedIds.indexOf(id);
            $scope.selectedIds.splice(index,1);
        }
    }

    //json字符串拼接显示
    $scope.jsonToString = function (jsonStr,key) {
        var str = "";

        var jsonArray = JSON.parse(jsonStr);
        for (var i = 0; i < jsonArray.length; i++) {
            if(i > 0) {
                str +=",";
            }

            str +=jsonArray[i][key];
        }

        return str;
    }
})