<template>

  <div>

    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 用户管理
        </el-breadcrumb-item>
        <el-breadcrumb-item>查看用户</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="container">
      <div class="handle-box">
        <el-row :gutter="20" style="margin-top: 10px; margin-bottom: 10px">
          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
            <el-input v-model="conditions.email" placeholder="邮箱" maxlength="10" show-word-limit/>
          </el-col>
          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
            <el-input v-model="conditions.name" placeholder="姓名" maxlength="10" show-word-limit/>
          </el-col>
          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
            <el-input v-model="conditions.remain" placeholder="可借阅量"/>
          </el-col>
          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
            <el-button type="primary" icon="el-icon-search" @click="search" style="width: 100%">搜索</el-button>
          </el-col>
        </el-row>
      </div>


      <el-table :data="page.data" border class="table" ref="multipleTable" header-cell-class-name="table-header">
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="remain" label="可借阅量"></el-table-column>
        <el-table-column prop="borrowTotal" label="总借阅次数"></el-table-column>

        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button type="danger" size="small" plain icon="el-icon-delete" class="red"
                       @click="del(scope.$index, scope.row)" v-if="button.indexOf('user-list-delete') !== -1">删除
            </el-button>
            <el-button type="primary" size="small" plain icon="el-icon-edit" class="red"
                       @click="update(scope.$index, scope.row)" v-if="button.indexOf('user-list-edit') !== -1">修改用户
            </el-button>
          </template>
        </el-table-column>


      </el-table>
      <div class="pagination">
        <el-pagination background layout="total, sizes, prev, pager, next"
                       :current-page="page.page"
                       :page-size="page.size"
                       :page-sizes="[10, 20, 50]"
                       :total="page.total"
                       @current-change="handlePageChange"
                       @size-change="handleSizeChange"/>
      </div>
    </div>


  </div>

</template>

<script>
import {ElMessage} from "element-plus";

export default {
  name: "list",
  data() {
    return {
      button: localStorage.getItem('button'),
      page: {
        page: 1,
        size: 10,
        total: "",
        data: []
      },
      conditions: {
        email: "",
        name: "",
        remain: ""
      }
    }
  },
  methods: {
    list() {
      let _this = this
      let url = "/user/list?page=" + this.page.page + "&size=" + _this.page.size
      for (let i in _this.conditions) {
        if (_this.conditions[i] === "" || _this.conditions[i] === null || _this.conditions[i] === undefined) {
          continue;
        }
        url += "&" + i + "=" + _this.conditions[i]
      }
      _this.$axios.get(url).then((resp) => {
        _this.page = resp.data.data
      })
    },
    handlePageChange(page) {
      let _this = this
      _this.page.page = page
      _this.list()
    },
    handleSizeChange(size) {
      let _this = this
      _this.page.size = size
      _this.page.page = 1
      _this.list()
    },
    search() {
      let _this = this
      if (!Number.isInteger(Number(_this.conditions.remain)) || Number(_this.conditions.remain) < 0) {
        _this.conditions.remain = ""
      }
      _this.page.page = 1
      _this.list()
    },
    del(index, row) {
      let _this = this
      _this.$axios.get("/user/delete?email=" + row["email"]).then((resp) => {
        if (resp.data.flag) {
          ElMessage.success(resp.data.message)
        } else {
          ElMessage.warning(resp.data.message)
        }
      })
    },
    update(index, row) {
      let _this = this
      _this.$router.push({
            name: "user-update", params: {
              email: row["email"],
              name: row["name"]
            }
          }
      )
    }
  },
  mounted() {
    let _this = this

    _this.list()


  }
}
</script>

<style scoped>

</style>