<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="规则ID" prop="ruleId">
        <el-input
          v-model="queryParams.ruleId"
          placeholder="请输入规则ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="函数" prop="function">
        <el-input
          v-model="queryParams.function"
          placeholder="请输入函数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="作用域" prop="scope">
        <el-input
          v-model="queryParams.scope"
          placeholder="请输入作用域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表达式" prop="condition">
        <el-input
          v-model="queryParams.condition"
          placeholder="请输入表达式"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="值" prop="value">
        <el-input
          v-model="queryParams.value"
          placeholder="请输入值"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单位" prop="unit">
        <el-input
          v-model="queryParams.unit"
          placeholder="请输入单位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品/设备ID" prop="relationId">
        <el-input
          v-model="queryParams.relationId"
          placeholder="请输入产品/设备ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="属性ID" prop="productAttributeId">
        <el-input
          v-model="queryParams.productAttributeId"
          placeholder="请输入属性ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="取值周期 时间 周期" prop="period">
        <el-input
          v-model="queryParams.period"
          placeholder="请输入取值周期 时间 周期"
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
          v-hasPermi="['manager:product_event_expression:add']"
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
          v-hasPermi="['manager:product_event_expression:edit']"
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
          v-hasPermi="['manager:product_event_expression:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['manager:product_event_expression:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="product_event_expressionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="" align="center" prop="id" v-if="true"/>
      <el-table-column label="规则ID" align="center" prop="ruleId" />
      <el-table-column label="函数" align="center" prop="function" />
      <el-table-column label="作用域" align="center" prop="scope" />
      <el-table-column label="表达式" align="center" prop="condition" />
      <el-table-column label="值" align="center" prop="value" />
      <el-table-column label="单位" align="center" prop="unit" />
      <el-table-column label="产品/设备ID" align="center" prop="relationId" />
      <el-table-column label="属性ID" align="center" prop="productAttributeId" />
      <el-table-column label="属性类型 属性 事件" align="center" prop="productAttributeType" />
      <el-table-column label="取值周期 时间 周期" align="center" prop="period" />
      <el-table-column label="属性值类型" align="center" prop="attributeValueType" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manager:product_event_expression:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manager:product_event_expression:remove']"
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

    <!-- 添加或修改告警规则达式对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="规则ID" prop="ruleId">
          <el-input v-model="form.ruleId" placeholder="请输入规则ID" />
        </el-form-item>
        <el-form-item label="函数" prop="function">
          <el-input v-model="form.function" placeholder="请输入函数" />
        </el-form-item>
        <el-form-item label="作用域" prop="scope">
          <el-input v-model="form.scope" placeholder="请输入作用域" />
        </el-form-item>
        <el-form-item label="表达式" prop="condition">
          <el-input v-model="form.condition" placeholder="请输入表达式" />
        </el-form-item>
        <el-form-item label="值" prop="value">
          <el-input v-model="form.value" placeholder="请输入值" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="产品/设备ID" prop="relationId">
          <el-input v-model="form.relationId" placeholder="请输入产品/设备ID" />
        </el-form-item>
        <el-form-item label="属性ID" prop="productAttributeId">
          <el-input v-model="form.productAttributeId" placeholder="请输入属性ID" />
        </el-form-item>
        <el-form-item label="取值周期 时间 周期" prop="period">
          <el-input v-model="form.period" placeholder="请输入取值周期 时间 周期" />
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
import { listProduct_event_expression, getProduct_event_expression, delProduct_event_expression, addProduct_event_expression, updateProduct_event_expression } from "@/api/manager/product_event_expression";

export default {
  name: "Product_event_expression",
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
      // 告警规则达式表格数据
      product_event_expressionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleId: undefined,
        function: undefined,
        scope: undefined,
        condition: undefined,
        value: undefined,
        unit: undefined,
        relationId: undefined,
        productAttributeId: undefined,
        productAttributeType: undefined,
        period: undefined,
        attributeValueType: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        ruleId: [
          { required: true, message: "规则ID不能为空", trigger: "blur" }
        ],
        function: [
          { required: true, message: "函数不能为空", trigger: "blur" }
        ],
        scope: [
          { required: true, message: "作用域不能为空", trigger: "blur" }
        ],
        condition: [
          { required: true, message: "表达式不能为空", trigger: "blur" }
        ],
        value: [
          { required: true, message: "值不能为空", trigger: "blur" }
        ],
        unit: [
          { required: true, message: "单位不能为空", trigger: "blur" }
        ],
        relationId: [
          { required: true, message: "产品/设备ID不能为空", trigger: "blur" }
        ],
        productAttributeId: [
          { required: true, message: "属性ID不能为空", trigger: "blur" }
        ],
        productAttributeType: [
          { required: true, message: "属性类型 属性 事件不能为空", trigger: "change" }
        ],
        period: [
          { required: true, message: "取值周期 时间 周期不能为空", trigger: "blur" }
        ],
        attributeValueType: [
          { required: true, message: "属性值类型不能为空", trigger: "change" }
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
    /** 查询告警规则达式列表 */
    getList() {
      this.loading = true;
      listProduct_event_expression(this.queryParams).then(response => {
        this.product_event_expressionList = response.rows;
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
        ruleId: undefined,
        function: undefined,
        scope: undefined,
        condition: undefined,
        value: undefined,
        unit: undefined,
        relationId: undefined,
        productAttributeId: undefined,
        productAttributeType: undefined,
        period: undefined,
        attributeValueType: undefined,
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
      this.title = "添加告警规则达式";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getProduct_event_expression(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改告警规则达式";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateProduct_event_expression(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addProduct_event_expression(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除告警规则达式编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delProduct_event_expression(ids);
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
      this.download('manager/product_event_expression/export', {
        ...this.queryParams
      }, `product_event_expression_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
