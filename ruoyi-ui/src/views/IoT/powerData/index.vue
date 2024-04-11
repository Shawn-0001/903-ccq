<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="UUID" prop="UUID">
        <el-input v-model="queryParams.UUID" placeholder="" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="时间戳" prop="timestamp">
        <el-input v-model="queryParams.timestamp" placeholder="请输入时间戳" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="设备ID" prop="deviceId">
        <el-input v-model="queryParams.deviceId" placeholder="请输入设备ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="创建者" prop="createBy">
        <el-input v-model="queryParams.createBy" placeholder="请输入创建者" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker v-model="daterangeCreateTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['IoT:powerData:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['IoT:powerData:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['IoT:powerData:remove']">删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['IoT:powerData:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="powerDataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="自增序列" align="center" prop="id" />
      <el-table-column label="每条数据的UUID" align="center" prop="uuid" show-overflow-tooltip min-width="150px" />
      <el-table-column label="上传的时间戳" align="center" prop="timestamp" />
      <el-table-column label="设备ID" align="center" prop="deviceId" />
      <el-table-column label="A:active_power:有功功率 R: F:" align="center" prop="type">
        <template slot-scope="{}" slot="header">
          <span>功率数据</span>
          <el-tooltip class="item" effect="dark" placement="top">
            <i class="el-icon-question" style="font-size: 14px; vertical-align: middle;"></i>
            <div slot="content">
              <p>P ：有功功率</p>
              <p>Q ：无功功率</p>
              <p>C ：consinΦ,功率因素</p>
            </div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="A相" align="center" prop="phaseA" />
      <el-table-column label="B相" align="center" prop="phaseB" />
      <el-table-column label="C相" align="center" prop="phaseC" />
      <el-table-column label="总计" align="center" prop="total" />
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['IoT:powerData:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['IoT:powerData:remove']">删除</el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改功率数据对话框 -->
    <!-- <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="上传的时间戳" prop="timestamp">
          <el-input v-model="form.timestamp" placeholder="请输入上传的时间戳" />
        </el-form-item>
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item label="A相" prop="phaseA">
          <el-input v-model="form.phaseA" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="B相" prop="phaseB">
          <el-input v-model="form.phaseB" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="C相" prop="phaseC">
          <el-input v-model="form.phaseC" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="总计" prop="total">
          <el-input v-model="form.total" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog> -->
  </div>
</template>

<script>
import { listPowerData } from "@/api/IoT/powerData";

export default {
  name: "PowerData",
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
      // 功率数据表格数据
      powerDataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 总计时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        UUID: null,
        timestamp: null,
        deviceId: null,
        type: null,
        createBy: null,
        createTime: null,
      },
      // 表单参数
      // form: {},
      // 表单校验
      // rules: {
      //   deviceId: [
      //     { required: true, message: "设备ID不能为空", trigger: "blur" }
      //   ],
      //   type: [
      //     { required: true, message: "A:active_power:有功功率 R:reactive_power:无功功率F:power_factor:功率因素不能为空", trigger: "change" }
      //   ],
      // }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询功率数据列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listPowerData(this.queryParams).then(response => {
        this.powerDataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    // cancel() {
    //   this.open = false;
    //   this.reset();
    // },
    // 表单重置
    // reset() {
    //   this.form = {
    //     id: null,
    //     timestamp: null,
    //     deviceId: null,
    //     type: null,
    //     phaseA: null,
    //     phaseB: null,
    //     phaseC: null,
    //     total: null,
    //     createBy: null,
    //     createTime: null,
    //     updateBy: null,
    //     updateTime: null
    //   };
    //   this.resetForm("form");
    // },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    // handleAdd() {
    //   this.reset();
    //   this.open = true;
    //   this.title = "添加功率数据";
    // },
    /** 修改按钮操作 */
    // handleUpdate(row) {
    //   this.reset();
    //   const id = row.id || this.ids
    //   getPowerData(id).then(response => {
    //     this.form = response.data;
    //     this.open = true;
    //     this.title = "修改功率数据";
    //   });
    // },
    /** 提交按钮 */
    // submitForm() {
    //   this.$refs["form"].validate(valid => {
    //     if (valid) {
    //       if (this.form.id != null) {
    //         updatePowerData(this.form).then(response => {
    //           this.$modal.msgSuccess("修改成功");
    //           this.open = false;
    //           this.getList();
    //         });
    //       } else {
    //         addPowerData(this.form).then(response => {
    //           this.$modal.msgSuccess("新增成功");
    //           this.open = false;
    //           this.getList();
    //         });
    //       }
    //     }
    //   });
    // },
    /** 删除按钮操作 */
    // handleDelete(row) {
    //   const ids = row.id || this.ids;
    //   this.$modal.confirm('是否确认删除功率数据编号为"' + ids + '"的数据项？').then(function () {
    //     return delPowerData(ids);
    //   }).then(() => {
    //     this.getList();
    //     this.$modal.msgSuccess("删除成功");
    //   }).catch(() => { });
    // },
    /** 导出按钮操作 */
    handleExport() {
      this.download('IoT/powerData/export', {
        ...this.queryParams
      }, `powerData_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
