<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="总成件物料编码" prop="materialId">
        <el-input
          v-model="queryParams.materialId"
          placeholder="请输入总成件物料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="总成件物料名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入总成件物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数量" prop="num">
        <el-input
          v-model="queryParams.num"
          placeholder="请输入数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="转序日期" prop="procedingDate">
        <el-date-picker clearable
                        v-model="queryParams.procedingDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择转序日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="报工操作人" prop="operator">
        <el-input
          v-model="queryParams.operator"
          placeholder="请输入报工操作人"
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
          type="info"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:weldingManage:add']"
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
          v-hasPermi="['system:weldingManage:edit']"
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
          v-hasPermi="['system:weldingManage:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:weldingManage:export']"
        >导出</el-button>
      </el-col>

      <!-- 转序涂装 -->
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-transfer"
          size="mini"
          :disabled="multiple"
          @click="handleTransfer"
          v-hasPermi="['system:weldingManage:transfer']"
        >转序涂装</el-button>
      </el-col>

      <!-- Excel转序 -->
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-upload"
          size="mini"
          @click="handleExcelTransfer"
          v-hasPermi="['system:weldingManage:transfer']"
        >Excel转序</el-button>
      </el-col>

      <!-- 转序涂装对话框 -->
      <el-dialog :title="transferTitle" :visible.sync="transferOpen" width="700px" append-to-body>
        <el-table :data="transferList" border>
          <el-table-column label="总成件物料编码" align="center" prop="materialId" width="150" />
          <el-table-column label="总成件物料名称" align="center" prop="materialName" width="300" />
          <el-table-column label="当前库存" align="center" prop="num" width="90" />
          <el-table-column label="扣减数量" align="center">
            <template slot-scope="scope">
              <el-input
                v-model.number="scope.row.reduceNum"
                type="number"
                min="1"
                :max="scope.row.num"
                @input="handleReduceNumChange(scope.row)"
                placeholder="请输入扣减数量"
              />
            </template>
          </el-table-column>
        </el-table>
        <div slot="footer" class="dialog-footer">
          <el-button @click="transferCancel">取 消</el-button>
          <el-button type="primary" @click="confirmTransfer">确 定</el-button>
        </div>
      </el-dialog>

      <!-- Excel转序对话框 -->
      <el-dialog :title="excelTransferTitle" :visible.sync="excelTransferOpen" width="600px" append-to-body>
        <el-upload
          class="upload-demo"
          action="#"
          :auto-upload="false"
          :on-change="handleFileChange"
          :file-list="fileList"
          accept=".xlsx,.xls"
          :limit="1"
        >
          <el-button size="mini" type="primary">选择Excel文件</el-button>
          <div slot="tip" class="el-upload__tip">
            请上传.xlsx或.xls格式的文件，内容需包含"物料编码"和"数量"两列
          </div>
        </el-upload>

        <!-- 预览上传的数据 -->
        <el-table
          v-if="excelData.length > 0"
          :data="excelData"
          border
          style="margin-top: 15px; max-height: 300px; overflow-y: auto;"
        >
          <el-table-column label="物料编码" prop="materialId" width="200" />
          <el-table-column label="扣减数量" prop="reduceNum" width="150" />
          <el-table-column label="状态" prop="status" width="150">
            <template slot-scope="scope">
        <span :class="scope.row.status === '有效' ? 'el-tag el-tag-success' : 'el-tag el-tag-danger'">
          {{ scope.row.status }}
        </span>
            </template>
          </el-table-column>
          <el-table-column label="备注" prop="remark" />
        </el-table>

        <div slot="footer" class="dialog-footer">
          <el-button @click="excelTransferCancel">取 消</el-button>
          <el-button type="primary" @click="confirmExcelTransfer" :loading="excelUploadLoading">确 定</el-button>
        </div>
      </el-dialog>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="weldingManageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="主键" align="center" prop="id" />-->
      <el-table-column label="总成件物料编码" align="center" prop="materialId" />
      <el-table-column label="总成件物料名称" align="center" prop="materialName" />
      <el-table-column label="数量" align="center" prop="num" />
      <el-table-column label="日期" align="center" prop="procedingDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.procedingDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="报工操作人" align="center" prop="operator" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:weldingManage:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:weldingManage:remove']"
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

    <!-- 添加或修改焊装物料库存管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="总成物料编码" prop="materialId">
          <el-input v-model="form.materialId" placeholder="请输入总成物料编码" />
        </el-form-item>
        <el-form-item label="总成物料名称" prop="materialName">
          <el-input v-model="form.materialName" placeholder="请输入总成物料名称" />
        </el-form-item>
        <el-form-item label="数量" prop="num">
          <el-input v-model="form.num" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="转序日期" prop="procedingDate">
          <el-date-picker clearable
                          v-model="form.procedingDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择转序日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="报工操作人" prop="operator">
          <el-input v-model="form.operator" placeholder="请输入报工操作人" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listWeldingManage, getWeldingManage, delWeldingManage, addWeldingManage, updateWeldingManage, transferWeldingManage,transferByExcel } from "@/api/material/weldingManage";

