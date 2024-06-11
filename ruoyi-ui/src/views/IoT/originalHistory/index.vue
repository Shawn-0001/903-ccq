<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="68px">
            <el-form-item label="UUID" prop="UUID">
                <el-input v-model="queryParams.UUID" placeholder="" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <!-- <el-form-item label="时间戳" prop="timestamp">
                <el-input v-model="queryParams.timestamp" placeholder="" clearable @keyup.enter.native="handleQuery" />
            </el-form-item> -->
            <el-form-item label="设备ID" prop="deviceId">
                <el-input v-model="queryParams.deviceId" placeholder="" clearable @keyup.enter.native="handleQuery" />
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

        <el-table v-loading="loading" :data="originalHistoryList">
            <!-- <el-table-column type="selection" width="55" align="center" /> -->
            <el-table-column label="自增序列" align="center" prop="id" />
            <el-table-column label="每条数据的UUID" align="center" prop="uuid" show-overflow-tooltip min-width="150px" />
            <el-table-column label="上传的时间戳" align="center" prop="timestamp" min-width="120px" />
            <el-table-column label="设备ID" align="center" prop="deviceId" min-width="150px" />
            <el-table-column label="原始数据" align="center" prop="data" show-overflow-tooltip min-width="300px" />
            <el-table-column label="处理结果" align="left" prop="remark" show-overflow-tooltip min-width="150px" />
            <el-table-column label="创建者" align="center" prop="createBy" min-width="120px" />
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize" @pagination="getList" />
    </div>
</template>

<script>
import { listOriginalHistory, } from "@/api/IoT/originalHistory";

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
            // 原始数据时间范围
            daterangeCreateTime: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                UUID: null,
                timestamp: null,
                deviceId: null,
                createBy: null,
                createTime: null,
            },
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
        /** 导出按钮操作 */
        handleExport() {
            this.download('IoT/originalHistory/export', {
                ...this.queryParams
            }, `originalHistory_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>