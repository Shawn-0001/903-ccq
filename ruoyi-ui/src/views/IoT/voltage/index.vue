<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="68px">
            <el-form-item label="UUID" prop="UUID">
                <el-input v-model="queryParams.UUID" placeholder="" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="时间戳" prop="timestamp">
                <el-input v-model="queryParams.timestamp" placeholder="请输入时间戳" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="设备ID" prop="deviceId">
                <el-input v-model="queryParams.deviceId" placeholder="请输入设备ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="创建时间">
                <el-date-picker v-model="daterangeCreateTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss"
                    type="datetimerange" range-separator="-" start-placeholder="开始日期"
                    end-placeholder="结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <!-- <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                    v-hasPermi="['IoT:voltage:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['IoT:voltage:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
                    @click="handleDelete" v-hasPermi="['IoT:voltage:remove']">删除</el-button>
            </el-col> -->
            <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                    v-hasPermi="['IoT:voltage:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="voltageList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="自增序列" align="center" prop="id" />
            <el-table-column label="每条数据的UUID" align="center" prop="uuid" show-overflow-tooltip min-width="150px" />
            <el-table-column label="上传的时间戳" align="center" prop="timestamp" min-width="120px" />
            <el-table-column label="设备ID" align="center" prop="deviceId" min-width="150px" />
            <el-table-column label="电压A，周期1" align="center" prop="voltageA1" show-overflow-tooltip min-width="200px" />
            <el-table-column label="电压A，周期2" align="center" prop="voltageA2" show-overflow-tooltip min-width="200px" />
            <el-table-column label="电压B，周期1" align="center" prop="voltageB1" show-overflow-tooltip min-width="200px" />
            <el-table-column label="电压B，周期2" align="center" prop="voltageB2" show-overflow-tooltip min-width="200px" />
            <el-table-column label="电压C，周期1" align="center" prop="voltageC1" show-overflow-tooltip min-width="200px" />
            <el-table-column label="电压C，周期2" align="center" prop="voltageC2" show-overflow-tooltip min-width="200px" />
            <el-table-column label="创建者" align="center" prop="createBy" min-width="120px" />
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改电压数据对话框 -->
        <!-- <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="上传的时间戳" prop="timestamp">
                    <el-input v-model="form.timestamp" placeholder="请输入上传的时间戳" />
                </el-form-item>
                <el-form-item label="设备ID" prop="deviceId">
                    <el-input v-model="form.deviceId" placeholder="请输入设备ID" />
                </el-form-item>
                <el-form-item label="电压A，周期1" prop="voltageA1">
                    <el-input v-model="form.voltageA1" type="textarea" placeholder="请输入内容" />
                </el-form-item>
                <el-form-item label="电压A，周期2" prop="voltageA2">
                    <el-input v-model="form.voltageA2" type="textarea" placeholder="请输入内容" />
                </el-form-item>
                <el-form-item label="电压B，周期1" prop="voltageB1">
                    <el-input v-model="form.voltageB1" type="textarea" placeholder="请输入内容" />
                </el-form-item>
                <el-form-item label="电压B，周期2" prop="voltageB2">
                    <el-input v-model="form.voltageB2" type="textarea" placeholder="请输入内容" />
                </el-form-item>
                <el-form-item label="电压C，周期1" prop="voltageC1">
                    <el-input v-model="form.voltageC1" type="textarea" placeholder="请输入内容" />
                </el-form-item>
                <el-form-item label="电压C，周期2" prop="voltageC2">
                    <el-input v-model="form.voltageC2" type="textarea" placeholder="请输入内容" />
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
import { listVoltage, getVoltage, delVoltage, addVoltage, updateVoltage } from "@/api/IoT/voltage";

export default {
    name: "Voltage",
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
            // 电压数据表格数据
            voltageList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 电压C，周期2时间范围
            daterangeCreateTime: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                UUID: null,
                timestamp: null,
                deviceId: null,
                createTime: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                deviceId: [
                    { required: true, message: "设备ID不能为空", trigger: "blur" }
                ],
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询电压数据列表 */
        getList() {
            this.loading = true;
            this.queryParams.params = {};
            if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
                this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
                this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
            }
            listVoltage(this.queryParams).then(response => {
                this.voltageList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        // 取消按钮
        // cancel() {
        //     this.open = false;
        //     this.reset();
        // },
        // 表单重置
        // reset() {
        //     this.form = {
        //         id: null,
        //         uuid: null,
        //         timestamp: null,
        //         deviceId: null,
        //         voltageA1: null,
        //         voltageA2: null,
        //         voltageB1: null,
        //         voltageB2: null,
        //         voltageC1: null,
        //         voltageC2: null,
        //         createBy: null,
        //         createTime: null,
        //         updateBy: null,
        //         updateTime: null
        //     };
        //     this.resetForm("form");
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
        //     this.reset();
        //     this.open = true;
        //     this.title = "添加电压数据";
        // },
        /** 修改按钮操作 */
        // handleUpdate(row) {
        //     this.reset();
        //     const id = row.id || this.ids
        //     getVoltage(id).then(response => {
        //         this.form = response.data;
        //         this.open = true;
        //         this.title = "修改电压数据";
        //     });
        // },
        /** 提交按钮 */
        // submitForm() {
        //     this.$refs["form"].validate(valid => {
        //         if (valid) {
        //             if (this.form.id != null) {
        //                 updateVoltage(this.form).then(response => {
        //                     this.$modal.msgSuccess("修改成功");
        //                     this.open = false;
        //                     this.getList();
        //                 });
        //             } else {
        //                 addVoltage(this.form).then(response => {
        //                     this.$modal.msgSuccess("新增成功");
        //                     this.open = false;
        //                     this.getList();
        //                 });
        //             }
        //         }
        //     });
        // },
        /** 删除按钮操作 */
        // handleDelete(row) {
        //     const ids = row.id || this.ids;
        //     this.$modal.confirm('是否确认删除电压数据编号为"' + ids + '"的数据项？').then(function () {
        //         return delVoltage(ids);
        //     }).then(() => {
        //         this.getList();
        //         this.$modal.msgSuccess("删除成功");
        //     }).catch(() => { });
        // },
        /** 导出按钮操作 */
        handleExport() {
            this.download('IoT/voltage/export', {
                ...this.queryParams
            }, `voltage_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>