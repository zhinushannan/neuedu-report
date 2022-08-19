<template>

  <div>

    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 借阅管理
        </el-breadcrumb-item>
        <el-breadcrumb-item>借阅日志</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="container">

      <el-table :data="page.data" border class="table" ref="multipleTable" header-cell-class-name="table-header">
        <el-table-column prop="book.name" label="书名"></el-table-column>
        <el-table-column prop="book.author" label="作者"></el-table-column>
        <el-table-column prop="book.keyWord" label="关键词"></el-table-column>
        <el-table-column prop="book.publish" label="出版社"></el-table-column>
        <el-table-column prop="borrowLog.borrowDate" label="借阅时间" :formatter="formatDate"></el-table-column>
        <el-table-column prop="borrowLog.returnDate" label="归还时间" :formatter="formatDate"></el-table-column>

        <el-table-column label="操作" width="300" align="center">
          <template #default="scope">
            <el-button type="success" size="small" plain icon="el-icon-lx-top"
                       v-show="!scope.row.borrowLog.returnDate"
                       @click="returnBook(scope.$index, scope.row)" v-if="button.indexOf('borrow-logs-return') !== -1">
              归还
            </el-button>

            <el-button type="success" size="small" plain disabled
                       v-show="scope.row.borrowLog.returnDate"
                       v-if="button.indexOf('borrow-logs-return') !== -1">
              已归还
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
export default {
  name: "logs",
  data() {
    return {
      button: localStorage.getItem('button'),
      page: {
        page: 1,
        size: 10,
        total: "",
        data: []
      }
    }
  },
  methods: {
    list() {
      let _this = this
      _this.$axios.get("/borrow/list?page=" + _this.page.page + "&size=" + _this.page.size).then((resp) => {
        console.log(resp.data.data)
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
    returnBook(index, row) {
      let _this = this
      _this.$axios.get("/borrow/return?_id=" + row["borrowLog"]["_id"]).then((resp) => {
        console.log(resp)
      })
    },
    formatDate(row, column) {
      let date = ""
      if (column["property"] === "borrowLog.borrowDate") {
        let borrowData = row.borrowLog.borrowDate
        date = borrowData.substring(0, borrowData.indexOf(".")).replace("T", " ")
      } else {
        let returnDate = row.borrowLog.returnDate
        if (returnDate !== null) {
          date = returnDate.substring(0, returnDate.indexOf(".")).replace("T", " ")
        }
      }
      return date
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