<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="下线属性ID" prop="attributeId">
        <el-input
          v-model="queryParams.attributeId"
          placeholder="请输入下线属性ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下线规则函数" prop="ruleFunction">
        <el-input
          v-model="queryParams.ruleFunction"
          placeholder="请输入下线规则函数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下线规则条件" prop="ruleCondition">
        <el-input
          v-model="queryParams.ruleCondition"
          placeholder="请输入下线规则条件"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下线单位描述" prop="unit">
        <el-input
          v-model="queryParams.unit"
          placeholder="请输入下线单位描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上线属性ID" prop="attributeIdRecovery">
        <el-input
          v-model="queryParams.attributeIdRecovery"
          placeholder="请输入上线属性ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上线规则函数" prop="ruleFunctionRecovery">
        <el-input
          v-model="queryParams.ruleFunctionRecovery"
          placeholder="请输入上线规则函数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上线规则条件" prop="ruleConditionRecovery">
        <el-input
          v-model="queryParams.ruleConditionRecovery"
          placeholder="请输入上线规则条件"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上线单位描述" prop="unitRecovery">
        <el-input
          v-model="queryParams.unitRecovery"
          placeholder="请输入上线单位描述"
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
          v-hasPermi="['manager:device_status_function:add']"
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
          v-hasPermi="['manager:device_status_function:edit']"
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
          v-hasPermi="['manager:device_status_function:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['manager:device_status_function:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="device_status_functionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" v-if="true"/>
      <el-table-column label="下线属性ID" align="center" prop="attributeId" />
      <el-table-column label="下线规则函数" align="center" prop="ruleFunction" />
      <el-table-column label="下线规则条件" align="center" prop="ruleCondition" />
      <el-table-column label="下线单位描述" align="center" prop="unit" />
      <el-table-column label="上线属性ID" align="center" prop="attributeIdRecovery" />
      <el-table-column label="上线规则函数" align="center" prop="ruleFunctionRecovery" />
      <el-table-column label="上线规则条件" align="center" prop="ruleConditionRecovery" />
      <el-table-column label="上线单位描述" align="center" prop="unitRecovery" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manager:device_status_function:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manager:device_status_function:remove']"
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

    <!-- 添加或修改设备上下线规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="下线属性ID" prop="attributeId">
          <el-input v-model="form.attributeId" placeholder="请输入下线属性ID" />
        </el-form-item>
        <el-form-item label="下线规则函数" prop="ruleFunction">
          <el-input v-model="form.ruleFunction" placeholder="请输入下线规则函数" />
        </el-form-item>
        <el-form-item label="下线规则条件" prop="ruleCondition">
          <el-input v-model="form.ruleCondition" placeholder="请输入下线规则条件" />
        </el-form-item>
        <el-form-item label="下线单位描述" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入下线单位描述" />
        </el-form-item>
        <el-form-item label="上线属性ID" prop="attributeIdRecovery">
          <el-input v-model="form.attributeIdRecovery" placeholder="请输入上线属性ID" />
        </el-form-item>
        <el-form-item label="上线规则函数" prop="ruleFunctionRecovery">
          <el-input v-model="form.ruleFunctionRecovery" placeholder="请输入上线规则函数" />
        </el-form-item>
        <el-form-item label="上线规则条件" prop="ruleConditionRecovery">
          <el-input v-model="form.ruleConditionRecovery" placeholder="请输入上线规则条件" />
        </el-form-item>
        <el-form-item label="上线单位描述" prop="unitRecovery">
          <el-input v-model="form.unitRecovery" placeholder="请输入上线单位描述" />
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
import { listDevice_status_function, getDevice_status_function, delDevice_status_function, addDevice_status_function, updateDevice_status_function } from "@/api/manager/device_status_function";

export default {
  name: "Device_status_function",
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
      // 设备上下线规则表格数据
      device_status_functionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        attributeId: undefined,
        ruleFunction: undefined,
        ruleCondition: undefined,
        unit: undefined,
        attributeIdRecovery: undefined,
        ruleFunctionRecovery: undefined,
        ruleConditionRecovery: undefined,
        unitRecovery: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "主键不能为空", trigger: "blur" }
        ],
        attributeId: [
          { required: true, message: "下线属性ID不能为空", trigger: "blur" }
        ],
        ruleFunction: [
          { required: true, message: "下线规则函数不能为空", trigger: "blur" }
        ],
        ruleCondition: [
          { required: true, message: "下线规则条件不能为空", trigger: "blur" }
        ],
        unit: [
          { required: true, message: "下线单位描述不能为空", trigger: "blur" }
        ],
        attributeIdRecovery: [
          { required: true, message: "上线属性ID不能为空", trigger: "blur" }
        ],
        ruleFunctionRecovery: [
          { required: true, message: "上线规则函数不能为空", trigger: "blur" }
        ],
        ruleConditionRecovery: [
          { required: true, message: "上线规则条件不能为空", trigger: "blur" }
        ],
        unitRecovery: [
          { required: true, message: "上线单位描述不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
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
    /** 查询设备上下线规则列表 */
    getList() {
      this.loading = true;
      listDevice_status_function(this.queryParams).then(response => {
        this.device_status_functionList = response.rows;
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
        attributeId: undefined,
        ruleFunction: undefined,
        ruleCondition: undefined,
        unit: undefined,
        attributeIdRecovery: undefined,
        ruleFunctionRecovery: undefined,
        ruleConditionRecovery: undefined,
        unitRecovery: undefined,
        status: 0,
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
      this.title = "添加设备上下线规则";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getDevice_status_function(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改设备上下线规则";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateDevice_status_function(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addDevice_status_function(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除设备上下线规则编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delDevice_status_function(ids);
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
      this.download('manager/device_status_function/export', {
        ...this.queryParams
      }, `device_status_function_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