export default {
  name: "WeldingManage",
  data() {
    return {
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
      // 焊装物料库存管理表格数据
      weldingManageList: [],
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
        num: null,
        procedingDate: null,
        operator: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        materialId: [
          { required: true, message: "总成件物料编码不能为空", trigger: "blur" }
        ],
        num: [
          { required: true, message: "数量不能为空", trigger: "blur" }
        ],
        procedingDate: [
          { required: true, message: "转序日期不能为空", trigger: "blur" }
        ],
        operator: [
          { required: true, message: "报工操作人不能为空", trigger: "blur" }
        ],
      },
      // 转序相关
      transferOpen: false,
      transferTitle: "转序涂装",
      transferList: [], // 转序列表

      // Excel转序相关
      excelTransferOpen: false,
      excelTransferTitle: "Excel转序涂装",
      fileList: [],
      excelData: [], // 解析后的Excel数据
      excelUploadLoading: false,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询焊装物料库存管理列表 */
    getList() {
      this.loading = true;
      listWeldingManage(this.queryParams).then(response => {
        this.weldingManageList = response.rows;
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
        procedingDate: null,
        operator: null,
        createDatetime: null,
        updateDatetime: null
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
      this.title = "添加焊装物料库存管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWeldingManage(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改焊装物料库存管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWeldingManage(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWeldingManage(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除焊装物料库存管理编号为"' + ids + '"的数据项？').then(function() {
        return delWeldingManage(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/weldingManage/export', {
        ...this.queryParams
      }, `weldingManage_${new Date().getTime()}.xlsx`)
    },
    /** 转序涂装按钮操作 */
    handleTransfer() {
      // 深拷贝选中的行数据
      this.transferList = this.ids.map(id => {
        const row = this.weldingManageList.find(item => item.id === id);
        return { ...row, reduceNum: 1 }; // 默认扣减1
      });
      this.transferOpen = true;
    },

    /** 扣减数量变化校验 */
    handleReduceNumChange(row) {
      if (row.reduceNum > row.num) {
        this.$message.warning(`扣减数量不能超过当前库存(${row.num})`);
        row.reduceNum = row.num;
      }
      if (row.reduceNum < 1) {
        this.$message.warning("扣减数量不能小于1");
        row.reduceNum = 1;
      }
    },

    /** 取消转序 */
    transferCancel() {
      this.transferOpen = false;
      this.transferList = [];
    },

    /** 确认转序 */
    confirmTransfer() {
      // 校验所有扣减数量
      const invalid = this.transferList.some(row =>
        !row.reduceNum || row.reduceNum < 1 || row.reduceNum > row.num
      );
      if (invalid) {
        this.$message.error("请检查扣减数量是否正确");
        return;
      }

      // 调用转序接口
      transferWeldingManage(this.transferList).then(response => {
        this.$modal.msgSuccess("转序成功");
        this.transferOpen = false;
        this.getList(); // 刷新列表
      }).catch(() => {
        this.$modal.msgError("转序失败");
      });
    },

    /** 打开Excel转序对话框 */
    handleExcelTransfer() {
      this.excelTransferOpen = true;
      this.fileList = [];
      this.excelData = [];
    },

    /** 处理文件选择 */
    handleFileChange(file, fileList) {
      this.fileList = [file]; // 只保留最新选择的文件
      this.parseExcel(file.raw);
    },

    /** 解析Excel文件 */
    parseExcel(file) {
      import('xlsx').then(XLSX => {
        const reader = new FileReader();
        reader.onload = (e) => {
          const data = new Uint8Array(e.target.result);
          const workbook = XLSX.read(data, { type: 'array' });
          const firstSheetName = workbook.SheetNames[0];
          const worksheet = workbook.Sheets[firstSheetName];
          const jsonData = XLSX.utils.sheet_to_json(worksheet);

          // 校验并转换数据格式
          this.excelData = jsonData.map(item => {
            // 适配不同表头可能的命名（物料编码/物料ID，数量/扣减数量）
            const materialId = item['物料编码'] || item['物料ID'] || '';
            const reduceNum = Number(item['数量'] || item['扣减数量'] || 0);
            const status = materialId && reduceNum > 0 ? '有效' : '无效';
            const remark = !materialId ? '缺少物料编码' : (reduceNum <= 0 ? '扣减数量必须大于0' : '');

            return {
              materialId,
              reduceNum,
              status,
              remark
            };
          });
        };
        reader.readAsArrayBuffer(file);
      });
    },

    /** 取消Excel转序 */
    excelTransferCancel() {
      this.excelTransferOpen = false;
      this.fileList = [];
      this.excelData = [];
    },

    /** 确认Excel转序 */
    confirmExcelTransfer() {
      // 校验数据有效性
      const invalidData = this.excelData.filter(item => item.status !== '有效');
      if (invalidData.length > 0) {
        this.$message.error('存在无效数据，请检查Excel内容');
        return;
      }

      if (this.excelData.length === 0) {
        this.$message.warning('请先上传并解析Excel文件');
        return;
      }

      this.excelUploadLoading = true;
      // 调用后端接口
      transferByExcel(this.excelData).then(response => {
        this.$modal.msgSuccess('Excel转序成功');
        this.excelTransferOpen = false;
        this.getList(); // 刷新列表
      }).catch(() => {
        this.$modal.msgError('Excel转序失败');
      }).finally(() => {
        this.excelUploadLoading = false;
      });
    }

  }
};
</script>
