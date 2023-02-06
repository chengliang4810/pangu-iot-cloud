<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="服务ID" prop="serviceId">
        <el-input
          v-model="queryParams.serviceId"
          placeholder="请输入服务ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="参数标识" prop="key">
        <el-input
          v-model="queryParams.key"
          placeholder="请输入参数标识"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="参数名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入参数名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="参数值" prop="value">
        <el-input
          v-model="queryParams.value"
          placeholder="请输入参数值"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备IDremark" prop="deviceId">
        <el-input
          v-model="queryParams.deviceId"
          placeholder="请输入设备IDremark"
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
          v-hasPermi="['manager:product_service_param:add']"
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
          v-hasPermi="['manager:product_service_param:edit']"
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
          v-hasPermi="['manager:product_service_param:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['manager:product_service_param:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="product_service_paramList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="" align="center" prop="id" v-if="true"/>
      <el-table-column label="服务ID" align="center" prop="serviceId" />
      <el-table-column label="参数标识" align="center" prop="key" />
      <el-table-column label="参数名" align="center" prop="name" />
      <el-table-column label="参数值" align="center" prop="value" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="设备IDremark" align="center" prop="deviceId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manager:product_service_param:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manager:product_service_param:remove']"
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

    <!-- 添加或修改产品功能参数对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="服务ID" prop="serviceId">
          <el-input v-model="form.serviceId" placeholder="请输入服务ID" />
        </el-form-item>
        <el-form-item label="参数标识" prop="key">
          <el-input v-model="form.key" placeholder="请输入参数标识" />
        </el-form-item>
        <el-form-item label="参数名" prop="name">
          <el-input v-model="form.name" placeholder="请输入参数名" />
        </el-form-item>
        <el-form-item label="参数值" prop="value">
          <el-input v-model="form.value" placeholder="请输入参数值" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="设备IDremark" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备IDremark" />
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
import { listProduct_service_param, getProduct_service_param, delProduct_service_param, addProduct_service_param, updateProduct_service_param } from "@/api/manager/product_service_param";

export default {
  name: "Product_service_param",
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
      // 产品功能参数表格数据
      product_service_paramList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceId: undefined,
        key: undefined,
        name: undefined,
        value: undefined,
        deviceId: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        serviceId: [
          { required: true, message: "服务ID不能为空", trigger: "blur" }
        ],
        key: [
          { required: true, message: "参数标识不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "参数名不能为空", trigger: "blur" }
        ],
        value: [
          { required: true, message: "参数值不能为空", trigger: "blur" }
        ],
        remark: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ],
        deviceId: [
          { required: true, message: "设备IDremark不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询产品功能参数列表 */
    getList() {
      this.loading = true;
      listProduct_service_param(this.queryParams).then(response => {
        this.product_service_paramList = response.rows;
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
        serviceId: undefined,
        key: undefined,
        name: undefined,
        value: undefined,
        remark: undefined,
        deviceId: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined
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
      this.title = "添加产品功能参数";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getProduct_service_param(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改产品功能参数";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateProduct_service_param(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addProduct_service_param(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除产品功能参数编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delProduct_service_param(ids);
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
      this.download('manager/product_service_param/export', {
        ...this.queryParams
      }, `product_service_param_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
