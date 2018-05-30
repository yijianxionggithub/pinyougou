app.service("brandService",function($http) {
    //发送请求查询所有品牌
    this.findAll = function () {
        return $http.get("../brand/findAll.do");
    }

    //分页查询
    this.findPage = function (page,rows) {
        return $http.get("../brand/findPage.do?page=" + page + "&rows=" + rows);
    }

    //新增一条数据
    this.add = function (entity) {
        return $http.post("../brand/add.do",entity)
    }
    
    //更新数据
    this.update = function (entity) {
        return $http.post("../brand/update.do",entity);
    }

    //根据id查询一条数据
    this.findOne = function (id) {
        return $http.get("../brand/findOne.do?id=" + id);
    }

    //批量删除
    this.delete = function (selectedIds) {
        return $http.get("../brand/delete.do?ids=" + selectedIds);
    }

    //条件加分页查询
    this.search = function (page,rows,searchEntity) {
        return $http.post("../brand/search.do?page=" + page + "&rows=" + rows,searchEntity);
    }

    //查询所有品牌
    this.findOptionList = function () {
        return $http.get("../brand/findOptionList.do");
    }

})