webpackJsonp([18],{"64ul":function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=a("rI3w"),i=a("cAgV"),l=a("IcnI"),s={name:"sellGoodsList",directives:{waves:i.a},filters:{lableFormat:function(t){switch(t){case"01":return"热门商品"}}},data:function(){return{tableKey:0,list:null,total:null,listLoading:!0,btnLoading:!1,dialogStockVisible:!1,listQuery:{pageNum:1,pageSize:20,isDel:"Y",status:"01",name:void 0,model:void 0},tempStock:{id:void 0,currentStock:0,useableStock:0}}},created:function(){this.getList()},methods:{getList:function(){var t=this;this.listLoading=!0,Object(n.g)(this.listQuery).then(function(e){50001===e.code&&l.a.dispatch("GetRefreshToken").then(function(){t.getList()}),200===e.code&&(t.list=e.data.items,t.total=e.data.total,setTimeout(function(){t.listLoading=!1},1500))}).catch(function(){t.listLoading=!1})},handleFilter:function(){this.listQuery.pageNum=1,this.getList()},handleSizeChange:function(t){this.listQuery.pageSize=t,this.getList()},handleCurrentChange:function(t){this.listQuery.pageNum=t,this.getList()},handleUpdate:function(t){},handleXj:function(t,e){var a=this;this.$confirm("下架商品, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(i){var s={id:t,status:e};return Object(n.k)(s).then(function(t){50001===t.code&&l.a.dispatch("GetRefreshToken").then(function(){a.handleXj(e)}),200===t.code&&(a.$message({message:"操作成功",type:"success"}),a.getList())}).catch(function(){a.listLoading=!1}),!0})},handleStock:function(t){var e=this;this.tempStock={id:t.stockId,currentStock:t.currentStock,useableStock:t.useableStock},this.dialogStockVisible=!0,this.$nextTick(function(){e.$refs.dataStock.clearValidate()})},updateStock:function(){var t=this;this.btnLoading=!0,Object(n.j)(this.tempStock).then(function(e){50001===e.code&&l.a.dispatch("GetRefreshToken").then(function(){t.updateStock()}),200===e.code&&(t.dialogStockVisible=!1,t.btnLoading=!1,t.$message({message:"操作成功",type:"success"}),t.getList())}).catch(function(){t.btnLoading=!1})},onJump:function(){this.$router.push("editGoods")}}},o={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"app-container"},[a("div",{staticClass:"filter-container"},[a("el-button",{staticClass:"filter-item",attrs:{type:"success",size:"mini",round:""},on:{click:t.onJump}},[t._v("发布商品")]),t._v(" "),a("div",{staticStyle:{float:"right"}},[a("label",{staticClass:"filter-item"},[t._v("商品名：")]),t._v(" "),a("el-input",{staticClass:"filter-item",staticStyle:{width:"140px"},attrs:{clearable:"",placeholder:"请输入商品名"},on:{change:t.handleFilter},model:{value:t.listQuery.name,callback:function(e){t.$set(t.listQuery,"name",e)},expression:"listQuery.name"}}),t._v(" "),a("label",{staticClass:"filter-item"},[t._v("分类：")]),t._v(" "),a("el-select",{staticClass:"filter-item",staticStyle:{width:"140px"},attrs:{clearable:""},on:{change:t.handleFilter},model:{value:t.listQuery.model,callback:function(e){t.$set(t.listQuery,"model",e)},expression:"listQuery.model"}},[a("el-option",{key:"01",attrs:{label:"新机",value:"01"}}),t._v(" "),a("el-option",{key:"02",attrs:{label:"旧机",value:"02"}})],1),t._v(" "),a("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",attrs:{type:"primary",icon:"el-icon-search",size:"mini"},on:{click:t.handleFilter}},[t._v("\n        "+t._s(t.$t("table.search"))+"\n      ")])],1)],1),t._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.listLoading,expression:"listLoading"}],key:t.tableKey,staticStyle:{width:"100%","min-height":"100%"},attrs:{data:t.list,stripe:"",border:"",fit:"","highlight-current-row":""}},[a("el-table-column",{attrs:{align:"center",label:t.$t("table.no"),width:"60"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[t._v(t._s(e.$index+1))])]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"图片",width:"110"},scopedSlots:t._u([{key:"default",fn:function(e){return[null!=e.row.imgs[0]?a("img",{staticStyle:{width:"90px",height:"50px"},attrs:{src:e.row.imgs[0].url}}):t._e()]}}])}),t._v(" "),a("el-table-column",{attrs:{align:"center",label:"名称","min-width":"110"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[t._v(t._s(e.row.name))])]}}])}),t._v(" "),a("el-table-column",{attrs:{align:"center",label:"价格",width:"120"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[t._v("￥"+t._s(e.row.banPrice))])]}}])}),t._v(" "),a("el-table-column",{attrs:{align:"center",label:"库存",width:"90"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[t._v(t._s(e.row.currentStock))])]}}])}),t._v(" "),a("el-table-column",{attrs:{align:"center",label:"可用库存",width:"110"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[t._v(t._s(e.row.useableStock))])]}}])}),t._v(" "),a("el-table-column",{attrs:{align:"center",label:"标签",width:"120"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[t._v(t._s(t._f("lableFormat")(e.row.lable)))])]}}])}),t._v(" "),a("el-table-column",{attrs:{align:"center",label:"创建时间","min-width":"110"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[t._v(t._s(e.row.createTime))])]}}])}),t._v(" "),a("el-table-column",{attrs:{align:"center",label:"更新时间","min-width":"110"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[t._v(t._s(e.row.lastCreateTime))])]}}])}),t._v(" "),a("el-table-column",{attrs:{width:"70",align:"center",label:t.$t("table.status")},scopedSlots:t._u([{key:"default",fn:function(e){return["01"===e.row.status?a("span",[t._v("上架")]):t._e(),t._v(" "),"02"===e.row.status?a("span",[t._v("下架")]):t._e()]}}])}),t._v(" "),a("el-table-column",{attrs:{align:"center",label:t.$t("table.actions"),width:"220","class-name":"small-padding fixed-width"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{attrs:{size:"mini",type:"success"},on:{click:function(a){t.handleUpdate(e.row)}}},[t._v(t._s(t.$t("table.edit"))+"\n        ")]),t._v(" "),a("el-button",{attrs:{size:"mini",type:"primary"},on:{click:function(a){t.handleStock(e.row)}}},[t._v(t._s(t.$t("table.stock"))+"\n        ")]),t._v(" "),a("el-button",{attrs:{size:"mini",type:"danger"},on:{click:function(a){t.handleXj(e.row.id,"02")}}},[t._v(t._s(t.$t("table.xj"))+"\n        ")])]}}])})],1),t._v(" "),a("div",{staticClass:"pagination-container"},[a("el-pagination",{attrs:{background:"","current-page":t.listQuery.pageNum,"page-sizes":[10,20,30,50],"page-size":t.listQuery.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:t.total},on:{"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}})],1),t._v(" "),a("el-dialog",{attrs:{title:"修改库存",visible:t.dialogStockVisible},on:{"update:visible":function(e){t.dialogStockVisible=e}}},[a("el-form",{ref:"dataStock",staticStyle:{width:"400px","margin-left":"50px"},attrs:{model:t.tempStock,"label-position":"left","label-width":"70px"}},[a("el-form-item",{staticClass:"postInfo-container-item",attrs:{"label-width":"110px",label:"当前库存"}},[a("el-input",{model:{value:t.tempStock.currentStock,callback:function(e){t.$set(t.tempStock,"currentStock",e)},expression:"tempStock.currentStock"}})],1),t._v(" "),a("el-form-item",{staticClass:"postInfo-container-item",attrs:{"label-width":"110px",label:"可用库存"}},[a("el-input",{model:{value:t.tempStock.useableStock,callback:function(e){t.$set(t.tempStock,"useableStock",e)},expression:"tempStock.useableStock"}})],1)],1),t._v(" "),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.dialogFormVisible=!1}}},[t._v(t._s(t.$t("table.cancel")))]),t._v(" "),a("el-button",{directives:[{name:"loading",rawName:"v-loading",value:t.btnLoading,expression:"btnLoading"}],attrs:{type:"primary"},on:{click:t.updateStock}},[t._v(t._s(t.$t("table.confirm")))])],1)],1)],1)},staticRenderFns:[]},c=a("VU/8")(s,o,!1,null,null,null);e.default=c.exports}});