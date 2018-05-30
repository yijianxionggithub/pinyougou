app.controller("brandController",function ($scope,$controller,brandService) {//注入内置对象

    //继承baseController
    $controller("baseController",{$scope:$scope});
    //发送请求查询所有品牌
    $scope.findAll = function(){
        brandService.findAll()
            .success(function(response){
                $scope.list = response;
            });
    }

    //分页查询
    $scope.findPage = function (page,rows) {
        //发送请求查询数据
        brandService.findPage(page,rows).success(function (response) {
            $scope.list = response.rows;
            //总记录数
            $scope.paginationConf.totalItems = response.total;
        })
    }

    //保存一条数据
    $scope.save = function(){
        var obj;
        if($scope.entity.id != null) {
            obj = brandService.update($scope.entity);
        } else {
            obj = brandService.add($scope.entity);
        }
        obj.success(function(response) {
            if(response.success) {
                //重新加载列表
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        });
    }

    //根据id查询一条数据
    $scope.findOne = function (id) {
        //本次写少了一个http,导致页面第一,二条数据显示错误
        brandService.findOne(id).success(function (response) {
            $scope.entity = response;
        });
    }

    //批量删除
    $scope.delete = function () {
        //判断是否有选中数据
        if($scope.selectedIds == null || $scope.selectedIds.length == 0 ) {
            alert("请选择要删除的数据");
            return;
        }
        if(confirm("您确定要删除该数据?")) {
            //删除
            brandService.delete($scope.selectedIds).success(function (response) {
                //重新加载页面
                $scope.reloadList();
            }).error(function (response) {
                alert(reponse.message);
            });
        }
    }

    //条件加分页查询
    //定义空搜索条件对象,第一次访问空时,防止造成400错误
    $scope.searchEntity = {};
    $scope.search = function (page,rows) {
        brandService.search(page,rows,$scope.searchEntity).success(function (response) {
            //页面数据
            $scope.list = response.rows;
            //页面总记录
            $scope.paginationConf.totalItems = response.total;
        });
    }
})