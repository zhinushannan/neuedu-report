<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">后台管理系统</div>
      <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
        <el-form-item prop="username">
          <el-input v-model="param.username" placeholder="username">
            <template #prepend>
              <el-button icon="el-icon-user"></el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" placeholder="password" v-model="param.password"
                    @keyup.enter="submitForm()">
            <template #prepend>
              <el-button icon="el-icon-lock"></el-button>
            </template>
          </el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button type="primary" @click="submitForm()">登录</el-button>
        </div>
        <p class="login-tips">Tips : 用户名和密码随便填。</p>
      </el-form>
    </div>
  </div>
</template>

<script>
import {ref, reactive} from "vue";
import {useTagsStore} from '../store/tags'
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";
import axios from "axios";

export default {
  methods: {
    submitForm() {
      let _this = this
      _this.$refs.login.validate((valid) => {
        if (valid) {
          _this.$axios.post("/login", 'username=' + _this.param.username + '&password=' + _this.param.password).then((resp) => {
            let data = resp.data
            if (data.flag) {
              ElMessage.success(data.message)

              let items = data.data["items"]
              let button = data.data["button"]

              let path = ["/login"]
              for (let i in items) {
                if (items[i]["subs"].length === 0 || items[i]["subs"] === null) {
                  delete (items[i]["subs"])
                  path.push(items[i]["index"])
                } else {
                  for (let j in items[i]["subs"]) {
                    if (items[i]["subs"][j]["subs"] === null || items[i]["subs"][j]["subs"] === undefined || items[i]["subs"][j]["subs"].length === 0) {
                      delete (items[i]["subs"][j]["subs"])
                      path.push(items[i]["subs"][j]["index"])
                    }
                  }
                }
              }

              localStorage.setItem("items", JSON.stringify(items))
              localStorage.setItem("button", button)
              localStorage.setItem("path", path)
              localStorage.setItem("Authorization", resp.headers["authorization"])

              localStorage.setItem("websiteInfo", JSON.stringify(data.data["websiteInfo"]))
              localStorage.setItem("email", data.data["email"])
              localStorage.setItem("websiteRate", JSON.stringify(data.data["websiteRate"]))

              _this.$router.push("/dashboard")
            } else {
              ElMessage.error(data.message)
            }
          })
        } else {
          ElMessage.error("请按要求填写用户名和密码！");
          return false;
        }
      });
    }
  },
  data() {
    return {
      param: {
        username: "admin@kwcoder.club",
        password: "123456",
      },
      rules: {
        username: [
          {
            required: true,
            message: "请输入用户名",
            trigger: "blur",
          },
        ],
        password: [
          {required: true, message: "请输入密码", trigger: "blur"},
        ],
      },
    }
  },
  mounted() {
    let tags = useTagsStore();
    tags.clearTags();
  }
}
</script>

<style scoped>
.login-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  background-image: url(../assets/img/login-bg.jpg);
  background-size: 100%;
}

.ms-title {
  width: 100%;
  line-height: 50px;
  text-align: center;
  font-size: 20px;
  color: #fff;
  border-bottom: 1px solid #ddd;
}

.ms-login {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 350px;
  margin: -190px 0 0 -175px;
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.3);
  overflow: hidden;
}

.ms-content {
  padding: 30px 30px;
}

.login-btn {
  text-align: center;
}

.login-btn button {
  width: 100%;
  height: 36px;
  margin-bottom: 10px;
}

.login-tips {
  font-size: 12px;
  line-height: 30px;
  color: #fff;
}
</style>