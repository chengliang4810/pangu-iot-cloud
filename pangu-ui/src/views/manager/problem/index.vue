<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="对象ID" prop="objectId">
        <el-input
          v-model="queryParams.objectId"
          placeholder="请输入对象ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="待确认状态" prop="acknowledged">
        <el-input
          v-model="queryParams.acknowledged"
          placeholder="请输入待确认状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="时间" prop="clock">
        <el-date-picker clearable
          v-model="queryParams.clock"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="解决时间" prop="rClock">
        <el-date-picker clearable
          v-model="queryParams.rClock"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择解决时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="设备ID" prop="deviceId">
        <el-input
          v-model="queryParams.deviceId"
          placeholder="请输入设备ID"
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
          v-hasPermi="['manager:problem:add']"
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
          v-hasPermi="['manager:problem:edit']"
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
          v-hasPermi="['manager:problem:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['manager:problem:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="problemList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="event_id" align="center" prop="eventId" v-if="true"/>
      <el-table-column label="对象ID" align="center" prop="objectId" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="待确认状态" align="center" prop="acknowledged" />
      <el-table-column label="时间" align="center" prop="clock" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.clock, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="解决时间" align="center" prop="rClock" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.rClock, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备ID" align="center" prop="deviceId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manager:problem:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manager:problem:remove']"
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

    <!-- 添加或修改告警记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="对象ID" prop="objectId">
          <el-input v-model="form.objectId" placeholder="请输入对象ID" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="待确认状态" prop="acknowledged">
          <el-input v-model="form.acknowledged" placeholder="请输入待确认状态" />
        </el-form-item>
        <el-form-item label="时间" prop="clock">
          <el-date-picker clearable
            v-model="form.clock"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="解决时间" prop="rClock">
          <el-date-picker clearable
            v-model="form.rClock"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择解决时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备ID" />
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
import { listProblem, getProblem, delProblem, addProblem, updateProblem } from "@/api/manager/problem";

export default {
  name: "Problem",
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
      // 告警记录表格数据
      problemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        objectId: undefined,
        name: undefined,
        acknowledged: undefined,
        clock: undefined,
        rClock: undefined,
        deviceId: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        eventId: [
          { required: true, message: "event_id不能为空", trigger: "blur" }
        ],
        objectId: [
          { required: true, message: "对象ID不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        acknowledged: [
          { required: true, message: "待确认状态不能为空", trigger: "blur" }
        ],
        clock: [
          { required: true, message: "时间不能为空", trigger: "blur" }
        ],
        rClock: [
          { required: true, message: "解决时间不能为空", trigger: "blur" }
        ],
        deviceId: [
          { required: true, message: "设备ID不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询告警记录列表 */
    getList() {
      this.loading = true;
      listProblem(this.queryParams).then(response => {
        this.problemList = response.rows;
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
        eventId: undefined,
        objectId: undefined,
        name: undefined,
        acknowledged: undefined,
        clock: undefined,
        rClock: undefined,
        deviceId: undefined
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
      this.ids = selection.map(item => item.eventId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加告警记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const eventId = row.eventId || this.ids
      getProblem(eventId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改告警记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.eventId != null) {
            updateProblem(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addProblem(this.form).then(response => {
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
      const eventIds = row.eventId || this.ids;
      this.$modal.confirm('是否确认删除告警记录编号为"' + eventIds + '"的数据项？').then(() => {
        this.loading = true;
        return delProblem(eventIds);
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
      this.download('manager/problem/export', {
        ...this.queryParams
      }, `problem_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
