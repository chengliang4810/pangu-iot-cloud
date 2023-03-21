<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备编号" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入设备编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备分组ID" prop="groupId">
        <el-input
          v-model="queryParams.groupId"
          placeholder="请输入设备分组ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品ID" prop="productId">
        <el-input
          v-model="queryParams.productId"
          placeholder="请输入产品ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入设备地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地址坐标" prop="position">
        <el-input
          v-model="queryParams.position"
          placeholder="请输入地址坐标"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最近在线时间" prop="latestOnline">
        <el-date-picker clearable
          v-model="queryParams.latestOnline"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择最近在线时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="Zabbix对应模板ID
" prop="zbxId">
        <el-input
          v-model="queryParams.zbxId"
          placeholder="请输入Zabbix对应模板ID
"
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
          v-hasPermi="['manager:device:add']"
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
          v-hasPermi="['manager:device:edit']"
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
          v-hasPermi="['manager:device:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['manager:device:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deviceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="设备主键" align="center" prop="id" v-if="true"/>
      <el-table-column label="设备编号" align="center" prop="code" />
      <el-table-column label="设备分组ID" align="center" prop="groupId" />
      <el-table-column label="产品ID" align="center" prop="productId" />
      <el-table-column label="设备名称" align="center" prop="name" />
      <el-table-column label="设备类型" align="center" prop="type" />
      <el-table-column label="设备地址" align="center" prop="address" />
      <el-table-column label="地址坐标" align="center" prop="position" />
      <el-table-column label="最近在线时间" align="center" prop="latestOnline" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.latestOnline, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="Zabbix对应模板ID
" align="center" prop="zbxId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manager:device:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manager:device:remove']"
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

    <!-- 添加或修改设备对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备编号" prop="code">
          <el-input v-model="form.code" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="设备分组ID" prop="groupId">
          <el-input v-model="form.groupId" placeholder="请输入设备分组ID" />
        </el-form-item>
        <el-form-item label="产品ID" prop="productId">
          <el-input v-model="form.productId" placeholder="请输入产品ID" />
        </el-form-item>
        <el-form-item label="设备名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="设备地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入设备地址" />
        </el-form-item>
        <el-form-item label="地址坐标" prop="position">
          <el-input v-model="form.position" placeholder="请输入地址坐标" />
        </el-form-item>
        <el-form-item label="最近在线时间" prop="latestOnline">
          <el-date-picker clearable
            v-model="form.latestOnline"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择最近在线时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="Zabbix对应模板ID
" prop="zbxId">
          <el-input v-model="form.zbxId" placeholder="请输入Zabbix对应模板ID
" />
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
import {addDevice, delDevice, getDevice, listDevice, updateDevice} from "@/api/manager/device";

export default {
  name: "Device",
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
      // 设备表格数据
      deviceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: undefined,
        groupId: undefined,
        productId: undefined,
        name: undefined,
        type: undefined,
        address: undefined,
        position: undefined,
        latestOnline: undefined,
        zbxId: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "设备主键不能为空", trigger: "blur" }
        ],
        code: [
          { required: true, message: "设备编号不能为空", trigger: "blur" }
        ],
        groupId: [
          { required: true, message: "设备分组ID不能为空", trigger: "blur" }
        ],
        productId: [
          { required: true, message: "产品ID不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "设备名称不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "设备类型不能为空", trigger: "change" }
        ],
        address: [
          { required: true, message: "设备地址不能为空", trigger: "blur" }
        ],
        position: [
          { required: true, message: "地址坐标不能为空", trigger: "blur" }
        ],
        latestOnline: [
          { required: true, message: "最近在线时间不能为空", trigger: "blur" }
        ],
        remark: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ],
        zbxId: [
          { required: true, message: "Zabbix对应模板ID不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询设备列表 */
    getList() {
      this.loading = true;
      listDevice(this.queryParams).then(response => {
        this.deviceList = response.rows;
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
        code: undefined,
        groupId: undefined,
        productId: undefined,
        name: undefined,
        type: undefined,
        address: undefined,
        position: undefined,
        latestOnline: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        remark: undefined,
        zbxId: undefined
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
      this.title = "添加设备";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getDevice(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改设备";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateDevice(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addDevice(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除设备编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delDevice(ids);
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
      this.download('manager/device/export', {
        ...this.queryParams
      }, `device_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
