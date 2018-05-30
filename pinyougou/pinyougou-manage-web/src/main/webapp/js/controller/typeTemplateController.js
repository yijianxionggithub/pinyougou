app.controller("typeTemplateController", function ($scope, $controller, typeTemplateService,brandService,specificationService) {

    //加载baseController控制器并传入1个作用域，与angularJs运行时作用域相同.
    $controller("baseController",{$scope:$scope});

    //加载列表数据
    $scope.findAll = function(){
        typeTemplateService.findAll().success(function (response) {
            $scope.list = response;
        });
    };

    $scope.findPage = function (page, rows) {
        typeTemplateService.findPage(page, rows).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });
    };

    $scope.save = function () {
        var object;
        if($scope.entity.id != null){//更新
            object = typeTemplateService.update($scope.entity);
        } else {//新增
            object = typeTemplateService.add($scope.entity);
        }
        object.success(function (response) {
            if(response.success){
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        });
    };

    $scope.findOne = function (id) {
        typeTemplateService.findOne(id).success(function (response) {
            $scope.entity = response;

            //将品牌转成json对象
            $scope.entity.brandIds = JSON.parse($scope.entity.brandIds);

            //将规格转成json对象
            $scope.entity.specIds = JSON.parse($scope.entity.specIds);

            //将扩展属性转成json对象
            $scope.entity.customAttributeItems = JSON.parse($scope.entity.customAttributeItems);
        });
    };

    $scope.delete = function () {
        if($scope.selectedIds.length < 1){
            alert("请先选择要删除的记录");
            return;
        }
        if(confirm("确定要删除已选择的记录吗")){
            typeTemplateService.delete($scope.selectedIds).success(function (response) {
                if(response.success){
                    $scope.reloadList();
                    $scope.selectedIds = [];
                } else {
                    alert(response.message);
                }
            });
        }
    };

    $scope.searchEntity = {};//初始为空
    $scope.search = function (page, rows) {
        typeTemplateService.search(page, rows, $scope.searchEntity).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });

    };

    //查询所有品牌
    $scope.brandList = {data:[]};
    $scope.findBrandList = function () {
        brandService.findOptionList().success(function (response) {
            $scope.brandList.data = response;
        })
    }

    //查询所有规格
    $scope.specList = {data:[]};
    $scope.findSpecList = function () {
        specificationService.findOptionList().success(function (response) {
            $scope.specList.data=response;
        });
    }

    //新增扩展属性
    $scope.addTableRow = function () {
        $scope.entity.customAttributeItems.push({});
    }

    //删除扩展属性
    $scope.removeTableRow = function (index) {
        $scope.entity.customAttributeItems.splice(index,1);
    }

});