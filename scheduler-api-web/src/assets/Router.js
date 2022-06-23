import { createRouter, createWebHistory } from "vue-router";


export default createRouter({
    history: createWebHistory(),
    routes: [
        { 
            path: '/', 
            redirect: {name: 'login'} 
        },
        { 
            path: '/login', name: 'login', 
            component: () => import('@/components/members/LogIn.vue') 
        },
        {
            path: '/register', name: 'register', 
            component: () => import('@/components/members/SignUp.vue')  
        },
        {
            path: '/schedule/list', name: 'schedule list', 
            component: () => import('@/components/schedule/ScheduleList.vue') 
        },
        {
            path: '/schedule/log', name: 'schedule log', 
            component: () => import('@/components/schedule/ScheduleLog.vue')  
        },
        {
            path: '/schedule/detail/:id', name: 'schedule detail',
            component: () => import('@/components/schedule/ScheduleDetail.vue')
        },
        {
            path: '/:catchAll(.*)', name: 'not found',
            component: () => import('@/components/common/NotFound.vue')
        },
    ],
});