import {createRouter, createWebHashHistory} from "vue-router";
import Home from "../views/Home.vue";
import {ElMessage, useFocus} from "element-plus";

const routes = [
    {
        path: "/",
        name: "Home",
        component: Home,
        children: [
            {
                path: "/dashboard",
                name: "dashboard",
                meta: {
                    title: '系统首页'
                },
                component: () => import ( /* webpackChunkName: "dashboard" */ "../views/Dashboard.vue")
            },
            {
                path: "/book/list",
                name: "book-list",
                meta: {
                    title: "查看图书"
                },
                component: () => import("../views/book/list.vue")
            },
            {
                path: "/book/save",
                name: "book-save",
                meta: {
                    title: "添加图书"
                },
                component: () => import("../views/book/save.vue")
            },
            {
                path: "/book/update",
                name: "book-update",
                meta: {
                    title: "修改图书"
                },
                component: () => import("../views/book/update.vue")
            },
            {
                path: "/user/list",
                name: "user-list",
                meta: {
                    title: "查看用户"
                },
                component: () => import("../views/user/list.vue")
            },
            {
                path: "/user/save",
                name: "user-save",
                meta: {
                    title: "添加用户"
                },
                component: () => import("../views/user/save.vue")
            },
            {
                path: "/user/update",
                name: "user-update",
                meta: {
                    title: "更新用户"
                },
                component: () => import("../views/user/update.vue")
            },
            {
                path: "/borrow/return",
                name: "borrow-return",
                meta: {
                    title: "归还图书"
                },
                component: () => import("../views/borrow/return.vue")
            },
            {
                path: "/borrow/logs",
                name: "borrow-logs",
                meta: {
                    title: "借阅日志"
                },
                component: () => import("../views/borrow/logs.vue")
            }, {
                path: "/icon",
                name: "icon",
                meta: {
                    title: '自定义图标'
                },
                component: () => import ( /* webpackChunkName: "icon" */ "../views/Icon.vue")
            }, {
                path: '/404',
                name: '404',
                meta: {
                    title: '找不到页面'
                },
                component: () => import (/* webpackChunkName: "404" */ '../views/404.vue')
            }, {
                path: '/403',
                name: '403',
                meta: {
                    title: '没有权限'
                },
                component: () => import (/* webpackChunkName: "403" */ '../views/403.vue')
            }
        ]
    }, {
        path: "/login",
        name: "Login",
        meta: {
            title: '登录'
        },
        component: () => import ( /* webpackChunkName: "login" */ "../views/Login.vue")
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

router.beforeEach((to, from, next) => {

    if (!localStorage.getItem("Authorization")) {
        if (to.fullPath !== "/login") {
            ElMessage.error("请先登录！")
            next("/login")
        }
    } else {
        if (localStorage.getItem("path").indexOf(to.fullPath) !== -1) {
            if (localStorage.getItem("path").indexOf(to.fullPath.replace("update", "save"))) {
                next()
            }
            next(from.fullPath)
        }
    }


    document.title = `${to.meta.title} | 图书管理系统`;
    next()

    // const role = localStorage.getItem('ms_username');
    // if (to.meta.permission) {
    //     // 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
    //     role === 'admin'
    //         ? next()
    //         : next('/403');
    // } else {
    //     next();
    // }

});

export default router;