webpackJsonp([15],{Jllm:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var l=a("vLgD");var n=a("cAgV"),s=a("IcnI"),i={name:"goodsBackList",directives:{waves:n.a},data:function(){return{tableKey:0,list:null,total:null,listLoading:!0,btnLoading:!1,dialogStockVisible:!1,listQuery:{pageNum:1,pageSize:20,number:"",orderStatus:"",payStatus:""}}},created:function(){this.getList()},methods:{getList:function(){var e,t=this;this.listLoading=!0,(e=this.listQuery,Object(l.a)({url:"/order/orderList",method:"get",params:e})).then(function(e){50001===e.code&&s.a.dispatch("GetRefreshToken").then(function(){t.getList()}),200===e.code&&(t.list=e.data.items,t.total=e.data.total,t.list.forEach(function(e){switch(e.payStatus){case"01":e.payStatus="未付款";break;case"02":e.payStatus="已付款";break;case"03":e.payStatus="已取消"}switch(e.orderStatus){case"01":e.orderStatus="已完成";break;case"02":e.orderStatus="已取消";break;case"03":e.orderStatus="进行中";break;case"04":e.orderStatus="售后中"}switch(e.deliveryStatus){case"01":e.deliveryStatus="已发货";break;case"02":e.deliveryStatus="未发货";break;case"03":e.deliveryStatus="已收货"}}),setTimeout(function(){t.listLoading=!1},1500))}).catch(function(){t.listLoading=!1})},handleFilter:function(){this.listQuery.pageNum=1,this.getList()},handleSizeChange:function(e){this.listQuery.pageSize=e,this.getList()},handleCurrentChange:function(e){this.listQuery.pageNum=e,this.getList()},handleUpdate:function(e){}}},r={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"app-container"},[a("div",{staticClass:"filter-container"},[a("el-input",{staticClass:"filter-item",staticStyle:{width:"200px"},attrs:{clearable:"",placeholder:"订单编号"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.handleFilter(t)}},model:{value:e.listQuery.number,callback:function(t){e.$set(e.listQuery,"number",t)},expression:"listQuery.number"}}),e._v(" "),a("el-select",{staticClass:"filter-item",staticStyle:{width:"140px"},attrs:{clearable:"",placeholder:"订单状态"},on:{change:e.handleFilter},model:{value:e.listQuery.orderStatus,callback:function(t){e.$set(e.listQuery,"orderStatus",t)},expression:"listQuery.orderStatus"}},[a("el-option",{key:"01",attrs:{label:"已完成",value:"01"}}),e._v(" "),a("el-option",{key:"02",attrs:{label:"已取消",value:"02"}}),e._v(" "),a("el-option",{key:"03",attrs:{label:"进行中",value:"03"}}),e._v(" "),a("el-option",{key:"04",attrs:{label:"售后中",value:"04"}})],1),e._v(" "),a("el-select",{staticClass:"filter-item",staticStyle:{width:"140px"},attrs:{clearable:"",placeholder:"付款状态"},on:{change:e.handleFilter},model:{value:e.listQuery.payStatus,callback:function(t){e.$set(e.listQuery,"payStatus",t)},expression:"listQuery.payStatus"}},[a("el-option",{key:"01",attrs:{label:"未付款",value:"01"}}),e._v(" "),a("el-option",{key:"02",attrs:{label:"已付款",value:"02"}}),e._v(" "),a("el-option",{key:"03",attrs:{label:"已取消",value:"03"}})],1),e._v(" "),a("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",attrs:{type:"primary",icon:"el-icon-search"},on:{click:e.handleFilter}},[e._v("\n      "+e._s(e.$t("table.search"))+"\n    ")])],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],key:e.tableKey,staticStyle:{width:"100%","min-height":"100%"},attrs:{data:e.list,stripe:"",border:"",fit:"","highlight-current-row":""}},[a("el-table-column",{attrs:{align:"center",label:e.$t("table.no"),width:"60"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.$index+1))])]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"图片",width:"110"},scopedSlots:e._u([{key:"default",fn:function(e){}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"订单编号","min-width":"110"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.row.number))])]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"价格","min-width":"90"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v("￥"+e._s(t.row.price))])]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"付款状态","min-width":"90"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.row.payStatus))])]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"订单状态","min-width":"90"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.row.orderStatus))])]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"收/发货状态","min-width":"90"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.row.deliveryStatus))])]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"创建时间","min-min-width":"110"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.row.createTime))])]}}])})],1),e._v(" "),a("div",{staticClass:"pagination-container"},[a("el-pagination",{attrs:{background:"","current-page":e.listQuery.pageNum,"page-sizes":[10,20,30,50],"page-size":e.listQuery.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1)],1)},staticRenderFns:[]},u=a("VU/8")(i,r,!1,null,null,null);t.default=u.exports}});