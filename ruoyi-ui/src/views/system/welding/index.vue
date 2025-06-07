<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料编码" prop="materialId">
        <el-input
          v-model="queryParams.materialId"
          placeholder="请输入物料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入库时间" prop="entryTime">
        <el-date-picker clearable
                        v-model="queryParams.entryTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择入库时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini"
          @click="handleAdd" v-hasPermi="['system:welding:add']"
        >新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="handleUpdate" v-hasPermi="['system:welding:edit']"
        >修改</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
          @click="handleDelete" v-hasPermi="['system:welding:remove']"
        >删除</el-button>
      </el-col>

      <!--  新增导入按键  -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-upload" size="mini"
          @click="handleImport" v-hasPermi="['system:welding:import']"
        >导入</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini"
          @click="handleExport" v-hasPermi="['system:welding:export']"
        >导出</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-caret-top" size="mini"
          @click="handleSyncStamping" v-hasPermi="['system:welding:syncStamping']"
        >报工</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格 -->
    <el-table ref="table" v-loading="loading" :data="weldingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="${comment}" align="center" prop="id" />-->
      <el-table-column label="物料编码" align="center" prop="materialId" />
      <el-table-column label="物料名称" align="center" prop="materialName" />
      <el-table-column label="数量" align="center" prop="num" />
      <el-table-column label="入库时间" align="center" prop="entryTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.entryTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
<!--      新增报工状态 syncFlag -->
      <el-table-column label="报工状态" align="center" prop="syncFlag" >
        <template slot-scope="scope">
          <el-tag :type="scope.row.syncFlag === 1 ? 'success' : 'info'">
            {{ scope.row.syncFlag === 1 ? '已同步' : '未同步' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:welding:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:welding:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改总成件库存管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物料编码" prop="materialId">
          <el-input v-model="form.materialId" placeholder="请输入物料编码" />
        </el-form-item>
        <el-form-item label="物料名称" prop="materialName">
          <el-input v-model="form.materialName" placeholder="请输入物料名称" />
        </el-form-item>
        <el-form-item label="数量" prop="num">
          <el-input v-model="form.num" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="入库时间" prop="entryTime">
          <el-date-picker clearable
                          v-model="form.entryTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择入库时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 新增excel导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
<!--          <div class="el-upload__tip" slot="tip">-->
<!--            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的物料数据-->
<!--          </div>-->
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listWelding, getWelding, delWelding, addWelding, updateWelding, syncStamping } from "@/api/system/welding";
import { getToken } from "@/utils/auth";

export default {
  name: "Welding",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      matids:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 总成件库存管理表格数据
      weldingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialId: null,
        materialName: null,
        entryTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        materialId: [
          { required: true, message: "物料编码不能为空", trigger: "blur" }
        ],
        materialName: [
          { required: true, message: "物料名称不能为空", trigger: "blur" }
        ],
        num: [
          { required: true, message: "数量不能为空", trigger: "blur" }
        ]
      },
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的物料数据
        updateSupport: "",
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/welding/importData"
      }

    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询总成件库存管理列表 */
    getList() {
      this.loading = true;
      listWelding(this.queryParams).then(response => {
        this.weldingList = response.rows;
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
        id: null,
        materialId: null,
        materialName: null,
        num: null,
        entryTime: null,
        remark: null,
        syncFlag:null
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
      this.mats = selection.map(item => item.materialId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加总成件库存管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWelding(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改总成件库存管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWelding(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWelding(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const mats = row.materialId || this.mats;
      this.$modal.confirm('是否确认删除物料编码为"' + mats + '"的数据项？').then(function() {
        return delWelding(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 报工按钮操作 */
    handleSyncStamping(row) {
      // 安全检查：确保表格引用存在
      if (!this.$refs.table) {
        return this.$message.error('表格组件未正确加载，请刷新页面后重试');
      }

      const selectedRows = this.$refs.table.selection;
      if (selectedRows.length === 0) {
        return this.$message.warning('请选择需要报工的焊装库存记录');
      }

      // 筛选已同步和未同步的记录
      const syncedRows = selectedRows.filter(row => row.syncFlag === 1);
      const unsyncedRows = selectedRows.filter(row => row.syncFlag === 0);

      // 若存在已同步记录，提示具体物料编码
      if (syncedRows.length > 0) {
        // 提取已同步记录的物料编码（最多显示5条）
        const materialIds = syncedRows
          .map(row => row.materialId)
          .slice(0, 5)
          .join('、');

        // 构建提示信息（超过5条时显示数量）
        let message = `选中的记录中包含${syncedRows.length}条已报工数据：`;
        message += syncedRows.length <= 5
          ? `${materialIds}`
          : `${materialIds}等`;
        message += '，请检查后重新提交';

        return this.$message.warning(message);
      }

      // 若所有选中记录均未同步，继续执行同步逻辑
      this.$confirm('确定要同步选中的焊装库存记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          // const weldingIds = unsyncedRows.map(row => row.materialId);
          const response = await syncStamping(unsyncedRows);
          if (response.code === 200) {
            this.$message.success('报工成功');
            this.getList();
          } else {
            this.$message.error(response.msg);
          }
        } catch (error) {
          this.$message.error('报工失败：' + error.message);
        }
      }).catch(() => {
        // 取消操作
      });
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('system/welding/export', {
        ...this.queryParams
      }, `welding_${new Date().getTime()}.xlsx`)
    },

    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "焊装件导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/welding/importTemplate', {
      }, `welding_material_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    }


  }
};
</script>
