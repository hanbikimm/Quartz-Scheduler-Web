<template>
  <div class="flex items-center justify-center h-screen px-6 bg-gray-200">
    <div class="w-full max-w-sm p-8 bg-white rounded-md shadow-md">
      <div class="flex items-center justify-center">
        <span class="text-2xl font-semibold text-gray-700">Hansol PNS Scheduler</span>
      </div>

      <div class="mt-6">
        <div>
          <label class="block">
            <input class="block w-full px-4 py-2 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
            type="text" 
            placeholder="사번 또는 이메일 입력"
            v-model="member.username"/>
          </label>
        </div>

        <div>
          <label class="block mt-4">
            <input class="block w-full px-4 py-2 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
            type="password" 
            placeholder="비밀번호 입력"
            @keyup.enter="memberLogin()"
            v-model="member.password"/>
          </label>
        </div>

        <div class="flex items-center justify-end mt-4">
          <router-link to="/register"
            class="block text-sm text-indigo-700  fontme hover:underline">
            아직 회원이 아니신가요?
          </router-link>
        </div>

        <div class="mt-6">
          <button 
            @click="memberLogin()"
            class="w-full px-4 py-2 text-sm text-center text-white bg-indigo-600 rounded-md focus:outline-none hover:bg-indigo-500">
            로그인
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import MemberApi from '../../api/MemberApi.js';
import Cookie from '../../assets/Cookie.js';

export default {
  name: 'LogIn',
  data () {
    return{
      member: {
        username: '',
        password: '',
      }
    }
  },

  created(){
    this.ifLogined();
  },

  methods : {

    async memberLogin() {
      try {
        if(this.isNull()){
          const data = await MemberApi.memberSignin(this.member);
          Cookie.setCookie('user_token', data.result.token);
          this.$store.commit('setUser', data.result);
          alert(`로그인에 성공했습니다! ${data.result.name}님 환영합니다!`);
          this.$router.push({name: 'schedule list'});
        }else{
          alert('아이디와 비밀번호를 입력했는지 확인해주세요.');
        }
      }catch(error){
        console.log(error);
      }
    },

    isNull(){
      if(this.member.username == '' || this.member.password == ''){
        return false;
      }else{
        return true;
      }
    },

    ifLogined(){
      if(localStorage.getItem('vuex')){
        this.$router.push({name: 'schedule list'});
      }
    }

    
  },
}

</script>

<style>

</style>