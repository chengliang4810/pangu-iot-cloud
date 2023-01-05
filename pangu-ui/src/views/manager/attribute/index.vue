<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="产品ID" prop="productId">
        <el-input
          v-model="queryParams.productId"
          placeholder="请输入产品ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备编号" prop="deviceId">
        <el-input
          v-model="queryParams.deviceId"
          placeholder="请输入设备编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="属性名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入属性名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="属性唯一Key
" prop="key">
        <el-input
          v-model="queryParams.key"
          placeholder="请输入属性唯一Key
"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来源" prop="source">
        <el-input
          v-model="queryParams.source"
          placeholder="请输入来源"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单位描述" prop="unit">
        <el-input
          v-model="queryParams.unit"
          placeholder="请输入单位描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="主条目id
" prop="masterItemId">
        <el-input
          v-model="queryParams.masterItemId"
          placeholder="请输入主条目id
"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label=" 依赖属性 id， 当 source为18时不为空
" prop="dependencyAttrId">
        <el-input
          v-model="queryParams.dependencyAttrId"
          placeholder="请输入 依赖属性 id， 当 source为18时不为空
"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="zabbix ItemId" prop="zbxId">
        <el-input
          v-model="queryParams.zbxId"
          placeholder="请输入zabbix ItemId"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="继承的ID" prop="templateId">
        <el-input
          v-model="queryParams.templateId"
          placeholder="请输入继承的ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="zabbix 值映射ID" prop="valueMapId">
        <el-input
          v-model="queryParams.valueMapId"
          placeholder="请输入zabbix 值映射ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['manager:attribute:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['manager:attribute:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['manager:attribute:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['manager:attribute:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="attributeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" v-if="true"/>
      <el-table-column label="产品ID" align="center" prop="productId" />
      <el-table-column label="设备编号" align="center" prop="deviceId" />
      <el-table-column label="属性名称" align="center" prop="name" />
      <el-table-column label="属性唯一Key
" align="center" prop="key" />
      <el-table-column label="值类型" align="center" prop="valueType" />
      <el-table-column label="来源" align="center" prop="source" />
      <el-table-column label="单位描述" align="center" prop="unit" />
      <el-table-column label="主条目id
" align="center" prop="masterItemId" />
      <el-table-column label=" 依赖属性 id， 当 source为18时不为空
" align="center" prop="dependencyAttrId" />
      <el-table-column label="zabbix ItemId" align="center" prop="zbxId" />
      <el-table-column label="继承的ID" align="center" prop="templateId" />
      <el-table-column label="zabbix 值映射ID" align="center" prop="valueMapId" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manager:attribute:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manager:attribute:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改设备属性对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="产品ID" prop="productId">
          <el-input v-model="form.productId" placeholder="请输入产品ID" />
        </el-form-item>
        <el-form-item label="设备编号" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="属性名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入属性名称" />
        </el-form-item>
        <el-form-item label="属性唯一Key
" prop="key">
          <el-input v-model="form.key" placeholder="请输入属性唯一Key
" />
        </el-form-item>
        <el-form-item label="来源" prop="source">
          <el-input v-model="form.source" placeholder="请输入来源" />
        </el-form-item>
        <el-form-item label="单位描述" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入单位描述" />
        </el-form-item>
        <el-form-item label="主条目id
" prop="masterItemId">
          <el-input v-model="form.masterItemId" placeholder="请输入主条目id
" />
        </el-form-item>
        <el-form-item label=" 依赖属性 id， 当 source为18时不为空
" prop="dependencyAttrId">
          <el-input v-model="form.dependencyAttrId" placeholder="请输入 依赖属性 id， 当 source为18时不为空
" />
        </el-form-item>
        <el-form-item label="zabbix ItemId" prop="zbxId">
          <el-input v-model="form.zbxId" placeholder="请输入zabbix ItemId" />
        </el-form-item>
        <el-form-item label="继承的ID" prop="templateId">
          <el-input v-model="form.templateId" placeholder="请输入继承的ID" />
        </el-form-item>
        <el-form-item label="zabbix 值映射ID" prop="valueMapId">
          <el-input v-model="form.valueMapId" placeholder="请输入zabbix 值映射ID" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAttribute, getAttribute, delAttribute, addAttribute, updateAttribute } from "@/api/manager/attribute";

export default {
  name: "Attribute",
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 设备属性表格数据
      attributeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        productId: undefined,
        deviceId: undefined,
        name: undefined,
        key: undefined,
        valueType: undefined,
        source: undefined,
        unit: undefined,
        masterItemId: undefined,
        dependencyAttrId: undefined,
        zbxId: undefined,
        templateId: undefined,
        valueMapId: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "主键不能为空", trigger: "blur" }
        ],
        productId: [
          { required: true, message: "产品ID不能为空", trigger: "blur" }
        ],
        deviceId: [
          { required: true, message: "设备编号不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "属性名称不能为空", trigger: "blur" }
        ],
        key: [
          { required: true, message: "属性唯一Key
不能为空", trigger: "blur" }
        ],
        valueType: [
          { required: true, message: "值类型不能为空", trigger: "change" }
        ],
        source: [
          { required: true, message: "来源不能为空", trigger: "blur" }
        ],
        unit: [
          { required: true, message: "单位描述不能为空", trigger: "blur" }
        ],
        masterItemId: [
          { required: true, message: "主条目id
不能为空", trigger: "blur" }
        ],
        dependencyAttrId: [
          { required: true, message: " 依赖属性 id， 当 source为18时不为空
不能为空", trigger: "blur" }
        ],
        zbxId: [
          { required: true, message: "zabbix ItemId不能为空", trigger: "blur" }
        ],
        templateId: [
          { required: true, message: "继承的ID不能为空", trigger: "blur" }
        ],
        valueMapId: [
          { required: true, message: "zabbix 值映射ID不能为空", trigger: "blur" }
        ],
        remark: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询设备属性列表 */
    getList() {
      this.loading = true;
      listAttribute(this.queryParams).then(response => {
        this.attributeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        productId: undefined,
        deviceId: undefined,
        name: undefined,
        key: undefined,
        valueType: undefined,
        source: undefined,
        unit: undefined,
        masterItemId: undefined,
        dependencyAttrId: undefined,
        zbxId: undefined,
        templateId: undefined,
        valueMapId: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备属性";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getAttribute(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改设备属性";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateAttribute(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addAttribute(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除设备属性编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delAttribute(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('manager/attribute/export', {
        ...this.queryParams
      }, `attribute_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
