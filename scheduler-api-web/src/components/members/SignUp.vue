<template>
  <div class="flex items-center justify-center h-screen px-6 bg-gray-200">
    <div class="w-full max-w-sm p-8 bg-white rounded-md shadow-md">
      <div class="flex items-center justify-center">
        <span class="text-2xl font-semibold text-gray-700">회원가입</span>
      </div>

      <div>
        <label class="block mt-5">
          <span class="text-gray-700 mx-1">아이디 
            <button @click="usernameCheck()" class="text-xs p-1 rounded-md mx-1 border bg-indigo-100 border-gray-200">
              중복확인
            </button>
          </span>
          <input class="block w-full px-4 py-1.5 my-1 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
          type="text" 
          placeholder="사번을 입력해주세요"
          v-model="member.username"/>

          <div v-show="isUsernameValid" class="text-sm mx-1">
            {{usernameMessage}}
          </div>
        </label>

        <label class="block mt-3">
          <span class=" text-gray-700 mx-1">비밀번호</span>
          <input class="block w-full px-4 py-1.5 my-1 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
          type="password" v-model="member.password"/>

          <div v-show="isPwdValid" class="text-sm mx-1">
            {{pwdMessage}}
          </div>
        </label>

        <label class="block mt-3">
         <span class=" text-gray-700 mx-1">비밀번호 확인</span>
         <input class="block w-full px-4 py-1.5 my-1 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
          type="password" v-model="check.password"/>

          <div v-show="isCheckPwdValid" class="text-sm mx-1">
            {{checkPwdMessage}}
          </div>
        </label>

        <label class="block mt-3">
          <span class=" text-gray-700 mx-1">이름</span>
          <input class="block w-full px-4 py-1.5 my-1 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
          type="text" v-model="member.name"/>
        </label>

        <label class="block mt-3">
          <span class=" text-gray-700 mx-1">이메일 
            <button @click="emailCheck()" class="text-xs p-1 rounded-md mx-1 border bg-indigo-100 border-gray-200">
              중복확인
            </button>
          </span>
          <input class="block w-full px-4 py-1.5 my-1 border border-gray-200 rounded-md hover:border-gray-400 focus:outline-none focus:border-gray-400"
          type="text" @keyup.enter="memberRegister()" v-model="member.email"/>

          <div v-show="isEmailValid" class="text-sm mx-1">
            {{emailMessage}}
          </div>
        </label>

        <div class="flex justify-center mt-6">
            <button
              @click="memberRegister()"
              class="px-6 py-3 mx-3 font-medium tracking-wide text-white bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none">
              회원가입
            </button>
            <router-link to="/login"
              class="px-10 py-3 mx-3 text-indigo-500 bg-transparent rounded-lg hover:bg-gray-100 hover:text-indigo-400 focus:outline-none">
              취소
            </router-link>
          </div>
      </div>
    </div>
  </div>
</template>

<script>
import MemberApi from '@/api/MemberApi.js';
import Validation from '@/utils/Validation.js'

export default {
  name: 'SignUp',
  data () {
        return {
          member : {
            username : '',
            password : '',
            name: '',
            email: '',
          },

          check: {
            password: '',
          }
        }
  },

  computed : {
    // 유효성 검사
    isUsernameValid(){
      return !Validation.validateUsername(this.member.username);
    },

    isEmailValid(){
      return !Validation.validateEmail(this.member.email);
    },

    isPwdValid(){
      return !Validation.validatePassword(this.member.password);
    },

    isCheckPwdValid(){
      let result = true
      if(this.check.password == this.member.password){
        result = false;
      }
      return result;
    },

    // 유효성 메세지
    usernameMessage(){
      let message;
      if(this.member.username.length === 0){
        message = "";
      }else if(!Validation.validateUsername(this.member.username)){
        message = "사번 형식이 올바르지 않습니다.";
      }
      return message;
    },

    emailMessage(){
      let message;
      if(this.member.email.length === 0){
        message = "";
      }else if(!Validation.validateEmail(this.member.email)){
        message = "이메일 형식이 올바르지 않습니다.";
      }
      return message;
    },

    pwdMessage(){
      let message;
      if(this.member.password.length === 0){
        message = "";
      }else if(!Validation.validatePassword(this.member.password)){
        message = "8~16자 이내의 영문 대소문자, 숫자를 사용하세요.";
      }
      return message;
    },

    checkPwdMessage(){
      let message
      if(this.check.password.length === 0){
        message = "";
      }else if(this.check.password != this.member.password){
        message = "비밀번호가 일치하지 않습니다.";
      }
      return message
    },

  },
  
  methods: {
    async memberRegister() {
      try{
        if(this.isNull()){
          if(!this.isUsernameValid){
            if(!this.isEmailValid){
              if(!this.isPwdValid && !this.isCheckPwdValid){
                const data = await MemberApi.memberCreate(this.member);
                if(data.success == true){
                  alert("회원가입이 완료되었습니다. 로그인 해주세요!");
                  this.$router.push({name: 'login'});
                }
              }else{
                alert('비밀번호 형식이 맞는지 또는 일치하는지 확인해주세요.');
              }
            }else{
              alert('이메일의 형식이 맞는지 확인해주세요.');
            }
          }else{
            alert('사번의 형식이 맞는지 확인해주세요.');
          }
        }else{
          alert('모든 항목을 작성했는지 확인해주세요.');
        }
      }catch(error){
          alert("잘못된 접근입니다. 다시 시도해주세요!");
          console.log(error);
      }
    },

    async usernameCheck() {
      try{
          if(this.member.username == ''){
            alert('사번을 입력해주세요.');
          }else if(this.isUsernameValid){
            alert('알맞은 사번 형식을 입력해주세요.');
          }else{
            const data = await MemberApi.checkUsername(this.member.username);
            if(data.result == true){
              alert('이미 등록된 사번은 사용하실 수 없습니다. 다시 입력해주세요!');
              this.member.username = '';
            }else{
              alert('이 사번은 사용하실 수 있습니다!');
            }
          }
      }catch(error){
        alert("잘못된 접근입니다. 다시 시도해주세요!");
        console.log(error);
      }
    },

    async emailCheck() {
      try{
          if(this.member.email == ''){
            alert('이메일을 입력해주세요.');
          }else if(this.isEmailValid){
            alert('알맞은 이메일 형식을 입력해주세요.');
          }else{
            const data = await MemberApi.checkEmail(this.member.email);
            if(data.result == true){
              alert('이미 등록된 이메일은 사용하실 수 없습니다. 다시 입력해주세요!');
              this.member.email = '';
            }else{
              alert('이 이메일은 사용하실 수 있습니다!');
            }
          }
      }catch(error){
        alert("잘못된 접근입니다. 다시 시도해주세요!");
          console.log(error);
      }
    },

    isNull(){
      if(this.member.username == '' || this.member.password == '' || 
      this.check.password == '' || this.member.name == '', this.member.email == ''){
        return false;
      }else{
        return true;
      }
    },

  }

}
</script>

<style>
</style>