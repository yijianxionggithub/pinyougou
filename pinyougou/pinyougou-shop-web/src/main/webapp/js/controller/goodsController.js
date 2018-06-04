app.controller("goodsController", function ($scope, $controller,$location, goodsService,uploadService,itemCatService,typeTemplateService) {

    //加载baseController控制器并传入1个作用域，与angularJs运行时作用域相同.
    $controller("baseController",{$scope:$scope});

    //加载列表数据
    $scope.findAll = function(){
        goodsService.findAll().success(function (response) {
            $scope.list = response;
        });
    };

    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });
    };

    $scope.save = function () {
        var object;
        //同步商品介绍信息到后台
        $scope.entity.goodsDesc.introduction = editor.html();
        if($scope.entity.goods.id != null){//更新
            object = goodsService.update($scope.entity);
        } else {//新增
            object = goodsService.add($scope.entity);
        }
        object.success(function (response) {
            if(response.success){
                alert(response.message);
               location.href = "goods.html";
            } else {
                alert(response.message);
            }
        });
    };

    //根据编号查询回显
    $scope.findOne = function () {
        //获取地址栏的id
        var id = $location.search()["id"];
        if(id == "" || id == null) {
            return;
        }
        goodsService.findOne(id).success(function (response) {
            $scope.entity = response;

            //显示富文本编辑器的商品介绍
            editor.html($scope.entity.goodsDesc.introduction);

            //将itemImages转为json对象
            $scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);

            //将扩展属性转为json对象
            $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);

            //将规格转为json对象
            $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);

            //
            if($scope.entity.itemList.length > 0) {
                for (var i = 0; i < $scope.entity.itemList.length; i++) {
                    $scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);

                }
            }

        });
    };

    $scope.delete = function () {
        if($scope.selectedIds.length < 1){
            alert("请先选择要删除的记录");
            return;
        }
        if(confirm("确定要删除已选择的记录吗")){
            goodsService.delete($scope.selectedIds).success(function (response) {
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
        goodsService.search(page, rows, $scope.searchEntity).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });

    };

    //上传图片
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (response) {
            //将url放入image_entity={"color":"","url":""}
            $scope.image_entity.url = response.message;
        }).error(function (response) {
            alert(response.message);
        });
    };

    //保存图片
    $scope.entity = {goods:{},goodsDesc:{itemImages:[]}};
    $scope.add_image_entity = function () {
        //将image_entity加入entity中
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
        $scope.image_entity = {};
    }

    //删除图片
    $scope.delete_image_entity = function (index) {
        //将图片信息删除
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }

    //查询顶级分类
    $scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(function (response) {
            $scope.itemCat1List = response;
        });
    };

    //监听一级分类的变化,查询二级分类
    $scope.$watch("entity.goods.category1Id",function (newValue,oldValue) {
        if(newValue != undefined) {
            itemCatService.findByParentId(newValue).success(function (response) {
                $scope.itemCat3List = [];
                $scope.itemCat2List = response;
         });
        }
    });

    //监听二级分类的变化,查询三级分类
    $scope.$watch("entity.goods.category2Id",function (newValue,oldValue) {
        if(newValue != undefined) {
            itemCatService.findByParentId(newValue).success(function (response) {
                $scope.itemCat3List = response;
            });
        }
    });

    //根据分类id查询分类模板id
    $scope.$watch("entity.goods.category3Id",function (newValue,oldValue) {
        if(newValue != undefined) {
            itemCatService.findOne(newValue).success(function (response) {
                $scope.entity.goods.typeTemplateId = response.typeId;
            });
        }
    });

    //查询品牌列表和扩展属性
    $scope.$watch("entity.goods.typeTemplateId",function (newValue,oldValue) {
        if(newValue != undefined) {
            typeTemplateService.findOne(newValue).success(function (response) {
                $scope.typeTemplate = response;
                //将返回的字符串转成json对象
                $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);

                //返回扩展属性
                if($location.search()["id"] == null || $location.search()["id"] == "") {
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
                }
            });

            //根据模板id查询分类模板中的规格,再根据规格的id取查询规格项
            typeTemplateService.findSpecList(newValue).success(function (response) {
                $scope.specList = response;
            });

        }
    });

    //选中规格和规格项
    $scope.entity = {goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};
    $scope.updateSpecAttribute = function ($event,specName,optionName) {
      //查询是否有该属性
      var specObj = $scope.findObjectByKeyAndValue($scope.entity.goodsDesc.specificationItems,"attributeName",specName);

      if(specObj != null) {
          //如果已经存在该规格,并选中
          if($event.target.checked) {
            specObj.attributeValue.push(optionName);
          } else {
            var optIndex = specObj.attributeValue.indexOf(optionName);
            specObj.attributeValue.splice(optIndex,1)
          }

          //如果规格没有规格选项,把规格删除
          if(specObj.attributeValue.length == 0) {
            var specIndex = $scope.entity.goodsDesc.specificationItems.indexOf(specObj);
            $scope.entity.goodsDesc.specificationItems.splice(specIndex,1);
          }

      } else {
          //如果没有存在该规格
          $scope.entity.goodsDesc.specificationItems.push({"attributeName" : specName,"attributeValue" : [optionName]});

      }
    };

    //在集合根据属性key找该对象并返回
    $scope.findObjectByKeyAndValue = function (list,key,value) {
        for (var i = 0; i < list.length; i++) {
            if(list[i][key] == value) {
                return list[i];
            }
        }
        return null;
    };

    //生成sku列表
    $scope.createItemList = function () {
        //初始化集合
        $scope.entity.itemList = [{spec:{},price:0,num:9999,status:"0",isDefault:"0"}];

        for (var i = 0; i < $scope.entity.goodsDesc.specificationItems.length; i++) {
            var spec = $scope.entity.goodsDesc.specificationItems[i]; //{"attributeValue":["移动3G","移动4G"],"attributeName":"网络"}
            $scope.entity.itemList = $scope.addColumn($scope.entity.itemList,spec.attributeName,spec.attributeValue);
        }
    }

    //添加spec
    $scope.addColumn = function (itemList,specName,specOptions) {
        var newItemList = [];
        for (var i = 0; i < itemList.length; i++) {
            var oldItem = itemList[i];
            for (var j = 0; j < specOptions.length; j++) {
                var option = specOptions[j];//3g /4g
                var newItem = JSON.parse(JSON.stringify(oldItem));//{spec:{},price:0,num:9999,status:"0",isDefault:"0"}
                newItem.spec[specName] = option;//{spec:{网络:3g},price:0,num:9999,status:"0",isDefault:"0"}
                newItemList.push(newItem);
            }

        }
        return newItemList;//{spec:{网络:3g},price:0,num:9999,status:"0",isDefault:"0"},{spec:{网络:4g},price:0,num:9999,status:"0",isDefault:"0"}
    };

    //显示状态
    $scope.goodsStatus = ["未审核","审核中","审核通过","审核未通过","关闭"];

    //显示商品类型
    $scope.itemCatList = [];
    $scope.findItemCatList = function () {
        itemCatService.findAll().success(function (response) {
            for (var i = 0; i < response.length; i++) {
                var item = response[i];
                $scope.itemCatList[item.id] = item.name;
            }
        });
    };

    //判断规格,选中规格项
    $scope.checkedAttributeValue = function (specName,optionName) {
        var item = $scope.entity.goodsDesc.specificationItems;
        var specObj = $scope.findObjectByKeyAndValue(item,"attributeName",specName);
        if(specObj != null) {
            if(specObj.attributeValue.indexOf(optionName) >= 0) {
                console.log(optionName);
                return true;
            }
        }
        return false;
    }

    //批量提交审核
    $scope.updateStatus = function (status) {
        if($scope.selectedIds.length < 1){
            alert("请选择要修改的商品");
            return;
        }
        if(confirm("确定要修改选中的商品状态吗?")){
            goodsService.updateStatus($scope.selectedIds,status).success(function (response) {
                if(response.success){
                    $scope.reloadList();
                    $scope.selectedIds = [];
                } else {
                    alert(response.message);
                }
            });
        }
    };

    //修改商品上架下架状态
    $scope.updateMarketable = function (status) {
      //判断是否选中商品
        if($scope.selectedIds.length < 1){
            alert("请先选择要删除的记录!");
            return;
        }

        //判断是否是审核通过的商品
        for (var i = 0; i < $scope.selectedIds.length; i++) {
           if(!$scope.isAuditStatus($scope.selectedIds[i])) {
               alert("请选中已经审核过的商品进行上架!");
               return;
           }
        }


        if(confirm("确定要修改已选择的商品状态吗?")){
            goodsService.updateMarketable($scope.selectedIds,status).success(function (response) {
                if(response.success){
                    $scope.reloadList();
                    $scope.selectedIds = [];
                } else {
                    alert(response.message);
                }
            });
        }
    };

    //判断是否是审核通过的商品
    $scope.isAuditStatus = function (id) {
        for (var i = 0; i < $scope.list.length; i++) {
           if(id == $scope.list[i].id && $scope.list[i].auditStatus != "2") {
                return false;
           }
        }
        return true;
    };

});