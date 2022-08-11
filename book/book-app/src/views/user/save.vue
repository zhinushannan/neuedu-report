<template>
  <div>

    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-redpacket_fill"></i> 用户管理
        </el-breadcrumb-item>
        <el-breadcrumb-item>
          添加用户
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="container">
      <div class="form-box">
        <el-form ref="saveUser" :rules="rules" :model="form" label-width="80px">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" :disabled="isUpdate"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password"></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="角色" prop="role">
            <el-select v-model="form.role" placeholder="请选择角色" filterable style="width: 100%;">
              <el-option
                  v-for="item in roleList"
                  :key="item.name"
                  :label="item.remark"
                  :value="item.name"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">表单提交</el-button>
            <el-button @click="onReset" v-if="!isUpdate">重置表单</el-button>
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
      isUpdate: false,
      rules: {
        email: [
          {required: true, message: "请输入邮箱", trigger: "blur"},
        ],
        password: [
          {required: true, message: "请输入密码", trigger: "blur"},
        ],
        name: [
          {required: true, message: "请输入姓名", trigger: "blur"},
        ],
        role: [
          {required: true, message: "请选择权限", trigger: "blur"},
        ],
      },
      form: {
        email: "",
        name: "",
        password: "",
        role: ""
      },
      roleList: []
    }
  },
  methods: {
    // 提交
    onSubmit() {
      let _this = this
      // 表单校验
      _this.$refs.saveUser.validate((valid) => {
        if (valid) {
          _this.$axios.post("/user/save", _this.form).then((resp) => {
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
      this.$refs.saveUser.resetFields();
    }
  },
  mounted() {
    let _this = this
    _this.$axios.get("/user/role/list").then((resp) => {
      _this.roleList = resp.data.data
    })

  }
}
</script>

<style scoped>

</style>