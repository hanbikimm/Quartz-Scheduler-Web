<template>
    <template v-if="isUserLogin">
        <header class="flex items-center justify-between px-6 py-4 bg-white border-b-4 border-indigo-600">
            <div>
                <router-link to="/schedule/list" class="text-2xl text-gray-600 font-bold px-4">
                    Hansol PNS Scheduler 
                </router-link>
            </div>

            <div class="flex items-center">

                <div class="relative">

                    <button class="pt-3 pr-7 text-gray-700" @click="dropdownOpen = !dropdownOpen">
                        {{ $store.state.Member.name }}({{ $store.state.Member.username}})님
                    </button>
                    <div
                    v-show="dropdownOpen"
                    @click="dropdownOpen = false"
                    class="fixed inset-0 z-10 w-full h-full"></div>

                    <transition
                    enter-active-class="transition duration-150 ease-out transform"
                    enter-from-class="scale-95 opacity-0"
                    enter-to-class="scale-100 opacity-100"
                    leave-active-class="transition duration-150 ease-in transform"
                    leave-from-class="scale-100 opacity-100"
                    leave-to-class="scale-95 opacity-0">
                        <div v-show="dropdownOpen"
                            class="absolute right-0 z-20 w-48 py-2 mt-2 bg-white rounded-md shadow-xl">
                            <div class="block px-4 py-2 text-sm text-center text-gray-700">
                                {{ $store.state.Member.email }}
                            </div>
                            <hr/>
                            <button
                            @click="memberLogout()"
                            class="block w-full px-4 py-2 text-sm text-gray-700 hover:bg-indigo-600 hover:text-white">
                                로그아웃
                            </button>
                        </div>
                    </transition>
                </div>
            </div>
        </header>
    </template>
</template>

<script>
import Cookie from '@/assets/Cookie.js';
import router from '@/assets/Router.js';

export default {
    name: 'SchedulerHeader',
    data () {
        return {
            dropdownOpen: false,

        }
    }, 

    computed:{
        isUserLogin(){
            return this.$store.getters.isLogin;
        }
    },

    methods: {
        async memberLogout(){
            try{
                this.dropdownOpen = false;
                this.$store.commit('removeUser');
                localStorage.removeItem('vuex');
                Cookie.removeCookie('user_token');
                alert("로그아웃 되었습니다!");
                router.push({name: 'login'});

            }catch(error){
                alert("로그아웃에 실패했습니다.");
                console.log(error);
            }
            
        },

    }
}
</script>

<style>

</style>