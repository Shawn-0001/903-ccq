<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="100px">
            <el-form-item label="上传的时间戳" prop="timestamp">
                <el-input v-model="queryParams.timestamp" placeholder="请输入上传的时间戳" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="设备ID" prop="deviceId">
                <el-input v-model="queryParams.deviceId" placeholder="请输入设备ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="创建者" prop="createBy">
                <el-input v-model="queryParams.createBy" placeholder="请输入创建者" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="创建时间">
                <el-date-picker v-model="daterangeCreateTime" style="width: 300px" value-format="yyyy-MM-dd HH:mm:ss"
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
                    v-hasPermi="['IoT:originalHistory:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['IoT:originalHistory:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
                    @click="handleDelete" v-hasPermi="['IoT:originalHistory:remove']">删除</el-button>
            </el-col> -->
            <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                    v-hasPermi="['IoT:originalHistory:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="originalHistoryList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="自增序列" align="center" prop="id" min-width="100px"/>
            <el-table-column label="上传的时间戳" align="center" prop="timestamp" show-overflow-tooltip min-width="150px" />
            <el-table-column label="设备ID" align="center" prop="deviceId" show-overflow-tooltip width="150px"/>
            <el-table-column label="原始数据" align="center" prop="data" show-overflow-tooltip min-width="400px"/>
            <el-table-column label="处理结果" align="center" prop="remark" show-overflow-tooltip min-width="200px"/>
            <el-table-column label="创建者" align="center" prop="createBy" min-width="150px"/>
            <el-table-column label="创建时间" align="center" prop="createTime" min-width="200px">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['IoT:originalHistory:edit']">修改</el-button>
                    <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['IoT:originalHistory:remove']">删除</el-button>
                </template>
            </el-table-column> -->
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改原始数据历史对话框 -->
        <!-- <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="上传的时间戳" prop="timestamp">
                    <el-input v-model="form.timestamp" placeholder="请输入上传的时间戳" />
                </el-form-item>
                <el-form-item label="设备ID" prop="deviceId">
                    <el-input v-model="form.deviceId" placeholder="请输入设备ID" />
                </el-form-item>
                <el-form-item label="原始数据" prop="data">
                    <el-input v-model="form.data" type="textarea" placeholder="请输入内容" />
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
import { listOriginalHistory, getOriginalHistory, delOriginalHistory, addOriginalHistory, updateOriginalHistory } from "@/api/IoT/originalHistory";

export default {
    name: "OriginalHistory",
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
            // 原始数据历史表格数据
            originalHistoryList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 原始数据时间范围
            daterangeCreateTime: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                timestamp: null,
                deviceId: null,
                createBy: null,
                createTime: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询原始数据历史列表 */
        getList() {
            this.loading = true;
            this.queryParams.params = {};
            if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
                this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
                this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
            }
            listOriginalHistory(this.queryParams).then(response => {
                this.originalHistoryList = response.rows;
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
                timestamp: null,
                deviceId: null,
                data: null,
                createBy: null,
                createTime: null,
                updateBy: null,
                updateTime: null
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
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加原始数据历史";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getOriginalHistory(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改原始数据历史";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateOriginalHistory(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addOriginalHistory(this.form).then(response => {
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
            this.$modal.confirm('是否确认删除原始数据历史编号为"' + ids + '"的数据项？').then(function () {
                return delOriginalHistory(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('IoT/originalHistory/export', {
                ...this.queryParams
            }, `originalHistory_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>