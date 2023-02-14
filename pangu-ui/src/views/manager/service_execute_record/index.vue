<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="功能名称" prop="serviceName">
        <el-input
          v-model="queryParams.serviceName"
          placeholder="请输入功能名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="参数" prop="param">
        <el-input
          v-model="queryParams.param"
          placeholder="请输入参数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备ID" prop="deviceId">
        <el-input
          v-model="queryParams.deviceId"
          placeholder="请输入设备ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执行人执行方式未手动触发时有值" prop="executeUser">
        <el-input
          v-model="queryParams.executeUser"
          placeholder="请输入执行人执行方式未手动触发时有值"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执行场景ID" prop="executeRuleId">
        <el-input
          v-model="queryParams.executeRuleId"
          placeholder="请输入执行场景ID"
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
          v-hasPermi="['manager:service_execute_record:add']"
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
          v-hasPermi="['manager:service_execute_record:edit']"
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
          v-hasPermi="['manager:service_execute_record:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['manager:service_execute_record:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="service_execute_recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="" align="center" prop="id" v-if="true"/>
      <el-table-column label="功能名称" align="center" prop="serviceName" />
      <el-table-column label="参数" align="center" prop="param" />
      <el-table-column label="设备ID" align="center" prop="deviceId" />
      <el-table-column label="执行方式   手动触发  场景触发" align="center" prop="executeType" />
      <el-table-column label="执行人执行方式未手动触发时有值" align="center" prop="executeUser" />
      <el-table-column label="执行场景ID" align="center" prop="executeRuleId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manager:service_execute_record:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manager:service_execute_record:remove']"
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

    <!-- 添加或修改功能执行记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="功能名称" prop="serviceName">
          <el-input v-model="form.serviceName" placeholder="请输入功能名称" />
        </el-form-item>
        <el-form-item label="参数" prop="param">
          <el-input v-model="form.param" placeholder="请输入参数" />
        </el-form-item>
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item label="执行人执行方式未手动触发时有值" prop="executeUser">
          <el-input v-model="form.executeUser" placeholder="请输入执行人执行方式未手动触发时有值" />
        </el-form-item>
        <el-form-item label="执行场景ID" prop="executeRuleId">
          <el-input v-model="form.executeRuleId" placeholder="请输入执行场景ID" />
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
import { listService_execute_record, getService_execute_record, delService_execute_record, addService_execute_record, updateService_execute_record } from "@/api/manager/service_execute_record";

export default {
  name: "Service_execute_record",
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
      // 功能执行记录表格数据
      service_execute_recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceName: undefined,
        param: undefined,
        deviceId: undefined,
        executeType: undefined,
        executeUser: undefined,
        executeRuleId: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        serviceName: [
          { required: true, message: "功能名称不能为空", trigger: "blur" }
        ],
        param: [
          { required: true, message: "参数不能为空", trigger: "blur" }
        ],
        deviceId: [
          { required: true, message: "设备ID不能为空", trigger: "blur" }
        ],
        executeType: [
          { required: true, message: "执行方式   手动触发  场景触发不能为空", trigger: "change" }
        ],
        executeUser: [
          { required: true, message: "执行人执行方式未手动触发时有值不能为空", trigger: "blur" }
        ],
        executeRuleId: [
          { required: true, message: "执行场景ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询功能执行记录列表 */
    getList() {
      this.loading = true;
      listService_execute_record(this.queryParams).then(response => {
        this.service_execute_recordList = response.rows;
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
        serviceName: undefined,
        param: undefined,
        deviceId: undefined,
        executeType: undefined,
        executeUser: undefined,
        executeRuleId: undefined,
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
      this.title = "添加功能执行记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getService_execute_record(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改功能执行记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateService_execute_record(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addService_execute_record(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除功能执行记录编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delService_execute_record(ids);
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
      this.download('manager/service_execute_record/export', {
        ...this.queryParams
      }, `service_execute_record_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
