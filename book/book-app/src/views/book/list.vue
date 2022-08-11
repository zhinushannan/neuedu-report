<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 图书管理
        </el-breadcrumb-item>
        <el-breadcrumb-item>查看图书</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="container">
      <div class="handle-box">
        <el-row :gutter="20">
          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
            <el-input v-model="conditions.name" placeholder="书名" maxlength="15" show-word-limit/>
          </el-col>
          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
            <el-input v-model="conditions.author" placeholder="作者" maxlength="10" show-word-limit/>
          </el-col>
          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
            <el-input v-model="conditions.publish" placeholder="出版社" maxlength="10" show-word-limit/>
          </el-col>
          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
            <el-input v-model="conditions.publishYear" placeholder="出版年份：2001-2010 或 2010" maxlength="9"/>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 10px; margin-bottom: 10px">
          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
            <el-input v-model="conditions.keyWord" placeholder="关键词" maxlength="30" show-word-limit/>
          </el-col>
          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
            <el-input v-model="conditions.abstractOfBook" placeholder="摘要" maxlength="50" show-word-limit/>
          </el-col>
          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
            <el-select v-model="conditions.classifyId" placeholder="选择分类"
                       class="handle-select mr10" filterable style="width: 100%">
              <el-option
                  v-for="item in classifies"
                  :key="item.classifyId"
                  :label="item.classify"
                  :value="item.classifyId"
              />
            </el-select>
          </el-col>

          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6">
            <el-button type="primary" icon="el-icon-search" @click="search" style="width: 100%">搜索</el-button>
          </el-col>

        </el-row>


      </div>


      <el-table :data="page.data" border class="table" ref="multipleTable" header-cell-class-name="table-header">
        <el-table-column prop="name" label="书名"></el-table-column>
        <el-table-column prop="author" label="作者"></el-table-column>
        <el-table-column prop="keyWord" label="关键词"></el-table-column>
        <el-table-column prop="publish" label="出版社"></el-table-column>
        <el-table-column prop="total" label="总量" width="50"></el-table-column>
        <el-table-column prop="remain" label="剩余" width="50"></el-table-column>

        <el-table-column label="操作" width="300" align="center">
          <template #default="scope">
            <el-button type="success" size="small" plain icon="el-icon-lx-down" @click="borrow(scope.$index, scope.row)" v-if="button.indexOf('book-list-borrow') !== -1">借阅
            </el-button>
            <el-button type="danger" size="small" plain icon="el-icon-delete" class="red"
                       @click="del(scope.$index, scope.row)"
                       v-if="button.indexOf('book-list-delete') !== -1">删除
            </el-button>
            <el-button type="primary" size="small" plain icon="el-icon-edit" class="red"
                       @click="update(scope.$index, scope.row)"
                       v-if="button.indexOf('book-list-edit') !== -1">修改
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
      classifies: [],
      conditions: {
        name: "",
        author: "",
        publish: "",
        publishYear: "",
        keyWord: "",
        abstractOfBook: "",
        classifyId: ""
      }
    }
  },
  methods: {
    borrow(index, row) {
      let _this = this
      _this.$axios.get("/borrow/borrow?bookUuid=" + row["uuid"]).then((resp) => {
        if (resp.data.flag) {
          ElMessage.success(resp.data.message)
        } else {
          ElMessage.warning(resp.data.message)
        }
      })
    },
    del(index, row) {
      let _this = this
      _this.$axios.get("/book/delete?uuid=" + row["uuid"]).then((resp) => {
        console.log()
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
            name: "book-update", params: {
              uuid: row["uuid"],
              name: row["name"],
              author: row["author"],
              publish: row["publish"],
              publishYear: row["publishYear"],
              publishMonth: row["publishMonth"],
              keyWord: row["keyWord"],
              abstractOfBook: row["abstractOfBook"],
              classifyId: row["classifyId"],
              total: row["total"],
              remain: row["remain"]
            }
          }
      )
    },
    list() {
      let _this = this
      let url = "/book/list?page=" + this.page.page + "&size=" + _this.page.size
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
      // 校验年份
      let year = _this.conditions.publishYear
      let tempPublishYear = ""
      // 长度为9且格式为 xxxx-xxxx

      if (year.length === 9 && year.indexOf("-") === 4) {
        let temp = _this.conditions.publishYear.split("-")
        // 校验是否都是数字
        if (Number.isInteger(Number(temp[0])) && Number.isInteger(Number(temp[1]))) {
          // 前后年份相同的情况
          if (temp[0] === temp[1]) {
            tempPublishYear = Number(temp[0])
            _this.conditions.publishYear = Number(temp[0])
          }
          // 前小于后的情况
          if (temp[0] < temp[1]) {
            tempPublishYear = year
            _this.conditions.publishYearStart = temp[0]
            _this.conditions.publishYearEnd = temp[1]
          }
        }
      } else if (year.length === 4 && Number.isInteger(Number(year))) {
        tempPublishYear = year
        _this.conditions.publishYear = year
      } else {
        _this.conditions.publishYear = ""
      }
      // 校验分类
      let classifyId = _this.conditions.classifyId
      if (!classifyId in _this.classifies.keys()) {
        _this.conditions.classifyId = ""
      }
      _this.page.page = 1
      _this.list()
      _this.conditions.publishYear = tempPublishYear
    }
  },
  mounted() {
    let _this = this
    _this.list()

    _this.$axios.get("/book/classify/list").then((resp) => {
      _this.classifies = resp.data.data
    })

  }
}
</script>

<style scoped>

</style>