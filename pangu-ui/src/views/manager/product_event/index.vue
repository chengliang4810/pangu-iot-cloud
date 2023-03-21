<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="告警规则名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入告警规则名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="告警等级" prop="level">
        <el-input
          v-model="queryParams.level"
          placeholder="请输入告警等级"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="and 或者 or" prop="expLogic">
        <el-input
          v-model="queryParams.expLogic"
          placeholder="请输入and 或者 or"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="0 否 1 是" prop="notify">
        <el-input
          v-model="queryParams.notify"
          placeholder="请输入0 否 1 是"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="0 告警 1场景联动" prop="classify">
        <el-input
          v-model="queryParams.classify"
          placeholder="请输入0 告警 1场景联动"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务ID" prop="taskId">
        <el-input
          v-model="queryParams.taskId"
          placeholder="请输入任务ID"
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
          v-hasPermi="['manager:product_event:add']"
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
          v-hasPermi="['manager:product_event:edit']"
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
          v-hasPermi="['manager:product_event:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['manager:product_event:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="product_eventList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" v-if="true"/>
      <el-table-column label="告警规则名称" align="center" prop="name" />
      <el-table-column label="告警等级" align="center" prop="level" />
      <el-table-column label="and 或者 or" align="center" prop="expLogic" />
      <el-table-column label="0 否 1 是" align="center" prop="notify" />
      <el-table-column label="0 告警 1场景联动" align="center" prop="classify" />
      <el-table-column label="任务ID" align="center" prop="taskId" />
      <el-table-column label="触发类型 0-条件触发 1-定时触发" align="center" prop="triggerType" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manager:product_event:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manager:product_event:remove']"
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

    <!-- 添加或修改告警规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="告警规则名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入告警规则名称" />
        </el-form-item>
        <el-form-item label="告警等级" prop="level">
          <el-input v-model="form.level" placeholder="请输入告警等级" />
        </el-form-item>
        <el-form-item label="and 或者 or" prop="expLogic">
          <el-input v-model="form.expLogic" placeholder="请输入and 或者 or" />
        </el-form-item>
        <el-form-item label="0 否 1 是" prop="notify">
          <el-input v-model="form.notify" placeholder="请输入0 否 1 是" />
        </el-form-item>
        <el-form-item label="0 告警 1场景联动" prop="classify">
          <el-input v-model="form.classify" placeholder="请输入0 告警 1场景联动" />
        </el-form-item>
        <el-form-item label="任务ID" prop="taskId">
          <el-input v-model="form.taskId" placeholder="请输入任务ID" />
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
import { listProduct_event, getProduct_event, delProduct_event, addProduct_event, updateProduct_event } from "@/api/manager/product_event";

export default {
  name: "Product_event",
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
      // 告警规则表格数据
      product_eventList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        level: undefined,
        expLogic: undefined,
        notify: undefined,
        classify: undefined,
        taskId: undefined,
        triggerType: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "主键不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "告警规则名称不能为空", trigger: "blur" }
        ],
        level: [
          { required: true, message: "告警等级不能为空", trigger: "blur" }
        ],
        expLogic: [
          { required: true, message: "and 或者 or不能为空", trigger: "blur" }
        ],
        notify: [
          { required: true, message: "0 否 1 是不能为空", trigger: "blur" }
        ],
        classify: [
          { required: true, message: "0 告警 1场景联动不能为空", trigger: "blur" }
        ],
        taskId: [
          { required: true, message: "任务ID不能为空", trigger: "blur" }
        ],
        triggerType: [
          { required: true, message: "触发类型 0-条件触发 1-定时触发不能为空", trigger: "change" }
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
    /** 查询告警规则列表 */
    getList() {
      this.loading = true;
      listProduct_event(this.queryParams).then(response => {
        this.product_eventList = response.rows;
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
        name: undefined,
        level: undefined,
        expLogic: undefined,
        notify: undefined,
        classify: undefined,
        taskId: undefined,
        triggerType: undefined,
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
      this.title = "添加告警规则";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getProduct_event(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改告警规则";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateProduct_event(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addProduct_event(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除告警规则编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delProduct_event(ids);
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
      this.download('manager/product_event/export', {
        ...this.queryParams
      }, `product_event_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
