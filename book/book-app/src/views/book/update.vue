<template>
  <div>

    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-redpacket_fill"></i> 图书管理
        </el-breadcrumb-item>
        <el-breadcrumb-item>
          修改图书
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="container">
      <div class="form-box">
        <el-form ref="saveBook" :rules="rules" :model="book" label-width="80px">
          <el-form-item label="书籍ID" prop="name">
            <el-input v-model="book.uuid" disabled></el-input>
          </el-form-item>
          <el-form-item label="书名" prop="name">
            <el-input v-model="book.name"></el-input>
          </el-form-item>
          <el-form-item label="作者" prop="author">
            <el-input v-model="book.author"></el-input>
          </el-form-item>
          <el-form-item label="出版社" prop="publish">
            <el-input v-model="book.publish"></el-input>
          </el-form-item>
          <el-form-item label="出版年份" prop="publishYear">
            <el-input v-model="book.publishYear"></el-input>
          </el-form-item>
          <el-form-item label="出版月份" prop="publishMonth">
            <el-input v-model="book.publishMonth"></el-input>
          </el-form-item>
          <el-form-item label="关键词" prop="keyWord">
            <el-input v-model="book.keyWord"></el-input>
          </el-form-item>
          <el-form-item label="摘要" prop="abstractOfBook">
            <el-input v-model="book.abstractOfBook"></el-input>
          </el-form-item>
          <el-form-item label="分类" prop="classifyId">
            <el-select v-model="book.classifyId" placeholder="请选择分类" filterable style="width: 100%;">
              <el-option
                  v-for="item in classifies"
                  :key="item.classifyId"
                  :label="item.classify"
                  :value="item.classifyId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="总量" prop="total">
            <el-input v-model="book.total"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">表单提交</el-button>
            <el-button @click="onReset">重置表单</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

  </div>
</template>

<script>
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";

export default {
  name: "save",
  data() {
    return {
      rules: {
        name: [{required: true, message: "请输入书名", trigger: "blur"}],
        author: [{required: true, message: "请输入作者", trigger: "blur"}],
        publish: [{required: true, message: "请输入出版社", trigger: "blur"}],
        publishYear: [{required: true, message: "请输入出版年份", trigger: "blur"}],
        publishMonth: [{required: true, message: "请输入出版月份", trigger: "blur"}],
        keyWord: [{required: true, message: "请输入关键词", trigger: "blur"}],
        abstractOfBook: [{required: true, message: "请输入摘要", trigger: "blur"}],
        classifyId: [{required: true, message: "请输入分类", trigger: "blur"}],
        total: [{required: true, message: "请输入总量", trigger: "blur"}]
      },
      book: {
        uuid: "",
        name: "",
        author: "",
        publish: "",
        publishYear: "",
        publishMonth: "",
        keyWord: "",
        abstractOfBook: "",
        classifyId: "",
        total: ""
      },
      oldTotal: "",
      classifies: [],
    }
  },
  methods: {
    // 提交
    onSubmit() {
      let _this = this;
      // 表单校验
      _this.$refs.saveBook.validate((valid) => {
        if (valid) {
          let remain = Number(_this.book["remain"]) - Number(_this.oldTotal) + Number(_this.book.total)
          if (remain < 0) {
            ElMessage.warning("总量不得低于已借出数量");
            return;
          }
          _this.book["remain"] = remain
          _this.$axios.post("/book/save", _this.book).then((resp) => {
            if (resp.data.flag) {
              ElMessage.success(resp.data.message);
            } else {
              ElMessage.warning(resp.data.message);
            }
          })
        } else {
          return false;
        }
      });
    },
    // 重置
    onReset() {
      this.$refs.saveBook.resetFields();
    }
  },
  mounted() {
    let _this = this

    if (_this.$route.path === "/book/update") {

      let uuid = _this.$route.params.uuid

      if (uuid === "" || uuid === null || uuid === undefined) {
        _this.$router.push("/book/list")
      }

      let title = document.title.split("|")
      document.title = "更新书籍 | " + title[1]

      _this.book.uuid = _this.$route.params.uuid
      _this.book.name = _this.$route.params.name
      _this.book.author = _this.$route.params.author
      _this.book.publish = _this.$route.params.publish
      _this.book.publishYear = _this.$route.params.publishYear
      _this.book.publishMonth = _this.$route.params.publishMonth
      _this.book.keyWord = _this.$route.params.keyWord
      _this.book.abstractOfBook = _this.$route.params.abstractOfBook
      _this.book.classifyId = _this.$route.params.classifyId
      _this.book.total = _this.$route.params.total
      _this.book.remain = _this.$route.params.remain
      _this.oldTotal = _this.$route.params.total

    }

    _this.$axios.get("/book/classify/list").then((resp) => {
      _this.classifies = resp.data.data
    })


  }
}
</script>

<style scoped>

</style>