<template>
    <div class="app-container home">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="68px">
            <!-- <el-form-item label="UUID" prop="UUID">
                <el-input v-model="queryParams.UUID" placeholder="" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="时间戳" prop="timestamp">
                <el-input v-model="queryParams.timestamp" placeholder="" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="设备ID" prop="deviceId">
                <el-input v-model="queryParams.deviceId" placeholder="" clearable @keyup.enter.native="handleQuery" />
            </el-form-item> -->
            <el-form-item label="创建时间">
                <el-date-picker v-model="daterangeCreateTime" style="width: 350px" value-format="yyyy-MM-dd HH:mm:ss"
                    type="datetimerange" range-separator="-" start-placeholder="开始时间"
                    end-placeholder="结束时间"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>


        <el-table v-loading="loading" :data="currentList" @selection-change="handleSelectionChange">
            <el-table-column :prop="index" :label="item" sortable show-overflow-tooltip
                v-for="(item, index) in tableHeader" :key="index">
            </el-table-column>

            <el-table-column label="设备名称" align="center" prop="deviceName" min-width="150px" />
            <el-table-column label="设备编号" align="center" prop="deviceId" min-width="150px" />

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

export default {
    name: "ConsumptionReport",
    data() {
        return {
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 时间范围
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
        }
    },
    methods: {
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
        /** 查询数据列表 */
        getList() {
            this.loading = true;
            this.queryParams.params = {};
            if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
                this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
                this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
            }
            listCurrent(this.queryParams).then(response => {
                this.currentList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
    }
}
</script>